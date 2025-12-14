package Gedcom_Tag;
import Gedcom_elements.*;

/**
 * classe pour gérer le Sexe d'un individu
 */
public class TagSexe extends GedcomTag {
    /**
     * Constructeur de ce tag
     * @param value
     */
    public TagSexe(String value) {                          // Tag SEXE
        super(1,"SEXE",value);
    }

    /**
     * Réecriture de la fonction to String pour afficher le sexe.
     * @return
     */
    @Override
    public String toString() {          // redéfinition de toString pour avoir le sexe en string
        if ("M".equals(value)) {
            return "M";
        }
        else if ("F".equals(value)) {
            return "F";
        }
        return "Le sexe est inconnu";
    }
}