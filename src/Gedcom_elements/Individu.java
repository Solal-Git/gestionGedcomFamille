package Gedcom_elements;
import java.util.ArrayList;
import java.util.List;

public class Individu extends GedcomEntity {

    // On stocke les OBJETS spécifiques pour un accès rapide
    private TagName nameTag;
    private TagSexe sexTag;
    private TagMultimedia multimediaTag;  // A faire pour l'attribut OBJE, A AMELIORER !!!!

    //Lien vers les familles
    private String famcId;
    private List<String> famsIds = new ArrayList<>();

    public Individu(String id) {
        super(0, "INDI", null, id);
    }

    //Appelé par le lecteur
    public void ajouterPropriete(GedcomTag tag) {
        tag.attributionIndividu(this);
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

    // A CHANGER !!!!!! LES 3 POUR LES UTILISER AVEC LE TOSTRING !!!!
    public TagName getNameTag() {
        return nameTag;
    }

    public TagSexe getSexTag() {
        return sexTag;
    }

    public TagMultimedia getMultimediaTag() {
        return multimediaTag;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Individu [").append(ID).append("]: ");
        sb.append(getNameTag());
        if (sexTag != null) sb.append(", ").append(sexTag.toString());
        if (multimediaTag != null) {
            sb.append("\n  Média : ").append(multimediaTag.toString());
        }
        return sb.toString();
    }
}