package Gedcom_Exceptions;

public class LinkIncoherentException extends GedcomNatException {
    private final String LINKIDFROM;                  //Lien de A vers B existant
    private final String LINKIDMISSING;               //Lien de B vers A manquant

    public LinkIncoherentException(String message, String linkIdfrom, String linkIdmissing) {
        super("LinkIncoherentError :" + message);
        this.LINKIDFROM = linkIdfrom;
        this.LINKIDMISSING = linkIdmissing;
    }

    public String getLinkIdFrom() {
        return LINKIDFROM;
    }
    public String getLINKIDMISSING() {
        return LINKIDMISSING;
    }
}
