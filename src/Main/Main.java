package Main;

import Gedcom_elements.*;
import Gedcom_Parsing.*; // On importe le Parser ET le Serializer
import java.io.IOException;
import java.util.Scanner;

//EXÉCUTER LE CODE DEPUIS LE FICHIER DE BASE ET NON DANS LE SRC

public class Main {

    public static void main(String[] args) {
        GedcomParser parser = new GedcomParser();
        GedcomGraph graph = null;
        Scanner scanner = new Scanner(System.in);

        while (graph == null) {
            System.out.println("Qu'elle est le nom du fichier ged (sans le ged)");
            System.out.println("Le fichier doit se trouver dans le dossier In");
            //JE NE RAJOUTE PAS /IN CAR JE LE FAIT DANS parse ETANT DONNE QUE LE LOAD NE PASSE PAR PAR LE MAIN
            //DONC ON AURAIT UN IN/IN/
            String fileName = scanner.next() + ".ged";
            try {
                System.out.println("Chargement de " + fileName + "...");
                graph = parser.parse(fileName);
            } catch (Exception e) {
                System.out.println("Pas de fichier trouvé.");
                System.out.println("vérifié le nom du fichier mais aussi son emplacement.");
            }
        }

        boolean running = true;
        System.out.println("\n=== SYSTEME DE GENEALOGIE ===");
        System.out.println("\n=== OPTION DE RECHERCHE ===");
        System.out.println(" INFO <Nom>      : Détails d'une personne");
        System.out.println(" ALL             : Info de toutes les personnes");
        System.out.println(" CHILD <Nom>     : Liste des enfants");
        System.out.println("\n=== OPTION DE GESTION ===");
        System.out.println(" SAVE <Fichier>  : Sauvegarder le graphe (.ser)");
        System.out.println(" LOAD <Fichier>  : Charger un graphe (.ser)");
        System.out.println(" IMPORT <Fichier>: Lire un nouveau fichier GEDCOM (.ged)");
        System.out.println("=============================");
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
                        if (graph != null) {
                            afficherInfo(graph, arg);
                        }
                        break;

                    case "CHILD":
                        if (graph != null){
                            afficherEnfants(graph, arg);
                        }
                        break;

                    case "SAVE": // NOUVEAU
                        if (graph != null && !arg.isEmpty()) {
                            GedcomSerializer.save(graph, arg);
                        }
                        else {
                            System.out.println("Erreur : précisez un nom de fichier (ex: SAVE data)");
                        }
                        break;

                    case "LOAD": // NOUVEAU
                        if (!arg.isEmpty()) {
                            graph = GedcomSerializer.load(arg);
                            // IMPORTANT : On reconstruit les liens objets qui étaient 'transient'
                            graph.construireEtValiderGraphe();
                        }
                        else {
                            System.out.println("Erreur : précisez un nom de fichier.");
                        }
                        break;

                    case "IMPORT": // Pour changer de fichier GEDCOM
                        if (!arg.isEmpty()) {
                            graph = parser.parse(arg);
                            System.out.println("Nouveau fichier importé.");
                        }
                        break;

                    case "ALL" :
                        if (graph != null) {
                            System.out.println(graph.toString());
                        }
                        break;

                    default:
                        System.out.println("Commande inconnue.");
                }
            } catch (Exception e) {
                System.out.println("Erreur durant l'opération : " + e.getMessage());
            }
        }
        scanner.close();
        System.out.println("Fermeture du programme.");
    }


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