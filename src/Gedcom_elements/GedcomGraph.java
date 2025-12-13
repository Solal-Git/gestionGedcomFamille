package Gedcom_elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import Gedcom_Exceptions.*;

public class GedcomGraph implements Serializable {

    private Map<String, Individu> mapIndividus;
    private Map<String, Famille> mapFamilles;

    public GedcomGraph() {
        this.mapIndividus = new HashMap<>();
        this.mapFamilles = new HashMap<>();
    }

    public void ajouterIndividu(Individu i) { mapIndividus.put(i.getID(), i); } // Attention: getID() vient de GedcomEntity
    public void ajouterFamille(Famille f) { mapFamilles.put(f.getID(), f); }

    public Individu getIndividu(String id) { return mapIndividus.get(id); }
    public Famille getFamille(String id) { return mapFamilles.get(id); }

    public void afficherStats() {
        System.out.println("Graphe chargé : " + mapIndividus.size() + " individus, " + mapFamilles.size() + " familles.");
    }

    // =========================================================
    // PARTIE VALIDATION ET LIAISON (LE COEUR DU PROJET)
    // =========================================================

    public void construireEtValiderGraphe() {
        System.out.println("Validation des données en cours...");

        // On parcourt tous les individus pour vérifier leurs liens parents (FAMC)
        for (Individu indiv : mapIndividus.values()) {
            validerLienEnfant(indiv);
            validerLiensConjoint(indiv);
        }

        // si on veut ajouter detecterCycles() c'est ici
    }

    private void validerLienEnfant(Individu indiv) {
        String idFam = indiv.getFamcId();
        if (idFam == null) return; // Pas de parents connus, on passe

        try {
            // 1. Chercher la famille
            Famille f = mapFamilles.get(idFam);
            if (f == null) {
                throw new RefMissingException(idFam);
            }

            // 2. Vérifier la réciprocité (Est-ce que la famille connait cet enfant ?)
            if (!f.getEnfantsIds().contains(indiv.getID())) {
                throw new LinkIncoherentException("Lien non réciproque : Famille " + idFam + " ne liste pas l'enfant " + indiv.getID(), indiv.getID(), idFam);
            }

            // 3. Tout est bon -> On lie les OBJETS
            indiv.setFamilleParentObj(f);

            // On s'assure que l'objet est aussi dans la liste de la famille
            if (!f.getEnfantsObj().contains(indiv)) {
                f.addEnfantObj(indiv);
            }

        } catch (RefMissingException e) {
            System.err.println("ERREUR : " + e.getMessage() + " -> Correction : Création de la famille.");
            // Correction : On crée la famille manquante
            Famille newF = new Famille(e.getIdManquant());
            ajouterFamille(newF);
            // On force le lien
            newF.addEnfant(indiv.getID());
            newF.addEnfantObj(indiv);
            indiv.setFamilleParentObj(newF);

        } catch (LinkIncoherentException e) {
            System.err.println("ERREUR : " + e.getMessage() + " -> Correction : Ajout force.");
            Famille f = mapFamilles.get(idFam);
            f.addEnfant(indiv.getID());
            f.addEnfantObj(indiv);
            indiv.setFamilleParentObj(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validerLiensConjoint(Individu indiv) {
        for (String idFam : indiv.getFamsIds()) {
            try {
                Famille f = mapFamilles.get(idFam);
                if (f == null) throw new RefMissingException(idFam);

                indiv.addFamillePropreObj(f);

                // Qui est cet individu dans la famille ? Mari ou Femme ?
                if (indiv.getID().equals(f.getMariId())) {
                    f.setMariObj(indiv);
                } else if (indiv.getID().equals(f.getFemmeId())) {
                    f.setFemmeObj(indiv);
                }
                // Si ni l'un ni l'autre, c'est une LinkIncoherent à gérer...

            } catch (RefMissingException e) {
                // Création famille manquante
                Famille newF = new Famille(e.getIdManquant());
                ajouterFamille(newF);
                indiv.addFamillePropreObj(newF);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Recherche un individu par son nom ou une partie de son nom.
     * Retourne le premier trouvé (ou null).
     */
    public Individu rechercheParNom(String recherche) {
        // On normalise en minuscule pour éviter les problèmes de majuscules
        String rechercheLower = recherche.toLowerCase();

        for (Individu i : mapIndividus.values()) {
            if (i.getNameTag() != null) {
                // On récupère le nom propre (sans les / /) via toString du TagName
                String nomComplet = i.getNameTag().toString().toLowerCase();

                // Si le nom contient la recherche (ex: "Jean Dupont" contient "Jean")
                if (nomComplet.contains(rechercheLower)) {
                    return i;
                }
            }
        }
        return null;
    }
}