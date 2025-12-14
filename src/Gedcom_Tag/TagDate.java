package Gedcom_Tag;
import Gedcom_elements.*;

public class TagDate extends GedcomTag {

    public TagDate(String value) {      //tag DATE
        super(2, "DATE",value);

    }

    @Override
    public String toString() {      //avoir la date du mariage
        return value;
    }
}
