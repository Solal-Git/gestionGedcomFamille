package Gedcom_Tag;
import Gedcom_elements.*;
/**
 * classe pour la gestion des tagName (donc des nom)
 */
public class TagName extends GedcomTag {

    public TagName(String value) {                  // Tag NAME
        super(1, "NAME", value);
    }
/**
 * réecriture de la méthode abstraite
 */
    @Override
    public String toString() {
        return value.replace("/", "");
    }   //avoir le name en string
}

