package mv.sdd.model;

import mv.sdd.utils.Constantes;
import mv.sdd.utils.Formatter;

import java.util.HashMap;
import java.util.Map;

public class Stats {
    private Horloge horloge;
    private int totalClients = 0;
    private int nbServis = 0;
    private int nbFaches = 0;
    private double chiffreAffaires = 0;
    // TODO : remplacer Object par le bon type et initilaliser l'attribut avec la bonne valeur
    //  et ajuster les getters et les setters
    private HashMap<MenuPlat, Integer> ventesParPlat;

    public int getTotalClients() {
        return totalClients;
    }

    public int getNbServis() {
        return nbServis;
    }

    public int getNbFaches() {
        return nbFaches;
    }

    // TODO: au besoin ajuster le constructeur et/ou ajouter d'autres
    public Stats(Horloge horloge) {
        this.horloge = horloge;
        // TODO : compléter le code manquant
        this.ventesParPlat = new HashMap<>();
        ventesParPlat.put(MenuPlat.PIZZA, 0);
        ventesParPlat.put(MenuPlat.BURGER, 0);
        ventesParPlat.put(MenuPlat.FRITES, 0);
    }

    public void incrementerTotalClients() {
        this.totalClients++;
    }

    public void incrementerNbServis() {
        this.nbServis++;
    }

    public void incrementerNbFaches() {
        this.nbFaches++;
    }

    public void incrementerChiffreAffaires(double montant) {
        this.chiffreAffaires += montant;
    }

    public static String statsPlatLine(String plat, int quantite) {
        return "\n" + "\t\t" + plat + " : " + quantite;
    }

    // DONE : ajouter incrementerVentesParPlat(MenuPlat codePlat) et autres méthodes au besoin
    public void incrementerVentesParPlat(MenuPlat codePlat) {
        ventesParPlat.put(codePlat, ventesParPlat.get(codePlat) + 1);
    }

    public String toString() {
        String chaine = String.format(
                Constantes.STATS_GENERAL,
                horloge.getTempsSimule(),
                totalClients,
                nbServis,
                nbFaches,
                chiffreAffaires
        );

        // TODO : ajouter le code pour concaténer avec statsPlatLines les lignes des quantités vendus par plat (à l'aide de ventesParPlat),
        //  sachant que la méthode statsPlatLine sert à formater une ligne et retourne une chaine

        for (Map.Entry<MenuPlat, Integer> entry : ventesParPlat.entrySet()) {
            String plat = String.format("%s %s", Formatter.emojiPlat(entry.getKey()), entry.getKey());
            chaine += statsPlatLine(plat, entry.getValue());
        }

        return chaine;
    }
}
