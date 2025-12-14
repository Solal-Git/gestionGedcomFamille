package Gedcom_Parsing;

import Gedcom_elements.GedcomGraph;
import java.io.*;

public class GedcomSerializer {

    public static void save(GedcomGraph graph, String filesName) throws IOException {
        if (!filesName.endsWith(".ser")) {
            filesName += ".ser";
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Save/"+ filesName))) {
            oos.writeObject(graph);
            System.out.println(">> Sauvegarde réussie dans : " + filesName);
        }
    }

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