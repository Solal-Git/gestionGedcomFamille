package Gedcom_Exceptions;
import java.io.Serializable;

public class GedcomNatException extends Exception {

    private static final long serialVersionUID = 1L;


    public GedcomNatException() {
        super();
    }

    public GedcomNatException(String message) {
        super("Gedcom Erreur :" + message);
    }

    public GedcomNatException(String message, Throwable cause) {
        super("Gedcom Erreur :" + message, cause);
    }

    public GedcomNatException(Throwable cause) {
        super("Gedcom Erreur :" + cause);
    }



}
