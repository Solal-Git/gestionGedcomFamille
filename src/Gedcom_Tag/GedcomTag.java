package Gedcom_Tag;

import Gedcom_elements.GedcomEntity;

import java.io.Serializable;
//Classe mére tag
//Un tag est une entité mais spécifique différente d'un individu ou d'une famille
public abstract class GedcomTag extends GedcomEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public GedcomTag(int level, String tag, String value) {
        super(level, tag, value, null);
    }

    public abstract void attribuateIndividu(GedcomEntity E);

    @Override
    public abstract String toString();      //méthode abstraite toString
}
