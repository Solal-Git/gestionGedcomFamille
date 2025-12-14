package Gedcom_Exceptions;


/**
 * Exception : UN ENFANT EST L'ENFANT D'UNE SEULE FAMILLE
 */
public class IsAlreadyChildException extends StructureException {

    /**
     * Constructeur de l'exception
     * @param message
     */
    public IsAlreadyChildException(String message) {
        super("IsAlreadyChildErr : " + message);
    }
}
