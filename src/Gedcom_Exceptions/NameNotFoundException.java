package Gedcom_Exceptions;

public class NameNotFoundException extends GedcomNatException {
    public NameNotFoundException(String nomRecherche) {
        super("NameNotFoundError : L'individu avec le nom '" + nomRecherche + "' est introuvable dans le graphe.");
    }
}
