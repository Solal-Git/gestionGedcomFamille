package Gedcom_Exceptions;
import Gedcom_elements.*;

//LIEN MANQUANT ENTRE UNE FAMILLE ET UN INDIVIDU
public class LinkIncoherentException extends LogicException {
    private Individu i;
    private Famille f;

    public LinkIncoherentException(String message, Individu i, Famille f) {
        super("LinkIncoherentErr : " + message);
        this.i = i;
        this.f = f;
    }

    public Individu getIndiv() {
        return i;
    }
    public Famille getFam() {
        return f;
    }
}
