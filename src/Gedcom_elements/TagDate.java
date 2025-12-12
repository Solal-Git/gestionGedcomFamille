package Gedcom_elements;

public class TagDate extends GedcomEntity {

    public TagDate(String value) {
        super(2,"DATE",value,null);
    }

    @Override
    public String toString() {
        return "Née le : " + value;
    }
}
