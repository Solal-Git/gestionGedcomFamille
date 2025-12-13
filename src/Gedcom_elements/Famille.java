package Gedcom_elements;

import java.util.ArrayList;
import java.util.List;

public class Famille extends GedcomEntity {

    private String HUSB;   // Tag HUSB
    private String WIFE;  // Tag WIFE
    private List<String> CHIL; // Tags CHIL

    private TagMultimedia multimediaTag;

    public Famille(String id) {
        super(0, "FAM", null, id);
        this.CHIL = new ArrayList<>();
        this.HUSB = "";
        this.WIFE = "";
    }

    public void setMari(String id) {
        this.HUSB = id;
    }
    public void setFemme(String id) {
        this.WIFE = id;
    }

    public void ajouterEnfant(String idEnfant) {
        this.CHIL.add(idEnfant);
    }

    public void setMultimedia(TagMultimedia multimedia) {
        this.multimediaTag = multimedia;
    }

    @Override
    public String toString() {
        return "Famille " + ID + " (Mari: " + HUSB + ", Femme: " + WIFE + ")";
    }
}

/*package fr.univ.projetgedcom;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GenealogyGraph implements Serializable {

    private Map<String, Individu> mapIndividus;
    private Map<String, Famille> mapFamilles;

    public GenealogyGraph() {
        this.mapIndividus = new HashMap<>();
        this.mapFamilles = new HashMap<>();
    }

    public void ajouterIndividu(Individu i) {
        mapIndividus.put(i.getId(), i);
    }

    public void ajouterFamille(Famille f) {
        mapFamilles.put(f.getId(), f);
    }

    public Individu getIndividu(String id) {
        return mapIndividus.get(id);
    }

    public Famille getFamille(String id) {
        return mapFamilles.get(id);
    }

    public Map<String, Individu> getMapIndividus() { return mapIndividus; }
    public Map<String, Famille> getMapFamilles() { return mapFamilles; }
}*/
