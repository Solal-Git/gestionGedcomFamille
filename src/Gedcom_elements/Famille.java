package Gedcom_elements;

import java.util.ArrayList;
import java.util.List;

public class Famille extends GedcomEntity {

    private String idMari;   // Tag HUSB
    private String idFemme;  // Tag WIFE
    private List<String> idsEnfants; // Tags CHIL

    public Famille(String id) {
        super(0, "FAM", null, id);
        this.idsEnfants = new ArrayList<>();
    }

    public void setMari(String id) { this.idMari = id; }
    public void setFemme(String id) { this.idFemme = id; }

    public void ajouterEnfant(String idEnfant) {
        this.idsEnfants.add(idEnfant);
    }

    @Override
    public String toString() {
        return "Famille " + id + " (Mari: " + idMari + ", Femme: " + idFemme + ")";
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
