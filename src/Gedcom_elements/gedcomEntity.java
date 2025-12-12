package Gedcom_elements;

import java.io.Serializable;

public abstract class gedcomEntity implements Serializable {
    protected String ID;        //id de l'entité
    protected String tag;       //famille ou individu ou autre tag
    protected int level;        //Niveau dans le fichier gedcom
    protected String value ;    //Information du noeud


    public gedcomEntity(int level,String tag, String value,String ID ) {
        this.ID = ID;
        this.tag = tag;
        this.level = level;
        this.value = value;
    }

    public String getID() {return this.ID;}
    public String getTag() {return this.tag;}
    public String getValue() {return this.value;}
    public int getLevel() {return this.level;}

    public abstract String toString();
}
