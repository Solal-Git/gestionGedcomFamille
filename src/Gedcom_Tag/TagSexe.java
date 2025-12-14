package Gedcom_Tag;
import Gedcom_elements.*;

public class TagSexe extends GedcomTag {

    public TagSexe(String value) {                          // Tag SEXE
        super(1,"SEXE",value);
    }

    @Override
    public void attribuateIndividu(GedcomEntity E) {        //attribuer le sexe d'un Individu
        if (E instanceof Individu I) {
            I.setSex(this);
        }
    }

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
