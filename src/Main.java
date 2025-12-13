import Gedcom_elements.*;
import Gedcom_Parsing.GedcomParser;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("   PROJET GEDCOM - TEST (Mode Raw ID)    ");
        System.out.println("=========================================");

        GedcomParser parser = new GedcomParser();
        String nomFichier = "test.ged";

        try {
            GedcomGraph graph = parser.parse(nomFichier);
            graph.afficherStats();

            System.out.println("\n>> Test de navigation :");

            // CHANGEMENT ICI : On cherche AVEC les @ car le parser ne les enlève plus
            String idRecherche = "@I3@";
            Individu enfant = graph.getIndividu(idRecherche);

            if (enfant != null) {
                System.out.println("Individu trouvé : " + enfant.toString());

                Famille fParent = enfant.getFamilleParentObj();
                if (fParent != null) {
                    System.out.println("  -> Enfant de la famille : " + fParent.getID());

                    Individu pere = fParent.getMariObj();
                    if (pere != null) System.out.println("     -> Père : " + pere.toString());

                    Individu mere = fParent.getFemmeObj();
                    if (mere != null) System.out.println("     -> Mère : " + mere.toString());

                } else {
                    System.out.println("  -> ERREUR : Pas de famille parente connectée.");
                }

            } else {
                System.out.println("Erreur : L'individu " + idRecherche + " introuvable.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}