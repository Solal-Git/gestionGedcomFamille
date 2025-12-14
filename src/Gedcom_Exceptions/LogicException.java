package Gedcom_Exceptions;

//ERREUR DE LOGIQUE : PERE AU LIEU DE MERE....
public class LogicException extends GedcomNatException {
    public LogicException(String message) {
        super("LogicErr-" + message);
    }
}
