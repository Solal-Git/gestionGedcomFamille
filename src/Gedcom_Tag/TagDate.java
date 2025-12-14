package Gedcom_Tag;
import Gedcom_elements.*;

/**
 * gestion des tagDate
 */
public class TagDate extends GedcomEntity {

    public TagDate(String value) {      //tag DATE
        super(2, "DATE",value, null);

    }
    /**
     * Réecriture de la fonction to String pour afficher le date de mariage ou naissance .
     * @return
     */
    @Override
    public String toString() {      //avoir la date du mariage ou de naissance
        return value;
    }
}
