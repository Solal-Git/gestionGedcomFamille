package Gedcom_elements;

import Gedcom_Tag.TagMultimedia;
import java.util.ArrayList;
import java.util.List;
import Gedcom_Tag.TagDate;
import Gedcom_Exceptions.*;

/**
 * Classe pour gérer les familles
 */
public class Famille extends GedcomEntity {

    private String HUSB;
    private String WIFE;
    private List<String> CHIL;
    private TagDate dateMariage;
    private TagMultimedia multimediaTag;

    private transient Individu mariObj;
    private transient Individu femmeObj;
    private transient List<Individu> enfantsObj;

    public Famille(String id) {
        super(0, "FAM", null, id);
        this.CHIL = new ArrayList<>();
        this.enfantsObj = new ArrayList<>();
        this.HUSB = null;
        this.WIFE = null;
    }

    public void setDateMariage(TagDate date) {
        this.dateMariage = date;
    }

    public TagDate getDateMariage() {
        return this.dateMariage;
    }

    public String getMariId() {
        return HUSB;
    }

    public String getFemmeId() {
        return WIFE;
    }

    public List<String> getEnfantsIds() {
        return CHIL;
    }

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

    public void setMari(String id) {
        this.HUSB = id;
    }

    public void setFemme(String id) {
        this.WIFE = id;
    }

    public void addEnfant(String idEnfant) {
        this.CHIL.add(idEnfant);
    }

    public void setMultimedia(TagMultimedia multimedia) {
        this.multimediaTag = multimedia;
    }

    /**
     * réécriture du toStriing pour avoir les informations.
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Famille ").append(ID).append("\n");
        sb.append(" Mari   : ");
        if (mariObj != null && mariObj.getNameTag() != null) {
            sb.append(mariObj.getNameTag().toString());
        } else {
            sb.append(HUSB != null ? HUSB : "UNKNOW");
        }
        sb.append("\n");
        sb.append(" Femme  : ");
        if (femmeObj != null && femmeObj.getNameTag() != null) {
            sb.append(femmeObj.getNameTag().toString());
        } else {
            sb.append(WIFE != null ? WIFE : "UNKNOW");
        }
        sb.append("\n");
        if (dateMariage != null) {
            sb.append(" Mariage   : ").append(dateMariage.toString()).append("\n");
        }
        else {
            sb.append(" Mariage   : UNKNOW  \n");
        }
        sb.append(" Enfants: ");
        if (enfantsObj != null && !enfantsObj.isEmpty()) {
            boolean premier = true;
            for (Individu enf : enfantsObj) {
                if (!premier) sb.append(", ");

                if (enf.getNameTag() != null) {
                    sb.append(enf.getNameTag().toString());
                } else {
                    sb.append(enf.getID());
                }
                premier = false;
            }
        } else {
            sb.append("Aucun");
        }
        if (multimediaTag != null){
            sb.append("\n" + multimediaTag);
        }
        sb.append("\n");
        return sb.toString();
    }
}
