package Gedcom_elements;

public class TagName extends GedcomEntity {

    public TagName(String value) {
        super(1,"NAME",value,null);
    }

    public String getNomPropre(){
        String tempValue = value;
        if (tempValue != null){
            tempValue.replace("/", "");
            return tempValue;
        }
        return tempValue;
    }

    @Override
    public String toString(){
        return getNomPropre();
    }

}
