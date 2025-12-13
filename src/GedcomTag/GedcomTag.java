package GedcomTag;

import Gedcom_elements.GedcomEntity;

import java.io.Serializable;

//Un tag est une entité mais spécifique différente d'un individu ou d'une famille
public abstract class GedcomTag extends GedcomEntity implements Serializable {

    public GedcomTag(int level, String tag, String value) {
        super(level, tag, value, null);
    }

    public abstract void attributionIndividu(GedcomEntity E);

    @Override
    public abstract String toString();
}
