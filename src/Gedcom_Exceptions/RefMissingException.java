package Gedcom_Exceptions;
import java.io.Serializable;

/**
 * UNE FAMILLE MANQUE DONC ON A UN ENFANT QUI N'A PAS DE PARENTS, ETC
 */
public class RefMissingException extends StructureException {
    private String id;
    private String type;

    /**
     * Constructeur de l'exception
     * @param message
     */    public RefMissingException(String message, String id, String type) {
        super("RefMissingErr : " + message);
        this.id = id;
        this.type = type;
    }

    /**
     * Retourne l'id
     * @return
     */
    public String getId() {
        return id;
    }
}
