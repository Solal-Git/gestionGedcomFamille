package Gedcom_Exceptions;
import java.io.Serializable;

public class TwiceChildException extends GedcomNatException {

    public TwiceChildException(String message) {
        super("Erreur deux fois un enfant : " + message) ;
    }

    public TwiceChildException(String message, Throwable cause) {
        super("Erreur deux fois un enfant : " + message, cause);
    }
}
