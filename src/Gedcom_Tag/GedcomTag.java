package Gedcom_Tag;

import Gedcom_elements.GedcomEntity;

import java.io.Serializable;
//Classe mère tag
//Un tag est une entité mais spécifique différente d'un individu ou d'une famille

/**
 * Classe mere pour les Tags
 */

public abstract class GedcomTag extends GedcomEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public GedcomTag(int level, String tag, String value) {
        super(level, tag, value, null);
    }

    /**
     * calsse abstraite pour avoir les informations des tag en string.
     * @return
     */
    @Override
    public abstract String toString();      //méthode abstraite toString
}
