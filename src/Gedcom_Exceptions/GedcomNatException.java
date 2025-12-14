package Gedcom_Exceptions;
import java.io.Serializable;


/**
 * Exception Mère
 */

public class GedcomNatException extends Exception {

    private static final long serialVersionUID = 1L;        //Force la version du fichier utilise pour la sérialisation

    /**
     * Constructeur de l'exception mère
     * @param message
     */
    public GedcomNatException(String message) {
        super("GedcomNatErr-" + message);
    }

}
