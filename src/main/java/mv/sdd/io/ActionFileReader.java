package mv.sdd.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Lecture du fichier d'actions
public class ActionFileReader {
    public static List<Action> readActions(String filePath) throws IOException {
        List<Action> actions = new ArrayList<>();
        // TODO : Ajouter le code qui permet de lire et parser un fichier d'actions
        // Done
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = br.readLine()) != null) {
            actions.add(ActionParser.parseLigne(line));
        }
        return actions;
    }
}
