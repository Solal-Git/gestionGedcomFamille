package Gedcom_Exceptions;
import java.io.Serializable;


public class RefMissingException extends GedcomNatException {
    private String idManquant;

    public RefMissingException(String idManquant) {
        super("RefMissingError : Référence manquante détectée vers l'ID : " + idManquant);
        this.idManquant = idManquant;
    }

    public String getIdManquant() {
        return idManquant;
    }
}
