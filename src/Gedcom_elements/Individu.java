package Gedcom_elements;
import Gedcom_Tag.*;
import java.util.ArrayList;
import java.util.List;

public class Individu extends GedcomEntity {

    private TagName nameTag;
    private TagSexe sexTag;
    private TagMultimedia multimediaTag;
    private TagDate birthTag;

    private String famcId;
    private List<String> famsIds = new ArrayList<>();

    //pour l'exception IsAlreadyChild
    private List<String> declarationsFamc;

    private transient Famille familleParentObj;
    private transient List<Famille> famillesPropresObj = new ArrayList<>();

    public Individu(String id) {
        super(0, "INDI", null, id);
        this.declarationsFamc = new ArrayList<>();
    }

    public TagDate getBirthTag() {
        return birthTag;
    }

    public void setBirthTag(TagDate birthTag) {
        this.birthTag = birthTag;
    }

    public void setBirthDate(String birthDate) {
        this.birthTag = new TagDate(birthDate);
    }

    public String getFamcId() {
        return famcId;
    }

    public TagMultimedia getMultimediaTag() {
        return multimediaTag;
    }

    public void addFamcId(String id) {
        this.declarationsFamc.add(id);

        if (this.famcId == null) {
            this.famcId = id;
        }
    }

    public int getNbParentsDeclares() {
        return this.declarationsFamc.size();
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
        addFamcId(famcId);
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
        if(nameTag != null){
            sb.append(nameTag);
        }
        else {
            sb.append("UNKNOW");
        }
        if (sexTag != null){
            sb.append("\n Sexe : " + sexTag);
        }
        else {
            sb.append("\n Sexe : UNKNOW");
        }
        if (multimediaTag != null){
            sb.append("\n" + multimediaTag);
        }
        if (birthTag != null){
            sb.append("\n Naissance : " + birthTag);
        }
        else {
            sb.append("\n Naissance : UNKNOW");
        }
        sb.append("\n");
        return sb.toString();
    }
}