package Gedcom_Tag;

import Gedcom_elements.*;

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

    @Override
    public void attribuateIndividu(GedcomEntity entity) {
        if (entity instanceof Individu) {
            ((Individu) entity).setMultimedia(this);
        } else if (entity instanceof Famille) {
            ((Famille) entity).setMultimedia(this);
        }
    }

    @Override
    public String toString() {
        return "Média: " + (title != null ? title : "Sans titre") + " [" + (file != null ? file : "?") + "]";
        }
}
