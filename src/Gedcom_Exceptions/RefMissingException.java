package Gedcom_Exceptions;
import java.io.Serializable;


public class RefMissing extends GedcomNatException {
    private String idManquant;

    public RefMissing(String idManquant) {
        super("Référence manquante détectée vers l'ID : " + idManquant);
        this.idManquant = idManquant;
    }

    public String getIdManquant() {
        return idManquant;
    }
}
