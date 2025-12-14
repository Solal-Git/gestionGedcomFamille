package Gedcom_elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import Gedcom_Exceptions.*;

public class GedcomGraph implements Serializable {

    private static final long serialVersionUID = 1L;        //version du ficchier pour la sérialisation

    private Map<String, Individu> mapIndividus;
    private Map<String, Famille> mapFamilles;
    public GedcomGraph() {
        this.mapIndividus = new HashMap<>();
        this.mapFamilles = new HashMap<>();
    }
    //adder
    public void addIndividual(Individu i) {
        mapIndividus.put(i.getID(), i);
    }

    public void addFamilly(Famille f) {
        mapFamilles.put(f.getID(), f);
    }

    //Getter
    public Individu getIndividual(String id) {
        return mapIndividus.get(id);
    }

    public Famille getFamilly(String id) {
        return mapFamilles.get(id);
    }


    public void buildAndValidGraph() throws TwiceChildException, GenealogyException, GenderMissMatchException, RefMissingException {
        // On parcourt tous les individus pour vérifier leurs liens parents (FAMC)
        for (Individu indiv : mapIndividus.values()) {
            checkChildLink(indiv);
            checkSpouseLink(indiv);
        }

        findCycles();
    }

    private void checkChildLink(Individu indiv) throws TwiceChildException {
        String idFam = indiv.getFamcId();
        if (idFam == null) return; // Pas de parents connus, on passe

        try {
            //Chercher la famille
            Famille f = mapFamilles.get(idFam);
            if (f == null) {
                throw new RefMissingException(idFam);
            }
            // Vérifier la réciprocité (Est-ce que la famille connait cet enfant ?)
            if (!f.getEnfantsIds().contains(indiv.getID())) {
                throw new LinkIncoherentException("Lien non réciproque : Famille " + idFam + " ne liste pas l'enfant " + indiv.getID(), indiv.getID(), idFam);
            }
            //Tout est bon -> On lie les OBJETS
            indiv.setFamilleParentObj(f);
            if (!f.getEnfantsObj().contains(indiv)) {             // On s'assure que l'objet est aussi dans la liste de la famille
                f.addEnfantObj(indiv);
            }

        } catch (RefMissingException e) { //Correction de la famille manquante
            System.err.println("ERREUR : " + e.getMessage() + " -> Correction : Création de la famille.");
            Famille newF = new Famille(e.getIdManquant());
            addFamilly(newF);
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
    public Individu searchByName(String recherche) throws NameNotFoundException {
        String rechercheLower = recherche.toLowerCase();

        for (Individu i : mapIndividus.values()) {
            if (i.getNameTag() != null) {
                String fullName = i.getNameTag().toString().toLowerCase();
                if (fullName.contains(rechercheLower)) {
                    return i;
                }
            }
        }
        throw new NameNotFoundException(recherche);
    }
    private void checkSpouseLink(Individu indiv) throws GenderMissMatchException, RefMissingException {
        for (String idFam : indiv.getFamsIds()) {
            try {
                Famille f = mapFamilles.get(idFam);
                if (f == null) throw new RefMissingException(idFam);
                indiv.addFamillePropreObj(f);
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
                System.err.println("Erreur (Genre) : " + e.getMessage());
            } catch (RefMissingException e) {
                throw new RefMissingException(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void findCycles() throws GenealogyException {               //initialise toutes la recherche du cycle
        for (Individu indiv : mapIndividus.values()) {
            ArrayList<String> cheminInitial = new ArrayList<>();
            cheminInitial.add(indiv.getID());
            checkAncestors(indiv, indiv, cheminInitial);
        }
    }

    private void checkAncestors(Individu cible, Individu courant, ArrayList<String> chemin) throws GenealogyException {
        if (courant == null) return;
        Famille f = courant.getFamilleParentObj();                  //récupérer le pere et la mere de la famille

        if (f != null) {
            checkParents(cible, f.getMariObj(), chemin);            // On vérifie le père
            checkParents(cible, f.getFemmeObj(), chemin);           // On vérifie la mère
        }
    }

    private void checkParents(Individu cible, Individu parent, ArrayList<String> chemin) throws GenealogyException {
        if (parent != null) {
            try {
                if (parent.getID().equals(cible.getID())) {
                    chemin.add(parent.getID());                     //on ajt au chemin l'id si il est égale a l'id cible(donc cycle)
                    throw new GenealogyException(chemin, "L'individu " + cible.getID() + " tourne en rond !");
                }
                if (!chemin.contains(parent.getID())) {
                    ArrayList<String> nouveauChemin = new ArrayList<>(chemin);  //sinon on relance récursirvement avec un nouveau chemin et un nouvelle individu
                    nouveauChemin.add(parent.getID());
                    checkAncestors(cible, parent, nouveauChemin);
                }
            }
            catch (GenealogyException e) {
                throw new GenealogyException(e.getCYCLE(), e.getMessage());         //récup l'erreur si il y a un cycle
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Graphe généalogique\n");
        sb.append("Individus :\n");
        for (Individu i : mapIndividus.values()) {
            sb.append("  ").append(i).append("\n");
        }
        sb.append("\n");
        sb.append("Familles :\n");
        for (Famille f : mapFamilles.values()) {
            sb.append(f).append("\n");
        }
        return sb.toString();
    }
}