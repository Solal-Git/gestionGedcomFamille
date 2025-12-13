package Gedcom_Parsing;

import Gedcom_elements.GedcomGraph;
import java.io.*;

public class GedcomSerializer {

    /**
     * Sauvegarde le graphe entier dans un fichier binaire (.ser)
     */
    public static void sauvegarder(GedcomGraph graph, String nomFichier) throws IOException {
        // On ajoute l'extension .ser si l'utilisateur l'a oubliée
        if (!nomFichier.endsWith(".ser")) {
            nomFichier += ".ser";
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomFichier))) {
            oos.writeObject(graph);
            System.out.println(">> Sauvegarde réussie dans : " + nomFichier);
        }
    }

    /**
     * Charge un graphe depuis un fichier binaire
     */
    public static GedcomGraph charger(String nomFichier) throws IOException, ClassNotFoundException {
        if (!nomFichier.endsWith(".ser")) {
            nomFichier += ".ser";
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nomFichier))) {
            GedcomGraph graph = (GedcomGraph) ois.readObject();
            System.out.println(">> Chargement réussi depuis : " + nomFichier);
            return graph;
        }
    }
}