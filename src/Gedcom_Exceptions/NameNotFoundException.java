package Gedcom_Exceptions;

//SI LE NOM D'UNE REQUÊTE EST INTROUVABLE DANS LE GRAPHE
public class NameNotFoundException extends GedcomNatException {
    public NameNotFoundException(String message) {
        super("NameNotFoundErr : " + message);
    }
}
