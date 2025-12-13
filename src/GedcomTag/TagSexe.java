package GedcomTag;
import Gedcom_elements.*;
import Gedcom_elements.Individu;

public class TagSexe extends GedcomTag {

    public TagSexe(String value) {
        super(1,"SEXE",value);
    }

    @Override
    public void attribuateIndividu(GedcomEntity E) {
        if (E instanceof Individu I) {
            I.setSex(this);
        }
    }

    @Override
    public String toString() {
        if ("M".equals(value)) {
            return "C'est un homme";
        }
        else if ("F".equals(value)) {
            return "C'est une femme";
        }
        return "Le sexe est inconnu";
    }
}
