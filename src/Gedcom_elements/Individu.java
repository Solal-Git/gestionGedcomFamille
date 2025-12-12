package Gedcom_elements;

import java.util.ArrayList;

public class Individu extends GedcomEntity {

    private ArrayList<GedcomEntity> proprietes ;

    private String idFamilleChild;                              // famille où l'individu est enfant
    private ArrayList<String> idsFamilles;                      //famille(s) où l'individu est parent.

    public Individu(String id) {
        super(0, "INDI", null, id);
        this.proprietes = new ArrayList<>();
        this.idsFamilles = new ArrayList<>();
    }

    public void ajouterPropriete(GedcomEntity prop) {
        this.proprietes.add(prop);
    }

    public void setFamc(String id) {
        this.idFamilleChild = id;
    }
    public void ajouterFams(String id) {
        this.idsFamilles.add(id);
    }

    @Override
    public String toString() {
        StringBuilder indInfo = new StringBuilder();
        indInfo.append("Individu : ").append(getID()).append("\n");
        for (GedcomEntity prop : this.proprietes){
            indInfo.append(" - ").append(prop).append("\n");
        }
        return indInfo.toString();
    }




}



/*public class Individu extends GedcomEntity {

    // 1. Liste Polymorphique : contient TagNom, TagSexe, TagDate, etc.
    private List<GedcomEntity> proprietes;

    // 2. LIENS "PARSING" (Strings temporaires lors de la lecture)
    private String idFamilleParentString;       // Tag FAMC
    private List<String> idsFamillesPropresString; // Tags FAMS

    // 3. LIENS "GRAPHE" (Vrais objets pour la navigation)
    // transient = ne pas sauvegarder directement lors de la sérialisation,
    // on les reconstruira ou on sauvegardera le graphe entier.
    private transient Famille familleParent;
    private transient List<Famille> famillesPropres;

    public Individu(String id) {
        super(0, "INDI", null, id);
        this.proprietes = new ArrayList<>();
        this.idsFamillesPropresString = new ArrayList<>();
        this.famillesPropres = new ArrayList<>();
    }

    // --- GESTION DES PROPRIETES (Tags) ---
    public void ajouterPropriete(GedcomEntity prop) {
        this.proprietes.add(prop);
    }

    // Méthode utilitaire pour trouver le nom dans la liste de tags
    public String getNomComplet() {
        for (GedcomEntity p : proprietes) {
            if (p instanceof TagNom) {
                return ((TagNom) p).getNomPropre();
            }
        }
        return "Inconnu (" + id + ")";
    }

    // --- GETTERS/SETTERS POUR LE PARSING (Strings) ---
    public void setFamcID(String id) { this.idFamilleParentString = id; }
    public String getFamcID() { return idFamilleParentString; }

    public void addFamsID(String id) { this.idsFamillesPropresString.add(id); }
    public List<String> getFamsIDs() { return idsFamillesPropresString; }

    // --- GETTERS/SETTERS POUR LE GRAPHE (Objets) ---
    public void setFamilleParent(Famille f) { this.familleParent = f; }
    public Famille getFamilleParent() { return familleParent; }

    public void addFamillePropre(Famille f) { this.famillesPropres.add(f); }
    public List<Famille> getFamillesPropres() { return famillesPropres; }

    @Override
    public String toString() {
        return getNomComplet();
    }
}*/
