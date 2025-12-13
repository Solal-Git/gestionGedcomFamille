import Gedcom_elements.*;
import Gedcom_Parsing.GedcomParser;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("   PROJET GEDCOM - TEST DE VALIDATION    ");
        System.out.println("=========================================");

        // 1. Initialisation du Parser
        GedcomParser parser = new GedcomParser();
        String nomFichier = "test.ged"; // Assure-toi que ce fichier est à la racine du projet

        try {
            // 2. LECTURE ET CONSTRUCTION DU GRAPHE
            System.out.println(">> Lecture du fichier " + nomFichier + "...");
            GedcomGraph graph = parser.parse(nomFichier);

            // 3. AFFICHAGE DES STATISTIQUES
            System.out.println("\n>> Statistiques après chargement :");
            graph.afficherStats();

            // 4. TEST DE NAVIGATION (Vérification des liens Objets)
            System.out.println("\n>> Test de navigation dans le Graphe :");

            // On cherche l'enfant "I3" (Paul)
            String idRecherche = "I3";
            Individu enfant = graph.getIndividu(idRecherche);

            if (enfant != null) {
                System.out.println("Individu trouvé : " + enfant.toString());

                // --- TEST CRUCIAL : Est-ce que le lien vers la famille parente fonctionne ? ---
                // On utilise le getter OBJET que nous avons ajouté (getFamilleParentObj)
                Famille fParent = enfant.getFamilleParentObj();

                if (fParent != null) {
                    System.out.println("  -> Enfant de la famille : " + fParent.getID());

                    // On remonte vers le Père (via l'objet Famille)
                    Individu pere = fParent.getMariObj();
                    if (pere != null) {
                        System.out.println("     -> Père trouvé : " + pere.toString());
                    } else {
                        System.out.println("     -> Père inconnu (Lien cassé ou absent)");
                    }

                    // On remonte vers la Mère
                    Individu mere = fParent.getFemmeObj();
                    if (mere != null) {
                        System.out.println("     -> Mère trouvée : " + mere.toString());
                    } else {
                        System.out.println("     -> Mère inconnue");
                    }

                } else {
                    System.out.println("  -> ERREUR : La famille parente (Objet) est nulle ! La liaison a échoué.");
                }

            } else {
                System.out.println("Erreur : L'individu " + idRecherche + " n'a pas été trouvé.");
            }

            // 5. TEST DES REQUETES UTILISATEUR (Exemple)
            System.out.println("\n>> Exemple de requête SIBLINGS (Frères et soeurs de Paul) :");
            if (enfant != null && enfant.getFamilleParentObj() != null) {
                System.out.println("Frères et soeurs de " + enfant.getNameTag() + " :");
                for (Individu frereSoeur : enfant.getFamilleParentObj().getEnfantsObj()) {
                    // On ne s'affiche pas soi-même
                    if (!frereSoeur.getID().equals(enfant.getID())) {
                        System.out.println(" - " + frereSoeur.toString());
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("ERREUR CRITIQUE : Impossible de lire le fichier.");
            System.err.println("Vérifiez que 'test.ged' est bien à la racine du projet (hors de src).");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Une erreur inattendue est survenue :");
            e.printStackTrace();
        }
    }
}