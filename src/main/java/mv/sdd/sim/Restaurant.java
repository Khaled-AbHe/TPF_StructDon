package mv.sdd.sim;

import mv.sdd.io.Action;
import mv.sdd.model.*;
import mv.sdd.sim.thread.Cuisinier;
import mv.sdd.utils.Constantes;
import mv.sdd.utils.Formatter;
import mv.sdd.utils.Logger;

import java.util.*;


public class Restaurant {
    private final Logger logger;
    // TODO : Ajouter les attributs nécessaires ainsi que les getters et les setters
    private final HashMap<Integer, Client> clients;
    private final ArrayDeque<Commande> FILE_DE_COMMANDES = new ArrayDeque<>();
    private final HashSet<Commande> COMMANDES_EN_PREP = new HashSet<>();
    private final List<Thread> CUISINIERS = new ArrayList<>();
    private final Object TEMPS_VERROU = new Object();
    private final Horloge HORLOGE = new Horloge();
    private final Stats STATS;

    // getters
    public int getTemps() {
        return HORLOGE.getTempsSimule();
    }

    public Object getTempsVerrou() {
        return TEMPS_VERROU;
    }

    public ArrayDeque<Commande> getFILE_DE_COMMANDES() {
        return FILE_DE_COMMANDES;
    }

    // TODO : Ajouter le(s) constructeur(s)
    public Restaurant(Logger logger, HashMap<Integer, Client> clients) {
        this.logger = logger;
        this.clients = clients;
        this.STATS = new Stats(HORLOGE);
    }

    // DONE : implémenter les méthodes suivantes
    // Méthode appelée depuis App pour chaque action
    public void executerAction(Action action) {
        // Votre code ici.
        switch (action.getType()) {
            case DEMARRER_SERVICE:
                demarrerService(action.getParam1(), action.getParam2());
                break;
            case AJOUTER_CLIENT:
                ajouterClient(action.getParam1(), action.getParam3(), action.getParam2());
                break;
            case PASSER_COMMANDE:
                synchronized (FILE_DE_COMMANDES) {
                    FILE_DE_COMMANDES.add(passerCommande(action.getParam1(), MenuPlat.valueOf(action.getParam3())));
                    FILE_DE_COMMANDES.notifyAll();
                }
                break;
            case AVANCER_TEMPS:
                logger.logLine(Constantes.AVANCER_TEMPS + action.getParam1());
                avancerTemps(action.getParam1());
                break;
            case AFFICHER_ETAT:
                afficherEtat();
                break;
            case AFFICHER_STATS:
                afficherStatistiques();
                break;
            case QUITTER:
                break;
        }
    }

    public void demarrerService(int dureeMax, int nbCuisiniers) {
        // Votre code ici.
        logger.logLine(String.format(Constantes.DEMARRER_SERVICE, dureeMax, nbCuisiniers));
        for (int i = 0; i < nbCuisiniers; i++) {
            Thread t = new Thread(new Cuisinier(this));
            CUISINIERS.add(t);
            t.start();
        }
    }

    public void avancerTemps(int minutes) {
        // Votre code ici.
        for  (int i = 0; i < minutes; i++) {
            tick();
        }
    }

    public void arreterService(){
        // Votre code ici.
        CUISINIERS.forEach(Thread::interrupt);

        synchronized (FILE_DE_COMMANDES) {
            FILE_DE_COMMANDES.notifyAll();
        }
        synchronized (TEMPS_VERROU) {
            TEMPS_VERROU.notifyAll();
        }

        for (Thread cuisinier : CUISINIERS) {
            try {
                cuisinier.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // DONE : Déclarer et implémenter les méthodes suivantes
    // tick() | This is the method that manipulates the time in minutes
    private void tick() {
        synchronized (TEMPS_VERROU) {
            HORLOGE.avancerTempsSimule(1);
            TEMPS_VERROU.notifyAll();
        }
        diminuerPatienceClients(1);
        progresserCommandesEnPreparation(1);
    }

    // DONE : afficherEtat()
    private void afficherEtat() {
        logger.logLine(Formatter.resumeEtat(
            HORLOGE.getTempsSimule(),
            STATS.getTotalClients(),
            STATS.getNbServis(),
            STATS.getNbFaches(),
            FILE_DE_COMMANDES.size(),
            COMMANDES_EN_PREP.size()
        ));
        printLineClients();
    }

    // DONE : afficherStatistiques()
    private void afficherStatistiques() {
        logger.logLine(Constantes.HEADER_AFFICHER_STATS);
        logger.logLine(STATS.toString());
    }

    // DONE : Client ajouterClient(int id, String nom, int patienceInitiale)
    private void ajouterClient(int id, String nom, int patienceInitiale) {
        Client client = creerClient(nom, patienceInitiale);
        client.setId(id); clients.put(id, client);
        STATS.incrementerTotalClients();
        logger.logLine(Formatter.eventArriveeClient(HORLOGE.getTempsSimule(), client));
    }

    // DONE : Commande passerCommande(int idClient, MenuPlat codePlat)
    private Commande passerCommande(int idClient, MenuPlat codePlat) {
        Client client = clients.get(idClient);
        Commande commande = creerCommandePourClient(client);
        commande.ajouterPlat(idClient, codePlat);

        logger.logLine(Formatter.eventCommandeCree(
            HORLOGE.getTempsSimule(),
            commande.getId(),
            client,
            codePlat
        ));

        return commande;
    }

    // DONE : retirerProchaineCommande(): Commande
    public Commande retirerProchaineComande() {
        return FILE_DE_COMMANDES.poll();
    }

    // DONE : marquerCommandeTerminee(Commande commande)
    private void marquerCommandeTerminee(Commande commande) {
        commande.setEtat(EtatCommande.PRETE);
        COMMANDES_EN_PREP.remove(commande);

        Client client = commande.getClient();
        client.setEtat(EtatClient.SERVI);

        STATS.incrementerNbServis();
        STATS.incrementerChiffreAffaires(commande.getMontant());

        for (Plat plat : commande.getPlats().values()) {
            STATS.incrementerVentesParPlat(plat.getCode());
        }

        logger.logLine(Formatter.eventCommandeTerminee(
            HORLOGE.getTempsSimule(),
            commande.getId(),
            client
        ));
    }

    // DONE : Client creerClient(String nom, int patienceInitiale)
    private Client creerClient(String nom, int patienceInitiale) {
        return new Client(-1 , nom, patienceInitiale);
    }

    // DONE : Commande creerCommandePourClient(Client client)
    private Commande creerCommandePourClient(Client client) {
        Commande commande = new Commande(client, null);
        client.setCommande(commande);
        return commande;
    }

    // TODO : implémenter d'autres sous-méthodes qui seront appelées par les méthodes principales
    //  pour améliorer la lisibilité des méthodes en les découpant au besoin (éviter les trés longues méthodes)
    //  exemple : on peut avoir une méthode diminuerPatienceClients()
    //  qui permet de diminuer la patience des clients (appelée par tick())

    private void diminuerPatienceClients(int minutes) {
        for (Client client : clients.values()) {
            if (client.getEtat() == EtatClient.EN_ATTENTE) client.diminuerPatience(minutes);
            if (client.getEtat() == EtatClient.PARTI_FACHE) {
                STATS.incrementerNbFaches();
                logger.logLine(Formatter.eventClientFache(
                    HORLOGE.getTempsSimule(),
                    client
                ));
            }
        }
    }

    public void ajouterCommandeEnPreparation(Commande commande) {
        COMMANDES_EN_PREP.add(commande);
        logger.logLine(Formatter.eventCommandeDebut(
            HORLOGE.getTempsSimule(),
            commande.getId(),
            commande.getTempsRestant()
        ));
    }

    private void progresserCommandesEnPreparation(int minutes) {
        List<Commande> commandesTerminees = new ArrayList<>();

        for (Commande commande : COMMANDES_EN_PREP) {
            commande.decrementerTempsRestant(minutes);
            if (commande.estTermineeParTemps()) {
                commandesTerminees.add(commande);
            }
        }

        for (Commande commande : commandesTerminees) {
            marquerCommandeTerminee(commande);
        }
    }

    private void printLineClients() {
        for (Client client : clients.values()) {
            String emojiPlat = "";
            for (Plat plat : client.getCommande().getPlats().values()) {
                emojiPlat += Formatter.emojiPlat(plat.getCode());
            }
            logger.logLine(Formatter.clientLine(client, emojiPlat));
        }
    }
}
