package Gedcom_elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import Gedcom_Exceptions.*;

/**
 * Classe qui gère le graphe
 */
public class GedcomGraph implements Serializable {

    private static final long serialVersionUID = 1L;        //version du ficchier pour la sérialisation
    private Map<String, Individu> mapIndividus;
    private Map<String, Famille> mapFamilles;

    /**
     * Constructeur de graphe
     */
    public GedcomGraph() {
        this.mapIndividus = new HashMap<>();
        this.mapFamilles = new HashMap<>();
    }

    /**
     * Ajoute un individu à la map
     * @param i
     */
    public void addIndividual(Individu i) {
        mapIndividus.put(i.getID(), i);
    }

    /**
     * Ajoute une famille à la map
     * @param f
     */
    public void addFamilly(Famille f) {
        mapFamilles.put(f.getID(), f);
    }

    /**
     * Retourne un individu donné
     * @param id
     * @return
     */
    public Individu getIndividual(String id) {
        return mapIndividus.get(id);
    }

    /**
     * Retourne une famille donnée
     * @param id
     * @return
     */
    public Famille getFamilly(String id) {
        return mapFamilles.get(id);
    }

    /**
     *Construit le graphe et vérifie les exceptions
     * @return
     */
    public List<String> buildAndValidGraph() {
        List<String> rapport = new ArrayList<>();

        //Copie de la liste pour éviter les erreurs si on ajoute des familles pendant la boucle
        List<Individu> tousLesIndividus = new ArrayList<>(mapIndividus.values());
        List<Famille> toutesLesFamilles = new ArrayList<>(mapFamilles.values());

        for (Individu indiv : tousLesIndividus) {

            //Je vérifie les erreurs de structures
            try {
                checkChildLink(indiv);
            }
            catch (IsAlreadyChildException e) {
                //L'enfant a déjà une famille
                rapport.add(e.getMessage() + " -> Second lien ignoré.");
            }
            catch (RefMissingException e) {
                //La famille pointée par FAMC n'existe pas
                rapport.add(e.getMessage() + " -> Famille créée.");

                Famille newF = new Famille(e.getId());
                addFamilly(newF);

                //On crée les liens de la nouvelle famille
                try {
                    newF.addEnfant(indiv.getID());
                }
                catch (Exception ex) {}

                newF.addEnfantObj(indiv);
                indiv.setFamilleParentObj(newF);
            }
            catch (LinkIncoherentException e) {
                //La famille existe mais n'a pas listé l'enfant
                rapport.add(e.getMessage() + " -> Lien manquant ajouté.");

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

            //Je vérifie la logique du .ged
            try {
                checkSpouseLink(indiv);
            }
            catch (GenderMissMatchException e) {
                rapport.add(e.getMessage());
            }
            catch (RefMissingException e) {
                rapport.add(e.getMessage() + " -> Famille créée.");

                Famille newF = new Famille(e.getId());
                addFamilly(newF);

                indiv.addFamillePropreObj(newF);

                String sexe = (indiv.getSexTag() != null) ? indiv.getSexTag().toString() : "UNKNOW";
                if ("M".equals(sexe)) {
                    newF.setMari(indiv.getID());
                    newF.setMariObj(indiv);
                }
                else if ("F".equals(sexe)) {
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
            }
            catch (TwiceChildException e) {
                rapport.add(e.getMessage());
            }
        }
        try {
            findCycles();
        }
        catch (GenealogyException e) {
            rapport.add(e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return rapport;
    }

    /**
     * Vérifie l'exception TwiceChildException
     * @param f
     * @throws TwiceChildException
     */
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

    /**
     * Vérifie les exceptions pour les enfants
     * @param indiv
     * @throws RefMissingException
     * @throws LinkIncoherentException
     * @throws IsAlreadyChildException
     */
    private void checkChildLink(Individu indiv) throws RefMissingException, LinkIncoherentException, IsAlreadyChildException {
        if (indiv.getNbParentsDeclares() > 1) {
            throw new IsAlreadyChildException("L'individu " + indiv.getID() +
                    " déclare " + indiv.getNbParentsDeclares() + " familles parentales.");
        }

        if (indiv.getFamilleParentObj() != null) {
            throw new IsAlreadyChildException("L'individu " + indiv.getID() + " est déjà lié à la famille " + indiv.getFamilleParentObj().getID());
        }

        String idFam = indiv.getFamcId();
        if (idFam == null) return;

        Famille f = mapFamilles.get(idFam);
        if (f == null) {
            throw new RefMissingException("Famille parentale " + idFam + " introuvable pour " + indiv.getID(), idFam, "FAM");
        }

        if (!f.getEnfantsIds().contains(indiv.getID())) {
            throw new LinkIncoherentException("Famille " + idFam + " ne connait pas son enfant " + indiv.getID(), indiv, f);
        }

        indiv.setFamilleParentObj(f);
        if (!f.getEnfantsObj().contains(indiv)) {
            f.addEnfantObj(indiv);
        }
    }

    /**
     * Vérifie les exceptions pour les adultes
     * @param indiv
     * @throws GenderMissMatchException
     * @throws RefMissingException
     */
    protected void checkSpouseLink(Individu indiv) throws GenderMissMatchException, RefMissingException {
        for (String idFam : indiv.getFamsIds()) {
            Famille f = mapFamilles.get(idFam);
            if (f == null) {
                throw new RefMissingException("Famille propre " + idFam + " introuvable", idFam, "FAM");
            }

            indiv.addFamillePropreObj(f);
            String sexe;
            if (indiv.getSexTag() != null) {
                sexe = indiv.getSexTag().toString();
            } else {
                sexe = "UNKNOW";
            }

            if (indiv.getID().equals(f.getMariId())) {
                f.setMariObj(indiv);
            }
            else if (indiv.getID().equals(f.getFemmeId())) {
                f.setFemmeObj(indiv);
            }

            //Le sexe n'est pas renseigné
            if ("UNKNOW".equals(sexe)) {
                throw new GenderMissMatchException("L'individu" + indiv.getID() + "N'a pas de sexe déclaré on ne peut donc pas vérifier si c'est bon");
            }

            //Une épouse est déclarée en tant que mari
            if (indiv.getID().equals(f.getMariId())) {
                if ("F".equals(sexe)) {
                    throw new GenderMissMatchException("Une FEMME (" +indiv.getID()+ ") est déclarée comme MARI dans " + f.getID());
                }
            }

            // un époux est déclaré en tant qu'épouse
            else if (indiv.getID().equals(f.getFemmeId())) {
                if ("M".equals(sexe)) {
                    throw new GenderMissMatchException("Un HOMME ("+indiv.getID()+") est déclaré comme FEMME dans " + f.getID());
                }
            }
        }
    }

    /**
     * Vérifie si le nom recherché est existant
     * @param recherche
     * @return
     * @throws NameNotFoundException
     */
    public Individu searchByName(String recherche) throws NameNotFoundException {
        String rechercheLower = recherche.toLowerCase();

        for (Individu i : mapIndividus.values()) {
            if (i.getNameTag() != null && !"UNKNOW".equals(i.getNameTag().toString())) {
                String fullName = i.getNameTag().toString().toLowerCase();
                if (fullName.contains(rechercheLower)) {
                    return i;
                }
            }
        }
        throw new NameNotFoundException("L'individu avec le nom '" + recherche + "' est introuvable dans le graphe.");
    }

    /**
     * Vérifie si un cycle est présent en appelant checkAncestors
     * @throws GenealogyException
     */
    private void findCycles() throws GenealogyException {               //initialise toutes la recherche du cycle
        for (Individu indiv : mapIndividus.values()) {
            ArrayList<String> cheminInitial = new ArrayList<>();
            cheminInitial.add(indiv.getID());
            checkAncestors(indiv, indiv, cheminInitial);
        }
    }

    /**
     * Appelle checkParents
     * @param cible
     * @param courant
     * @param chemin
     * @throws GenealogyException
     */
    private void checkAncestors(Individu cible, Individu courant, ArrayList<String> chemin) throws GenealogyException {
        if (courant == null) return;
        Famille f = courant.getFamilleParentObj();                  //récupérer le pere et la mere de la famille

        if (f != null) {
            checkParents(cible, f.getMariObj(), chemin);            // On vérifie le père
            checkParents(cible, f.getFemmeObj(), chemin);           // On vérifie la mère
        }
    }

    /**
     *  Vérifie si un enfant et parent de lui même
     * @param cible
     * @param parent
     * @param chemin
     * @throws GenealogyException
     */
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