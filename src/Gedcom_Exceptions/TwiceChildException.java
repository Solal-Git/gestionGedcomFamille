package Gedcom_Exceptions;
import java.io.Serializable;

//SI L'ENFANT EST DEJA DANS LA FAMILLE
public class TwiceChildException extends GedcomNatException {

    public TwiceChildException(String message) {
        super("TwiceChildErr : " + message) ;
    }

}
