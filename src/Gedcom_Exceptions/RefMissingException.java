package Gedcom_Exceptions;
import java.io.Serializable;

//CA JE SAIS PAS
public class RefMissingException extends StructureException {
    private String id;
    private String type;

    public RefMissingException(String message, String id, String type) {
        super("RefMissingErr : " + message);
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }
    public String getType() {
        return type;
    }
}
