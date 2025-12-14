package Gedcom_elements;

import java.io.Serializable;

/**
 * Classe abstraite pour chaque entité
 */
public abstract class GedcomEntity implements Serializable {
    protected String ID;        //id de l'entité
    protected String tag;       //famille ou individu ou autre tag
    protected int level;        //Niveau dans le fichier gedcom
    protected String value;    //Information du noeud
    private static final long serialVersionUID = 1L;

    public GedcomEntity(int level,String tag, String value,String ID ) {
        this.ID = ID;
        this.tag = tag;
        this.level = level;
        this.value = value;
    }
//Getter

    public String getID() {
        return this.ID;
    }

    public String getTag() {
        return this.tag;
    }

    public String getValue() {
        return this.value;
    }

    public int getLevel() {
        return this.level;
    }

//toString Abstraite

    /**
     * Méthodes abstraite pour afficher les informations sois d'une famille, sois d'un individu ou alors tout le graphe.
     * @return
     */
    public abstract String toString();
}
