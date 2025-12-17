package mv.sdd.sim.thread;

import mv.sdd.model.Commande;
import mv.sdd.sim.Restaurant;

import java.util.ArrayDeque;

public class Cuisinier implements Runnable{
    private Commande commande;

    public void getCommande(Commande commande) {
        this.commande = commande;
    }

    @Override
    public void run() {

    }
}
