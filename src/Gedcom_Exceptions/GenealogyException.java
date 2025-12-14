package Gedcom_Exceptions;

import java.util.ArrayList;

public class GenealogyException extends GedcomNatException {
    private final ArrayList<String> CYCLE;

    public GenealogyException(ArrayList<String> cycle, String message) {
        this.CYCLE = cycle;
        super("GenealogyError : Erreur généalogique, Il existe un CYCLE dans l'arbre" + message);
    }

    public ArrayList<String> getCYCLE() {
        return CYCLE;
    }
}
