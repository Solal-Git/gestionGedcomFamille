package Gedcom_Exceptions;

import java.util.ArrayList;

//Erreur de cycle : un fils est son ancêtre
public class GenealogyException extends LogicException {
    private final ArrayList<String> cycle;

    public GenealogyException(String message, ArrayList<String> cycle) {
        this.cycle = cycle;
        super("GenealogyErr : " + message);
    }

    public ArrayList<String> getCycle() {
        return cycle;
    }
}
