package Gedcom_Parsing;

import Gedcom_elements.GedcomGraph;
import java.io.*;

public class GedcomSerializer {

    /**
     * Sauvegarde le graphe entier dans un fichier binaire (.ser)
     */
    public static void save(GedcomGraph graph, String nomFichier) throws IOException {
        if (!nomFichier.endsWith(".ser")) {
            nomFichier += ".ser";
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Save/"+nomFichier))) {
            oos.writeObject(graph);
            System.out.println(">> Sauvegarde réussie dans : " + nomFichier);
        }
    }

    /**
     * Charge un graphe depuis un fichier binaire
     */
    public static GedcomGraph load(String nomFichier) throws IOException, ClassNotFoundException {
        if (!nomFichier.endsWith(".ser")) {
            nomFichier += ".ser";
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Save/" +nomFichier))) {
            GedcomGraph graph = (GedcomGraph) ois.readObject();
            System.out.println(">> Chargement réussi depuis : " + "Save/" + nomFichier);
            return graph;
        }
    }
}