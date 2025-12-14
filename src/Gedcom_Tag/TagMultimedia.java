package Gedcom_Tag;

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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n  [MEDIA] ");
        if (titre != null){
            sb.append(titre).append(" ");
        }
        if (fichier != null){
            sb.append("(").append(fichier).append(")");
        }
        if (format != null){
            sb.append(format).append(" ");
        }
        return sb.toString();
    }
}
