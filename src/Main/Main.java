import Gedcom_elements.*;
import Gedcom_Parsing.*; // On importe le Parser ET le Serializer
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        GedcomParser parser = new GedcomParser();
        GedcomGraph graph = null;
        Scanner scanner = new Scanner(System.in);
        String nomFichierDefaut = "test.ged";

        // Chargement initial automatique (optionnel)
        try {
            System.out.println("Chargement initial de " + nomFichierDefaut + "...");
            graph = parser.parse(nomFichierDefaut);
        } catch (Exception e) {
            System.out.println("Pas de fichier par défaut trouvé. Graph vide.");
            graph = new GedcomGraph(); // On démarre avec un graphe vide
        }

        boolean running = true;
        System.out.println("\n=== SYSTEME DE GENEALOGIE ===");
        System.out.println(" INFO <Nom>      : Détails d'une personne");
        System.out.println(" CHILD <Nom>     : Liste des enfants");
        System.out.println(" SAVE <Fichier>  : Sauvegarder le graphe (.ser)");
        System.out.println(" LOAD <Fichier>  : Charger un graphe (.ser)");
        System.out.println(" IMPORT <Fichier>: Lire un nouveau fichier GEDCOM (.ged)");
        System.out.println(" EXIT            : Quitter");
        System.out.println("=============================");

        while (running) {
            System.out.print("\nCommande > ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            String[] parts = input.split(" ", 2);
            String commande = parts[0].toUpperCase();
            String arg = (parts.length > 1) ? parts[1] : "";

            try {
                switch (commande) {
                    case "EXIT":
                        running = false;
                        break;

                    case "INFO":
                        if (graph != null) afficherInfo(graph, arg);
                        break;

                    case "CHILD":
                        if (graph != null) afficherEnfants(graph, arg);
                        break;

                    case "SAVE": // NOUVEAU
                        if (graph != null && !arg.isEmpty()) {
                            GedcomSerializer.sauvegarder(graph, arg);
                        } else {
                            System.out.println("Erreur : précisez un nom de fichier (ex: SAVE data)");
                        }
                        break;

                    case "LOAD": // NOUVEAU
                        if (!arg.isEmpty()) {
                            graph = GedcomSerializer.charger(arg);
                            // IMPORTANT : On reconstruit les liens objets qui étaient 'transient'
                            graph.construireEtValiderGraphe();
                        } else {
                            System.out.println("Erreur : précisez un nom de fichier.");
                        }
                        break;

                    case "IMPORT": // Pour changer de fichier GEDCOM
                        if (!arg.isEmpty()) {
                            graph = parser.parse(arg);
                            System.out.println("Nouveau fichier importé.");
                        }
                        break;

                    default:
                        System.out.println("Commande inconnue.");
                }
            } catch (Exception e) {
                System.out.println("Erreur durant l'opération : " + e.getMessage());
                // e.printStackTrace(); // Décommente pour voir les détails si bug
            }
        }
        scanner.close();
        System.out.println("Fermeture du programme.");
    }

    // --- Garde tes méthodes afficherInfo et afficherEnfants ici ---
    // (Je ne les remets pas pour ne pas surcharger, garde celles de l'étape précédente)

    private static void afficherInfo(GedcomGraph graph, String nom) {
        Individu i = graph.rechercheParNom(nom);
        if (i != null) System.out.println("Résultat : " + i.toString());
        else System.out.println("Introuvable.");
    }

    private static void afficherEnfants(GedcomGraph graph, String nom) {
        Individu parent = graph.rechercheParNom(nom);
        if (parent == null) { System.out.println("Introuvable."); return; }

        boolean found = false;
        System.out.println("Enfants de " + parent.getNameTag() + " :");
        for (Famille f : parent.getFamillesPropresObj()) {
            for (Individu enf : f.getEnfantsObj()) {
                System.out.println(" - " + enf.toString());
                found = true;
            }
        }
        if(!found) System.out.println("Aucun enfant.");
    }
}