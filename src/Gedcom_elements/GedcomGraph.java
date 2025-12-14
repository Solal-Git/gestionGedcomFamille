package Gedcom_elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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


    public List<String> buildAndValidGraph() {
        List<String> rapport = new ArrayList<>();

        // Copie de la liste pour éviter les erreurs si on ajoute des familles pendant la boucle
        List<Individu> tousLesIndividus = new ArrayList<>(mapIndividus.values());
        List<Famille> toutesLesFamilles = new ArrayList<>(mapFamilles.values());

        for (Individu indiv : tousLesIndividus) {

            // --- BLOC 1 : VÉRIFICATION STRUCTURELLE (Liens Parents) ---
            try {
                checkChildLink(indiv); // LANCE l'erreur vers le catch ci-dessous
            }
            catch (IsAlreadyChildException e) {
                // CAS : L'enfant a déjà une famille -> On signale et on ignore
                rapport.add("[ALERTE] " + e.getMessage() + " -> Second lien ignoré.");
            }
            catch (RefMissingException e) {
                // CAS : La famille pointée par FAMC n'existe pas -> On crée
                rapport.add("[RÉPARATION] " + e.getMessage() + " -> Famille créée.");

                // On vérifie si c'est bien une FAMILLE qui manque (selon ta classe RefMissing)
                // Si tu n'as pas ajouté le champ "type", on suppose que c'est une famille ici par contexte
                Famille newF = new Famille(e.getId());
                addFamilly(newF);

                // On force les liens
                try {
                    newF.addEnfant(indiv.getID());
                } catch (Exception ex) {}

                newF.addEnfantObj(indiv);
                indiv.setFamilleParentObj(newF);
            }
            catch (LinkIncoherentException e) {
                // CAS : La famille existe mais n'a pas listé l'enfant -> On ajoute
                rapport.add("[RÉPARATION] " + e.getMessage() + " -> Lien réciproque ajouté.");

                // Note : Ta classe LinkIncoherentException doit avoir une méthode pour récupérer la Famille
                // Si tu ne l'as pas mise, utilise mapFamilles.get(e.getLinkIdFrom())
                Famille f = e.getFam();
                if(f != null) {
                    try {
                        f.addEnfant(indiv.getID());
                    }
                    catch (Exception ex) {}
                    f.addEnfantObj(indiv);
                }
                indiv.setFamilleParentObj(f);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            // --- BLOC 2 : VÉRIFICATION SÉMANTIQUE (Genre, Conjoint) ---
            try {
                checkSpouseLink(indiv);
            }
            catch (GenderMissMatchException e) {
                // On signale l'erreur de genre
                rapport.add("[ALERTE GENRE] " + e.getMessage());
            }
            catch (RefMissingException e) {
                // --- CORRECTION ICI : ON FAIT PAREIL QUE POUR FAMC ---
                rapport.add("[RÉPARATION] " + e.getMessage() + " -> Famille créée.");

                // 1. On crée la famille manquante
                Famille newF = new Famille(e.getId());
                addFamilly(newF);

                // 2. On lie l'individu à cette famille
                indiv.addFamillePropreObj(newF);

                // 3. On essaie de deviner si c'est le mari ou la femme
                String sexe = (indiv.getSexTag() != null) ? indiv.getSexTag().toString() : "?";
                if ("M".equals(sexe)) {
                    newF.setMari(indiv.getID());
                    newF.setMariObj(indiv);
                } else if ("F".equals(sexe)) {
                    newF.setFemme(indiv.getID());
                    newF.setFemmeObj(indiv);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (Famille f : toutesLesFamilles) {
            try {
                checkTwiceChild(f);
            } catch (TwiceChildException e) {
                rapport.add("[ALERTE DOUBLON] " + e.getMessage());
            }
        }

        // --- BLOC 3 : DÉTECTION DE CYCLES ---
        try {
            findCycles();
        } catch (GenealogyException e) {
            rapport.add("[ERREUR GRAVE CYCLE] " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rapport;
    }

    private void checkTwiceChild(Famille f) throws TwiceChildException {
        List<String> enfants = f.getEnfantsIds();
        List<String> enfantsVus = new ArrayList<>();

        for (String idEnfant : enfants) {
            if (enfantsVus.contains(idEnfant)) {
                throw new TwiceChildException("L'enfant " + idEnfant + " est cité en double dans la famille " + f.getID());
            }
            enfantsVus.add(idEnfant);
        }
    }

    private void checkChildLink(Individu indiv) throws RefMissingException, LinkIncoherentException, IsAlreadyChildException {
        if (indiv.getFamilleParentObj() != null) {
            throw new IsAlreadyChildException("L'individu " + indiv.getID() + " est déjà lié à la famille " + indiv.getFamilleParentObj().getID());
        }

        String idFam = indiv.getFamcId();
        if (idFam == null) return;

        // 1. Chercher la famille
        Famille f = mapFamilles.get(idFam);
        if (f == null) {
            // Pas de Try-Catch ici ! On lance l'erreur !
            throw new RefMissingException("Famille parentale " + idFam + " introuvable pour " + indiv.getID(), idFam, "FAM");
        }

        // 2. Vérifier la réciprocité
        if (!f.getEnfantsIds().contains(indiv.getID())) {
            throw new LinkIncoherentException("Famille " + idFam + " ne connait pas son enfant " + indiv.getID(), indiv, f);
        }

        // 3. Si tout va bien, on lie les objets
        indiv.setFamilleParentObj(f);
        if (!f.getEnfantsObj().contains(indiv)) {
            f.addEnfantObj(indiv);
        }
    }


    protected void checkSpouseLink(Individu indiv) throws GenderMissMatchException, RefMissingException {
        for (String idFam : indiv.getFamsIds()) {
            Famille f = mapFamilles.get(idFam);
            if (f == null) {
                // CORRECTION : Ajout du type "FAM" manquant dans ton code précédent
                throw new RefMissingException("Famille propre " + idFam + " introuvable", idFam, "FAM");
            }

            indiv.addFamillePropreObj(f);
            String sexe = (indiv.getSexTag() != null) ? indiv.getSexTag().toString() : "?";

            // Cas 1 : L'individu est listé comme le MARI
            if (indiv.getID().equals(f.getMariId())) {
                if ("F".equals(sexe)) {
                    throw new GenderMissMatchException("Une FEMME (" +indiv.getID()+ ") est déclarée comme MARI dans " + f.getID());
                }
                f.setMariObj(indiv);
            }
            // Cas 2 : L'individu est listé comme la FEMME
            else if (indiv.getID().equals(f.getFemmeId())) {
                if ("M".equals(sexe)) {
                    throw new GenderMissMatchException("Un HOMME ("+indiv.getID()+") est déclaré comme FEMME dans " + f.getID());
                }
                f.setFemmeObj(indiv);
            }
        }
    }

    public Individu searchByName(String recherche) throws NameNotFoundException {
        // On passe tout en minuscules pour ne pas être gêné par la casse
        String rechercheLower = recherche.toLowerCase();

        for (Individu i : mapIndividus.values()) {
            // Sécurité : On vérifie que l'individu a bien un tag NAME
            if (i.getNameTag() != null) {
                String fullName = i.getNameTag().toString().toLowerCase();

                // .contains permet de trouver "Jean" si on tape juste "Je"
                if (fullName.contains(rechercheLower)) {
                    return i;
                }
            }
        }
        // Si la boucle se termine sans résultat, on lance l'erreur
        throw new NameNotFoundException("L'individu avec le nom '" + recherche + "' est introuvable dans le graphe.");
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
                    throw new GenealogyException("L'individu " + cible.getID() + " tourne en rond !", chemin);
                }
                if (!chemin.contains(parent.getID())) {
                    ArrayList<String> nouveauChemin = new ArrayList<>(chemin);  //sinon on relance récursirvement avec un nouveau chemin et un nouvelle individu
                    nouveauChemin.add(parent.getID());
                    checkAncestors(cible, parent, nouveauChemin);
                }
            }
            catch (GenealogyException e) {
                throw e;         //récup l'erreur si il y a un cycle
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