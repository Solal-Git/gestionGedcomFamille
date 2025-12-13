package GedcomTag;

import Gedcom_elements.*;

public class TagMultimedia extends GedcomTag {

    private String format; // Tag FORM
    private String titre;  // Tag TITL
    private String fichier;

    public TagMultimedia() {
        super(1, "OBJE", null);
    }

    public void setFormat(String format) {
        this.format = format;
    }
    public void setTitre(String titre) {
        this.titre = titre;
    }
    public void setFichier(String fichier) {
        this.fichier = fichier;
    }

    @Override
    public void attributionIndividu(GedcomEntity entity) {
        if (entity instanceof Individu) {
            ((Individu) entity).setMultimedia(this);
        } else if (entity instanceof Famille) {
            ((Famille) entity).setMultimedia(this);
        }
    }

    @Override
    public String toString() {
        return "Média: " + (titre != null ? titre : "Sans titre") + " [" + (fichier != null ? fichier : "?") + "]";
        }
}
