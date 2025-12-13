package Gedcom_elements;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GedcomGraph implements Serializable {

    private Map<String, Individu> mapIndividus;
    private Map<String, Famille> mapFamilles;

    public GedcomGraph() {
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

    // Utile pour valider que tout est chargé
    public void afficherStats() {
        System.out.println("Graphe chargé : " + mapIndividus.size() + " individus, " + mapFamilles.size() + " familles.");
    }
}