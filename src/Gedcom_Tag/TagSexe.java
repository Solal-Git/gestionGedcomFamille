package Gedcom_Tag;
import Gedcom_elements.*;
/**
 * classe pour la gestion des tagSexe (donc du sexe)
 */
public class TagSexe extends GedcomTag {

    public TagSexe(String value) {                          // Tag SEXE
        super(1,"SEXE",value);
    }

    /**
     * réecriture de la fonction
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
