package Gedcom_Exceptions;

// Pas très inclusive comme erreur pas trop pour le mariage pour tous ^^

public class GenderMissMatchException extends LogicException {

    public GenderMissMatchException(String message) {
        super("GenderMissMatchErr : " + message);
    }

}
