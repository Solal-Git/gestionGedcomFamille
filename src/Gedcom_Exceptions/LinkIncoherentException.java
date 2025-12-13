package Gedcom_Exceptions;
import java.io.Serializable;

public class LinkIncoherentException extends GedcomNatException {
    private final String linkIdfrom;                  //Lien de A vers B existant
    private final String linkIdMissing;               //Lien de B vers A manquant

    public LinkIncoherentException(String message, String linkIdfrom, String linkIdmissing) {
        super(message);
        this.linkIdfrom = linkIdfrom;
        this.linkIdMissing = linkIdmissing;
    }

    public String getLinkIdFrom() {
        return linkIdfrom;
    }
    public String getLinkIdMissing() {
        return linkIdMissing;
    }
}
