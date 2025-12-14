package Gedcom_Exceptions;

import java.util.ArrayList;

/**
 * Exception de cycle : un fils est son ancêtre
 */
public class GenealogyException extends LogicException {
    private final ArrayList<String> cycle;

    /**
     * Constructeur de cette exception
     * @param message
     * @param cycle
     */
    public GenealogyException(String message, ArrayList<String> cycle) {
        this.cycle = cycle;
        super("GenealogyErr : " + message);
    }

}
