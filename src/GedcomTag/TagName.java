package GedcomTag;
import Gedcom_elements.*;
import Gedcom_elements.Individu;

public class TagName extends GedcomTag {

    public TagName(String value) {
        super(1, "NAME", value);
    }

    @Override
    public void attributionIndividu(GedcomEntity E) {
        if (E instanceof Individu I) {
            I.setName(this);
        }
    }

    @Override
    public String toString() {
        return value.replace("/", "");
    }
}

