package Gedcom_Tag;

import Gedcom_elements.*;

/**
 * Gestion des supports multimédia (Pas vérifié si la fonctionnalité marche)
 */
public class TagMultimedia extends GedcomTag {

    private String format; // Tag FORM
    private String title;  // Tag TITL
    private String file;

    public TagMultimedia() {
        super(1, "OBJE", null);
    }

    public void setFormat(String format) {
        this.format = format;
    }
    public void setTitre(String titre) {
        this.title = titre;
    }
    public void setFichier(String fichier) {
        this.file = fichier;
    }

    /**
     * Réecriture de la fonction to String pour afficher le contenu multimédia.
     * @return
     */
    @Override
    public String toString() {
        return "Média: " + (title != null ? title : "Sans titre") + " [" + (file != null ? file : "?") + "]";
        }
}
