package Gedcom_elements;

public class tagName extends gedcomEntity {

    public tagName(String value) {
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
