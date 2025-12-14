package Gedcom_Exceptions;
import java.io.Serializable;


//Erreur Mère
public class GedcomNatException extends Exception {

    private static final long serialVersionUID = 1L;        //Force la version du fichier utilise pour la sérialisation

    public GedcomNatException(String message) {
        super("GedcomNatErr-" + message);
    }

}
