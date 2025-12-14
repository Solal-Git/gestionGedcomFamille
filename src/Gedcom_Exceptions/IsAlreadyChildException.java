package Gedcom_Exceptions;


//UN ENFANT EST L'ENFANT D'UNE SEULE FAMILLE
public class IsAlreadyChildException extends StructureException {
    public IsAlreadyChildException(String message) {
        super("IsAlreadyChildErr : " + message);
    }
}
