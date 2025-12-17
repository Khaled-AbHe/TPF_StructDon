package mv.sdd.sim.thread;

import mv.sdd.model.Commande;

import java.util.ArrayDeque;

public class Cuisinier implements Runnable{
    private Commande commande;

    public void getCommande(Commande commande) {
        this.commande = commande;
    }

    @Override
    public void run() {
        System.out.println(commande.toString());
    }
}
