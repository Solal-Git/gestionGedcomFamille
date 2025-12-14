package Gedcom_Parsing;

import Gedcom_elements.GedcomGraph;
import java.io.*;

/**
 * Classe Pour la sérialisation (save et load)
 */
public class GedcomSerializer implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * Méthode pour save le graph
     * <p>
     *     prend en parametre un graph et un nom de fichier, permet de save le fichier avec le nom dans le dossier save
     * </p>
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
     * Méthode pour load un graph
     * <p>
     *     Prend en parametre un nom de fichier, le fichier sera chercher dans le dossier save
     * </p>
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