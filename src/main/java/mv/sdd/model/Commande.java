package mv.sdd.model;

import mv.sdd.utils.Constantes;

import java.util.HashMap;

public class Commande {
    private int id;
    private static int nbCmd = 0;
    private final Client client;
    private EtatCommande etat = EtatCommande.EN_ATTENTE;
    private int tempsRestant; // en minutes simulées
    // TODO : ajouter l'attribut plats et son getter avec le bon type et le choix de la SdD adéquat
    // private final <Votre structure de choix adéquat> plats
    private final HashMap<Integer, Plat> plats = new HashMap<>();

    // TODO : Ajout du ou des constructeur(s) nécessaires ou compléter au besoin
    public Commande(Client client, MenuPlat plat) {
        id = ++nbCmd;
        this.client = client;
        // À compléter
        this.ajouterPlat(id, plat);
    }

    // getters
    public int getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public EtatCommande getEtat() {
        return etat;
    }

    public int getTempsRestant() {
        return tempsRestant;
    }

    public HashMap<Integer, Plat> getPlats() {return plats;}

    // setters
    public void setEtat(EtatCommande etat) {
        this.etat = etat;
    }

    // TODO : Ajoutez la méthode ajouterPlat
    private void ajouterPlat(int id, MenuPlat plat) {
        plats.put(id, Constantes.MENU.get(plat));
    };

    // TODO : Ajoutez la méthode demarrerPreparation


    // TODO : Ajoutez la méthode decrementerTempsRestant


    // TODO : Ajoutez la méthode estTermineeParTemps


    // TODO : Ajoutez la méthode calculerTempsPreparationTotal
    public int calculerTempsPreparationTotal() {
        int total = 0;
        for (Plat plat : getPlats().values()) {
            total += plat.getTempsPreparation();
        }
        return total;
    }

    // TODO : Ajoutez la méthode calculerMontant
    public double calculerMontant() {
        double montant = 0.0;
        for (Plat plat : getPlats().values()) {
            montant += plat.getPrix();
        }
        return montant;
    }
}
