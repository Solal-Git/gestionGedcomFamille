package Gedcom_Exceptions;

// Pas très inclusive comme erreur pas trop pour le mariage pour tous ^^

public class GenderMissMatchException extends GedcomNatException {
    private String id;                    //id de la perssonne
    private  String sexDeclared;                //Sex déclaré
    private String sexTag;                // tag de la personne donc la où ça bloque


    public GenderMissMatchException(String message, Throwable cause, String id, String sexDeclared, String sexTag ) {
        super("GenderMissMatchError : Erreur de sexe déclarée, ne correspond pas" + message, cause);
        this.id = id;
        this.sexDeclared = sexDeclared;
        this.sexTag = sexTag;
    }
    public GenderMissMatchException(String message) {
        super("GenderMissMatchError : Erreur de sexe déclarée, ne correspond pas" + message);
    }

    public String getId() {
        return id;
    }
    public String getSexDeclared(){
        return sexDeclared;
    }
    public String getSexTag() {
        return sexTag;
    }

}
