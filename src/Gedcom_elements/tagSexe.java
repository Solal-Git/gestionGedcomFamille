package Gedcom_elements;

public class tagSexe extends gedcomEntity{

    public tagSexe(String value) {
        super(1,"SEXE",value,null);
    }

    @Override
    public String toString() {
        if (value == "M") {return "il s'agit de Monsieur" + value ;}
        else {return "il s'agit de Madame" + value; }
    }
}
