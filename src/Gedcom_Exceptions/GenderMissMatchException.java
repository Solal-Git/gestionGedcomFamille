package Gedcom_Exceptions;

/**
 * Pas très inclusive comme erreur pas trop pour le mariage pour tous ^^
  */
public class GenderMissMatchException extends LogicException {

    /**
     * Constructeur de cette exception
     * @param message
     */
    public GenderMissMatchException(String message) {
        super("GenderMissMatchErr : " + message);
    }

}
