package Gedcom_Exceptions;

/**
 * SI LE NOM D'UNE REQUÊTE EST INTROUVABLE DANS LE GRAPHE
 */
public class NameNotFoundException extends GedcomNatException {

    /**
     * Constructeur de l'exception
     * @param message
     */
    public NameNotFoundException(String message) {
        super("NameNotFoundErr : " + message);
    }
}
