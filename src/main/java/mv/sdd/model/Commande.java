package mv.sdd.model;

import mv.sdd.utils.Constantes;

import java.util.HashMap;

public class Commande {
    private int id;
    private static int nbCmd = 0;
    private final Client client;
    private EtatCommande etat = EtatCommande.EN_ATTENTE;
    private int tempsRestant = 0; // en minutes simulées
    // TODO : ajouter l'attribut plats et son getter avec le bon type et le choix de la SdD adéquat
    // private final <Votre structure de choix adéquat> plats
    private final HashMap<Integer, Plat> plats = new HashMap<>();
    private double montant = 0;

    // TODO : Ajout du ou des constructeur(s) nécessaires ou compléter au besoin
    public Commande(Client client, MenuPlat plat) {
        id = ++nbCmd;
        this.client = client;
        // À compléter
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

    public HashMap<Integer, Plat> getPlats() {return plats;}

    // setters
    public void setEtat(EtatCommande etat) {
        this.etat = etat;
    }

    // TODO : Ajoutez la méthode ajouterPlat
    public void ajouterPlat(int id, MenuPlat mp) {
        Plat p = Constantes.MENU.get(mp);
        plats.put(id, p);
        calculerTempsPreparationTotal(p);
        calculerMontant(p);
    };

    // TODO : Ajoutez la méthode demarrerPreparation
    public void demarrerPreparation() {
        setEtat(EtatCommande.EN_PREPARATION);
    }

    // TODO : Ajoutez la méthode decrementerTempsRestant
    public void decrementerTempsRestant() {}

    // TODO : Ajoutez la méthode estTermineeParTemps
    public boolean estTermineeParTemps() {
        return false;
    }

    // TODO : Ajoutez la méthode calculerTempsPreparationTotal
    public void calculerTempsPreparationTotal(Plat plat) {
        this.tempsRestant += plat.getTempsPreparation();
    }

    // TODO : Ajoutez la méthode calculerMontant
    public void calculerMontant(Plat plat) {
        this.montant += plat.getPrix();
    }
}
