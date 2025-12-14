package Gedcom_elements;
import Gedcom_Tag.*;
import java.util.ArrayList;
import java.util.List;

public class Individu extends GedcomEntity {

    // ... Tes attributs existants (nameTag, sexTag, etc.) ...
    private TagName nameTag;
    private TagSexe sexTag;
    private TagMultimedia multimediaTag;

    // --- Côté Parsing (Ce que tu as déjà) ---
    private String famcId;
    private List<String> famsIds = new ArrayList<>();

    // --- Côté Graphe (CE QU'IL FAUT AJOUTER) ---
    // transient = on ne sauvegarde pas ça directement, on le reconstruit
    private transient Famille familleParentObj;
    private transient List<Famille> famillesPropresObj = new ArrayList<>();

    public Individu(String id) {
        super(0, "INDI", null, id);
    }

    // ... Tes setters existants ...

    // --- AJOUTER CES GETTERS (Indispensable pour le Graph) ---
    public String getFamcId() { return famcId; }
    public List<String> getFamsIds() { return famsIds; }

    // --- AJOUTER CES MÉTHODES DE LIAISON ---
    public void setFamilleParentObj(Famille f) { this.familleParentObj = f; }
    public Famille getFamilleParentObj() { return familleParentObj; }

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

    // ... Le reste de ta classe (addPropriete, toString, etc.) ...
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