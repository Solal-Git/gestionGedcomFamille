package Gedcom_Parsing;

import Gedcom_elements.GedcomGraph;
import java.io.*;

/**
 * Classe pour le save les documents
 */
public class GedcomSerializer {
    /**
     * Méthode pour save les documents .ser dans Save/
     * @param graph
     * @param filesName
     * @throws IOException
     */
    public static void save(GedcomGraph graph, String filesName) throws IOException {
        if (!filesName.endsWith(".ser")) {
            filesName += ".ser";
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Save/"+ filesName))) {
            oos.writeObject(graph);
            System.out.println(">> Sauvegarde réussie dans : " + filesName);
        }
    }

    /**
     * Méthode pour load des fichiers .ser de Save/
     * @param filesName
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */

    public static GedcomGraph load(String filesName) throws IOException, ClassNotFoundException {
        if (!filesName.endsWith(".ser")) {
            filesName += ".ser";
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Save/" + filesName))) {
            GedcomGraph graph = (GedcomGraph) ois.readObject();
            System.out.println(">> Chargement réussi depuis : " + "Save/" + filesName);
            return graph;
        }
    }
}