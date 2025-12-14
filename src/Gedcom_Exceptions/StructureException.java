package Gedcom_Exceptions;

/**
 * Exception AU NIVEAU DU GED : LIEN MANQUANT, ABSENCE
 */
public class StructureException extends GedcomNatException{

    /**
     * Constructeur de l'exception
     * @param message
     */
    public StructureException(String message) {
        super("StructureErr-" + message);
    }
}
