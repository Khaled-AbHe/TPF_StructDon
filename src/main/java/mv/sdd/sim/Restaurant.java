package mv.sdd.sim;

import mv.sdd.io.Action;
import mv.sdd.model.*;
import mv.sdd.sim.thread.Cuisinier;
import mv.sdd.utils.Logger;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Restaurant {
    private final Logger logger;
    // TODO : Ajouter les attributs nécessaires ainsi que les getters et les setters
    private final HashMap<Integer, Client> clients;
    private final ArrayDeque<Commande> fileDeCommandes = new ArrayDeque<>();
    private final List<Thread> cuisiniers = new ArrayList<>();
    public static final Object tempsVerrou = new Object();
    private int temps;

    // getters
    public int getTemps() {
        return temps;
    }

    public ArrayDeque<Commande> getFileDeCommandes() {
        return fileDeCommandes;
    }

    // setters
    public void setTemps(int temps) {
        this.temps = temps;
    }

    // TODO : Ajouter le(s) constructeur(s)
    public Restaurant(Logger logger, HashMap<Integer, Client> clients) {
        this.logger = logger;
        this.clients = clients;
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
                synchronized (fileDeCommandes) {
                    fileDeCommandes.add(passerCommande(action.getParam1(), MenuPlat.valueOf(action.getParam3())));
                    fileDeCommandes.notify();
                }
                break;
            case AVANCER_TEMPS:
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
        setTemps(dureeMax);
        for (int i = 0; i < nbCuisiniers; i++) {
            Thread t = new Thread(new Cuisinier(this));
            cuisiniers.add(t);
            t.start();
        }
    }

    public void avancerTemps(int minutes) {
        // Votre code ici.
        tick(minutes);
    }

    public void arreterService(){
        // Votre code ici.
        cuisiniers.forEach(Thread::interrupt);
    }

    // TODO : Déclarer et implémenter les méthodes suivantes
    // tick() | This is the method that manipulates the time in minutes
    public void tick(int minutes) {
        // temps
        synchronized (tempsVerrou) {
            setTemps(getTemps() - minutes);
            tempsVerrou.notifyAll();
        }
        // clients
        diminuerPatienceClients(minutes);
        // TODO : updateStats
    }

    // afficherEtat()
    public void afficherEtat() {}

    // afficherStatistiques()
    public void afficherStatistiques() {}

    // DONE : Client ajouterClient(int id, String nom, int patienceInitiale)
    public void ajouterClient(int id, String nom, int patienceInitiale) {
        Client client = creerClient(nom, patienceInitiale);
        client.setId(id);
        clients.put(id, client);
    }

    // DONE : Commande passerCommande(int idClient, MenuPlat codePlat)
    public Commande passerCommande(int idClient, MenuPlat codePlat) {
        Commande commande = creerCommandePourClient(clients.get(idClient));
        commande.ajouterPlat(idClient, codePlat);
        return commande;
    }

    // DONE : retirerProchaineCommande(): Commande
    public Commande retirerProchaineComande() {
        return fileDeCommandes.poll();
    }

    // DONE : marquerCommandeTerminee(Commande commande)
    public void marquerCommandeTerminee(Commande commande) {
        commande.setEtat(EtatCommande.PRETE);
    }

    // DONE : Client creerClient(String nom, int patienceInitiale)
    public Client creerClient(String nom, int patienceInitiale) {
        return new Client(-1 , nom, patienceInitiale);
    }

    // Commande creerCommandePourClient(Client client)
    // DONE
    public Commande creerCommandePourClient(Client client) {
        Commande commande = new Commande(client, null);
        client.setCommande(commande);
        return commande;
    }

    // TODO : implémenter d'autres sous-méthodes qui seront appelées par les méthodes principales
    //  pour améliorer la lisibilité des méthodes en les découpant au besoin (éviter les trés longues méthodes)
    //  exemple : on peut avoir une méthode diminuerPatienceClients()
    //  qui permet de diminuer la patience des clients (appelée par tick())

    public void diminuerPatienceClients(int minutes) {
        for (Client client : clients.values()) {
            client.diminuerPatience(minutes);
        }
    }
}
