package Gedcom_Tag;
import Gedcom_elements.*;

public class TagName extends GedcomTag {

    public TagName(String value) {                  // Tag NAME
        super(1, "NAME", value);
    }

    @Override
    public void attribuateIndividu(GedcomEntity E) {        //attribuer le nom d'un Individu
        if (E instanceof Individu I) {
            I.setName(this);
        }
    }

    @Override
    public String toString() {
        return value.replace("/", "");
    }   //avoir le name en string
}

