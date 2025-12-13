package Gedcom_Exceptions;

import java.util.ArrayList;

public class GenealogyException extends GedcomNatException {
    private final ArrayList<String> cycle;

    public GenealogyException(ArrayList<String> cycle, String message) {
        this.cycle = cycle;
        super("Erreur généalogique, Il existe un cycle dans l'arbre" + message);
    }

    public ArrayList<String> getCycle() {
        return cycle;
    }
}
