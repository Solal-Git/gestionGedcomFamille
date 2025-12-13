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

    public void construireEtValiderGraphe() throws TwiceChildException {
        System.out.println("Validation des données en cours...");

        // On parcourt tous les individus pour vérifier leurs liens parents (FAMC)
        for (Individu indiv : mapIndividus.values()) {
            validerLienEnfant(indiv);
            validerLiensConjoint(indiv);
        }

        detecterCycles();
    }

    private void validerLienEnfant(Individu indiv) throws TwiceChildException {
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
    private void validerLiensConjoint(Individu indiv) {
        for (String idFam : indiv.getFamsIds()) {
            try {
                Famille f = mapFamilles.get(idFam);
                if (f == null) throw new RefMissingException(idFam);

                indiv.addFamillePropreObj(f);

                // --- AJOUT DE LA LOGIQUE GENRE ---
                String sexe = (indiv.getSexTag() != null) ? indiv.getSexTag().toString() : "?";

                // Cas 1 : L'individu est listé comme le MARI
                if (indiv.getID().equals(f.getMariId())) {
                    if ("F".equals(sexe)) { // Si c'est une femme
                        throw new GenderMissMatchException(" Incohérence: Une femme est déclarée comme Mari dans la famille " + f.getID(), null, indiv.getID(), sexe, "HUSB");
                    }
                    f.setMariObj(indiv);
                }
                // Cas 2 : L'individu est listé comme la FEMME
                else if (indiv.getID().equals(f.getFemmeId())) {
                    if ("M".equals(sexe)) { // Si c'est un homme
                        throw new GenderMissMatchException(" Incohérence: Un homme est déclaré comme Femme dans la famille " + f.getID(), null, indiv.getID(), sexe, "WIFE");
                    }
                    f.setFemmeObj(indiv);
                }

            } catch (GenderMissMatchException e) {
                System.err.println("ERREUR DE GENRE : " + e.getMessage());
                // Correction : On laisse faire, ou on supprime le lien si on veut être strict
            } catch (RefMissingException e) {
                // ... (ton code existant) ...
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void detecterCycles() {
        for (Individu indiv : mapIndividus.values()) {
            try {
                verifierAncetres(indiv, indiv, new ArrayList<>());
            } catch (GenealogyException e) {
                System.err.println("CYCLE DETECTÉ : " + e.getMessage());
                // Correction radicale : on coupe le lien parent
                indiv.setFamilleParentObj(null);
            }
        }
    }

    private void verifierAncetres(Individu cible, Individu courant, ArrayList<String> chemin) throws GenealogyException {
        if (courant == null) return;
        Famille f = courant.getFamilleParentObj(); // Utilise le lien OBJET
        if (f != null) {
            verifierParent(cible, f.getMariObj(), chemin);
            verifierParent(cible, f.getFemmeObj(), chemin);
        }
    }

    private void verifierParent(Individu cible, Individu parent, ArrayList<String> chemin) throws GenealogyException {
        if (parent != null) {
            if (parent.getID().equals(cible.getID())) {
                throw new GenealogyException(chemin, " L'individu " + cible.getID() + " tourne en rond !");
            }
            // Pour éviter les boucles infinies de l'algo, on vérifie si on a déjà vu ce parent dans ce chemin
            if (!chemin.contains(parent.getID())) {
                ArrayList<String> nouveauChemin = new ArrayList<>(chemin);
                nouveauChemin.add(parent.getID());
                verifierAncetres(cible, parent, nouveauChemin);
            }
        }
    }
}