package mv.sdd.sim.thread;

import mv.sdd.model.Commande;
import mv.sdd.sim.Restaurant;

public class Cuisinier implements Runnable{
    private Commande commande;
    private final Restaurant RESTAURANT;

    public Cuisinier(Restaurant restaurant) {
        this.RESTAURANT = restaurant;
    }

    private void getCommandeFromFile() throws InterruptedException {
        synchronized (RESTAURANT.getFILE_DE_COMMANDES()) {
            while (RESTAURANT.getFILE_DE_COMMANDES().isEmpty()) {
                RESTAURANT.getFILE_DE_COMMANDES().wait();
            }
            this.commande = RESTAURANT.retirerProchaineComande();
            this.commande.demarrerPreparation();
            RESTAURANT.ajouterCommandeEnPreparation(this.commande);
        }
    }

    private void cuisiner() throws InterruptedException {
        synchronized (RESTAURANT.getTempsVerrou()) {
            int oldTime = RESTAURANT.getTemps();
            while (RESTAURANT.getTemps() == oldTime) {
                RESTAURANT.getTempsVerrou().wait();
            }
            commande.decrementerTempsRestant(oldTime - RESTAURANT.getTemps());
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                getCommandeFromFile();
                cuisiner();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
