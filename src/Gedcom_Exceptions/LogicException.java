package Gedcom_Exceptions;

/**
 * Exception DE LOGIQUE
 */
public class LogicException extends GedcomNatException {

    /**
     * Constructeur de l'exception
     * @param message
     */
    public LogicException(String message) {
        super("LogicErr-" + message);
    }
}
