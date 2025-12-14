package Gedcom_Exceptions;

//AU NIVEAU DU GED : LIEN MANQUANT, ABSENCE
public class StructureException extends GedcomNatException{

    public StructureException(String message) {
        super("StructureErr-" + message);
    }
}
