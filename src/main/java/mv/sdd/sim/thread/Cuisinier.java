package mv.sdd.sim.thread;

import mv.sdd.model.Commande;
import mv.sdd.sim.Restaurant;

public class Cuisinier implements Runnable{
    private Commande commande;
    private final Restaurant RESTAURANT;

    public Cuisinier(Restaurant restaurant) {
        this.RESTAURANT = restaurant;
    }

    public void setCommande() throws InterruptedException {
        synchronized (RESTAURANT.getFileDeCommandes()) {
            while (RESTAURANT.getFileDeCommandes().isEmpty()) {
                RESTAURANT.getFileDeCommandes().wait();
            }
            this.commande = RESTAURANT.retirerProchaineComande();
        }
    }

    public void prepare() throws InterruptedException {
        synchronized (Restaurant.tempsVerrou) {
            int oldTime = RESTAURANT.getTemps();
            while (RESTAURANT.getTemps() == oldTime) {
                Restaurant.tempsVerrou.wait();
            }
            commande.decrementerTempsRestant(oldTime - RESTAURANT.getTemps());
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                setCommande();
                commande.demarrerPreparation();
                prepare();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
