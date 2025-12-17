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

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                setCommande();
                commande.demarrerPreparation();
//                System.out.println(commande.getPlats().get(1).getNom());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
