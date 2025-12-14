package Gedcom_Exceptions;
import Gedcom_elements.*;

/**
 * Exception : LIEN MANQUANT ENTRE UNE FAMILLE ET UN INDIVIDU
 */
public class LinkIncoherentException extends LogicException {
    private Individu i;
    private Famille f;

    /**
     * Constructeur de l'exception
     * @param message
     */
    public LinkIncoherentException(String message, Individu i, Famille f) {
        super("LinkIncoherentErr : " + message);
        this.i = i;
        this.f = f;
    }

    /**
     * Retounre la famille
     * @return
     */
    public Famille getFam() {
        return f;
    }
}
