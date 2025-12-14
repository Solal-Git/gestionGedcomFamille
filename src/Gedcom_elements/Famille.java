package Gedcom_elements;

import Gedcom_Tag.TagMultimedia;
import java.util.ArrayList;
import java.util.List;
import Gedcom_Exceptions.*;

public class Famille extends GedcomEntity {

    // Parsing (Strings)
    private String HUSB;
    private String WIFE;
    private List<String> CHIL;
    private TagMultimedia multimediaTag;

    // Graphe (Objets réels à AJOUTER)
    private transient Individu mariObj;
    private transient Individu femmeObj;
    private transient List<Individu> enfantsObj;

    public Famille(String id) {
        super(0, "FAM", null, id);
        this.CHIL = new ArrayList<>();
        this.enfantsObj = new ArrayList<>();
        this.HUSB = null; // Mieux vaut null que "" pour détecter l'absence
        this.WIFE = null;
    }

    // ... Tes setters existants ...

    // --- AJOUTER CES GETTERS (Indispensable) ---
    public String getMariId() {
        return HUSB;
    }

    public String getFemmeId() {
        return WIFE;
    }

    public List<String> getEnfantsIds() {
        return CHIL;
    }

    // --- AJOUTER CES MÉTHODES DE LIAISON ---
    public void setMariObj(Individu i) {
        this.mariObj = i;
    }

    public Individu getMariObj() {
        return mariObj;
    }

    public void setFemmeObj(Individu i) {
        this.femmeObj = i;
    }

    public Individu getFemmeObj() {
        return femmeObj;
    }

    public void addEnfantObj(Individu i) {
        if (this.enfantsObj == null) {
            this.enfantsObj = new ArrayList<>();
        }
        this.enfantsObj.add(i);
    }

    public List<Individu> getEnfantsObj() {
        if (this.enfantsObj == null) {
            this.enfantsObj = new ArrayList<>();
        }
        return this.enfantsObj;
    }

    // ... Reste de ta classe ...
    public void setMari(String id) {
        this.HUSB = id;
    }

    public void setFemme(String id) {
        this.WIFE = id;
    }

    public void addEnfant(String idEnfant) throws TwiceChildException {
        if (this.CHIL.contains(idEnfant)) {
            throw new TwiceChildException("L'enfant " + idEnfant + " est déjà présent dans la famille " + this.ID);
        }
        this.CHIL.add(idEnfant);
    }

    public void setMultimedia(TagMultimedia multimedia) {
        this.multimediaTag = multimedia;
    }

    @Override
    public String toString() {
        return "Famille " + ID +"\n" +" "+ HUSB +" "+ WIFE +" "+ CHIL;
    }
}