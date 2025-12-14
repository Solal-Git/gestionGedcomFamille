package Gedcom_Tag;
import Gedcom_elements.*;

/**
 * gestion des tagName (nom de l'individu)
 */
public class TagName extends GedcomTag {

    public TagName(String value) {                  // Tag NAME
        super(1, "NAME", value);
    }

    /**
     * Réecriture de la fonction to String pour afficher le nom sans les "/".
     * @return
     */
    @Override
    public String toString() {
        return value.replace("/", "");
    }   //avoir le name en string
}

