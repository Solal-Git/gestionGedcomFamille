package Gedcom_Exceptions;
import java.io.Serializable;

/**
 * Exception : SI L'ENFANT EST DEJA DANS LA FAMILLE
 */
public class TwiceChildException extends StructureException {

    /**
     * Constructeur de l'exception
     * @param message
     */
    public TwiceChildException(String message) {
        super("TwiceChildErr : " + message) ;
    }

}
