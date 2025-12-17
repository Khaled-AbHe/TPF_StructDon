package mv.sdd.model;

import java.util.Objects;

public class Client {
    private int id;
    private final String nom;
    private int patience;
    private EtatClient etat;
    private Commande commande;

    public Client(int id, String nom, int patienceInitiale) {
        this.id = id;
        this.nom = nom;
        this.patience = patienceInitiale;
        this.etat = EtatClient.EN_ATTENTE;
    }

    // getters
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public int getPatience() {
        return patience;
    }

    public EtatClient getEtat() {
        return etat;
    }

    public Commande getCommande() {
        return commande;
    }

    // setters

    public void setId(int id) {
        this.id = id;
    }

    public void setEtat(EtatClient etat) {
        this.etat = etat;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    // methods

    public void diminuerPatience(int minutes) {
        // TODO: diminuer patience et passer à PARTI_FACHE si <= 0
        this.patience -= minutes;
        if (patience <= 0) {
            setEtat(EtatClient.PARTI_FACHE);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id == client.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
