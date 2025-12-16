package mv.sdd.sim;

import mv.sdd.io.Action;
import mv.sdd.model.Client;
import mv.sdd.model.Commande;
import mv.sdd.model.MenuPlat;
import mv.sdd.utils.Logger;

import java.util.ArrayDeque;
import java.util.HashMap;

public class Restaurant {
    private final Logger logger;
    // TODO : Ajouter les attributs nécessaires ainsi que les getters et les setters
    private final HashMap<Integer, Client> clients;
    private ArrayDeque<Commande> fileDeCommandes;

    // TODO : Ajouter le(s) constructeur(s)
    public Restaurant(Logger logger, HashMap<Integer, Client> clients) {
        this.logger = logger;
        this.clients = clients;
    }

    // TODO : implémenter les méthodes suivantes
    // Méthode appelée depuis App pour chaque action
    public void executerAction(Action action) {
        // Votre code ici.

    }

    public void demarrerService(int dureeMax, int nbCuisiniers) {
        // Votre code ici.
    }

    public void avancerTemps(int minutes) {
        // Votre code ici.
    }

    public void arreterService(){
        // Votre code ici.
    }

    // TODO : Déclarer et implémenter les méthodes suivantes
    // tick() | This is the method that manipulates the time in minutes
    public void tick() {}

    // afficherEtat()
    public void afficherEtat() {}

    // afficherStatistiques()
    public void afficherStatistiques() {}

    // Client ajouterClient(int id, String nom, int patienceInitiale)
    public void ajouterClient(int id, String nom, int patienceInitiale) {
        clients.put(id, creerClient(id, nom, patienceInitiale));
    }

    // Commande passerCommande(int idClient, MenuPlat codePlat)
    // DONE
    public Commande passerCommande(int idClient, MenuPlat codePlat) {
        return new Commande(clients.get(idClient), codePlat);
    }

    // retirerProchaineCommande(): Commande
    public Commande retirerProchaineComande() {return null;}

    // marquerCommandeTerminee(Commande commande)
    public void marquerCommandeTerminee(Commande commande) {}

    // Client creerClient(String nom, int patienceInitiale)
    public Client creerClient(int id, String nom, int patienceInitiale) {
        return new Client(id, nom, patienceInitiale);
    }

    // Commande creerCommandePourClient(Client client)
    public Commande creerCommandePourClient(Client client, MenuPlat codePlat) {
        if (client.getCommande() == null) {
            Commande commande = new Commande(client, codePlat);
            client.setCommande(commande);
        }
        return null;
    }

    // TODO : implémenter d'autres sous-méthodes qui seront appelées par les méthodes principales
    //  pour améliorer la lisibilité des méthodes en les découpant au besoin (éviter les trés longues méthodes)
    //  exemple : on peut avoir une méthode diminuerPatienceClients()
    //  qui permet de diminuer la patience des clients (appelée par tick())
}
