package Gedcom_elements;
import Gedcom_Tag.*;
import java.util.ArrayList;
import java.util.List;

public class Individu extends GedcomEntity {

    private TagName nameTag;
    private TagSexe sexTag;
    private TagMultimedia multimediaTag;

    private String famcId;
    private List<String> famsIds = new ArrayList<>();

    private transient Famille familleParentObj;
    private transient List<Famille> famillesPropresObj = new ArrayList<>();

    public Individu(String id) {
        super(0, "INDI", null, id);
    }

    public String getFamcId() {
        return famcId;
    }

    public List<String> getFamsIds() {
        return famsIds;
    }

    public void setFamilleParentObj(Famille f) {
        this.familleParentObj = f;
    }
    public Famille getFamilleParentObj() {
        return familleParentObj;
    }

    public void addFamillePropreObj(Famille f) {
        if (this.famillesPropresObj == null) {
            this.famillesPropresObj = new ArrayList<>();
        }
        this.famillesPropresObj.add(f);
    }
    public List<Famille> getFamillesPropresObj() {
        if (this.famillesPropresObj == null) {
            this.famillesPropresObj = new ArrayList<>();
        }
        return this.famillesPropresObj;
    }

    public void setFamcId(String famcId) {
        this.famcId = famcId;
    }

    public void addFamsId(String famsId) {
        this.famsIds.add(famsId);
    }

    public void setName(TagName name) {
        this.nameTag = name;
    }

    public void setSex(TagSexe sex) {
        this.sexTag = sex;
    }

    public void setMultimedia(TagMultimedia multimedia) {
        this.multimediaTag = multimedia;
    }

    public TagName getNameTag() {
        return nameTag;
    }

    public TagSexe getSexTag(){
        return sexTag;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Individu [").append(ID).append("]: ");
        if(nameTag != null) sb.append(nameTag);
        else sb.append("Inconnu");
        return sb.toString();
    }
}