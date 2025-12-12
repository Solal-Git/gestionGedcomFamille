package Gedcom_elements;

public class tagDate extends gedcomEntity {

    public tagDate(String value) {
        super(2,"DATE",value,null);
    }

    @Override
    public String toString() {
        return "Née le : " + value;
    }
}
