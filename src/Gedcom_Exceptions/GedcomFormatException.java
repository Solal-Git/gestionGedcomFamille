package Gedcom_Exceptions;

//Trop ambitieux nous n'avons pas réussi l'insertion efficacement a temps.

public class GedcomFormatException extends GedcomNatException {

    public GedcomFormatException(String message) {
        super("GedcomFormatError : Erreur de format, format pas compatible." + message);
    }
}
