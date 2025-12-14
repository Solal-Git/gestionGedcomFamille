package Gedcom_Tag;
import Gedcom_elements.*;

/**
 * classe pour la gestion des tagDates (donc des dates
 */
public class TagDate extends GedcomTag {

    public TagDate(String value) {      //tag DATE
        super(2, "DATE",value);

    }

    /**
     * récriture de la méthode abstraite
     * @return
     */
    @Override
    public String toString() {      //avoir la date du mariage
        return value;
    }
}
