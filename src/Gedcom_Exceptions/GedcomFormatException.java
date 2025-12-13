package Gedcom_Exceptions;

public class GedcomFormatException extends GedcomNatException {

    public GedcomFormatException(String message) {
        super("Erreur de format, format pas compatible." + message);
    }
    public GedcomFormatException( String message, Throwable cause) {
        super("Erreur de format, format pas compatible." + message, cause);
    }
}
