package Main;

import Gedcom_Exceptions.NameNotFoundException;
import Gedcom_elements.*;
import Gedcom_Parsing.*;

import java.util.List;
import java.util.Scanner;

//EXÉCUTER LE CODE DEPUIS LE FICHIER DE BASE ET NON DANS LE SRC

public class Main {

    public static void main(String[] args) {
        GedcomParser parser = new GedcomParser();
        GedcomGraph graph = null;
        Scanner scanner = new Scanner(System.in);

        while (graph == null) {
            System.out.println("\nQu'elle est le nom du fichier ged (sans le ged)");
            System.out.println("Le fichier doit se trouver dans le dossier In");
            System.out.println("\n!!! Si vous voulez utilisez une sauvegarde déjà existante écrivez skip !!!");
            String fileName = scanner.next() + ".ged";
            try {
                if (fileName.equals("skip.ged")) {          //condition pour ne pas forcer prendre un nouveau fichier
                    break;
                }
                System.out.println("Chargement de " + fileName + "...");            // tant que le fichier est pas trouvé on continue
                graph = parser.parse(fileName);
            } catch (Exception e) {
                System.out.println("Pas de fichier trouvé.");
                System.out.println("vérifié le nom du fichier mais aussi son emplacement.");
            }
        }

        boolean running = true;
        showCommand();

        while (running) {
            System.out.print("\nCommande > ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;
            String[] parts = input.split(" ", 2);
            String command = parts[0].toUpperCase();
            String arg = (parts.length > 1) ? parts[1] : "";

            try {
                switch (command) {
                    case "HELP" :                   //affiche les commandes
                        showCommand();
                        break;

                    case "EXIT":                    //quitte tout
                        running = false;
                        break;

                    case "FAMC" :
                        if (graph != null) {
                            showChildFamilyInfo(graph, arg);
                        }
                        else {
                            System.out.println("Il n'existe pas de graph encore il faut LOAD une sauvegarde.");     //si skip  et que le graphe est encore null
                        }
                        break;

                    case "MARRIED" :
                        if (graph != null) {
                            showSpouse(graph, arg);
                        }
                        else {
                            System.out.println("Il n'existe pas de graph encore il faut LOAD une sauvegarde.");     //si skip  et que le graphe est encore null
                        }
                        break;
                    case "INFO":
                        if (graph != null) {
                            showInfo(graph, arg);
                        }
                        else {
                            System.out.println("Il n'existe pas de graph encore il faut LOAD une sauvegarde.");     //si skip  et que le graphe est encore null
                        }
                        break;

                    case "CHILD":
                        if (graph != null){
                            showChild(graph, arg);
                        }
                        else {
                            System.out.println("Il n'existe pas de graph encore il faut LOAD une sauvegarde.");     //si skip  et que le graphe est encore null
                        }
                        break;

                    case "SAVE":                               //Save 1 try
                        if (graph != null && !arg.isEmpty()) {
                            GedcomSerializer.save(graph, arg);
                        }
                        else {
                            System.out.println("Erreur : précisez un nom de fichier (ex: SAVE data)");
                        }
                        break;

                    case "LOAD":                              //Load 1 try
                        if (!arg.isEmpty()) {
                            graph = GedcomSerializer.load(arg);
                            graph.buildAndValidGraph();
                        }
                        else {
                            System.out.println("Erreur : précisez un nom de fichier.");
                        }
                        break;

                    case "IMPORT":                                 // Pour changer de fichier GEDCOM
                        if (!arg.isEmpty()) {
                            graph = parser.parse(arg);
                            System.out.println("Nouveau fichier importé.");
                        }
                        break;

                    case "ALL" :
                        if (graph != null) {
                            System.out.println(graph);
                        }
                        else {
                            System.out.println("Il n'existe pas de graph encore il faut LOAD une sauvegarde.");     //si skip  et que le graphe est encore null
                        }
                        break;

                    default:
                        System.out.println("Commande inconnue.");
                }
            } catch (Exception e) {
                System.out.println("Erreur durant l'opération : " + e.getMessage());                    //commande non reconnue
            }
        }
        scanner.close();
        System.out.println("Fermeture du programme.");
    }


    private static void showInfo(GedcomGraph graph, String nom) throws NameNotFoundException {                       //Méthode pour la commande INFO
        Individu i = graph.searchByName(nom);
        if (i != null) System.out.println("Résultat : " + i.toString());
        else System.out.println("Introuvable.");
    }

    private static void showChild(GedcomGraph graph, String nom) throws NameNotFoundException {                      //méthode pour la commande CHILD
        Individu parent = graph.searchByName(nom);
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
    private static void showSpouse(GedcomGraph graph, String nom) throws NameNotFoundException {                 //méthode pour la commande MARRIED
        Individu i = graph.searchByName(nom);                                    // !!! Ajout possible d'une exception facile Andreas CHAUFFE toi pour la faire
        if (i.getSexTag().toString().equals("M")) {
            List<String> fam = i.getFamsIds();
            for (String j : fam) {
                Famille famS =(graph.getFamilly(j));
                String stringWife = famS.getFemmeId();
                System.out.println(graph.getIndividual(stringWife).toString());
            }
        }
        if (i.getSexTag().toString().equals("F")) {
            List<String> fam = i.getFamsIds();
            for (String j : fam) {
                Famille famS =(graph.getFamilly(j));
                String stringHusband = famS.getMariId();
                System.out.println(graph.getIndividual(stringHusband).toString());
            }
        }
    }
    private static void showChildFamilyInfo(GedcomGraph graph, String nom) throws NameNotFoundException {               //méthode pour la commande FAMC
        Individu i = graph.searchByName(nom);
        Famille famC = graph.getFamilly(i.getFamcId());
        System.out.println(famC.getFemmeId());
        if (famC == null) {
            System.out.println("Erreur, cette personne n'a pas de famille où il est renseigné comme enfant eregistré");
            return;
        }
        System.out.println(famC.toString());
    }

    private static void showCommand(){                                                      //méthode pour affichager les commandes
        System.out.println("\n===== SYSTEME DE GENEALOGIE =====");
        System.out.println("\n===== OPTION DE RECHERCHE =====");
        System.out.println(" INFO <Nom>      : Détails d'une personne");
        System.out.println(" ALL             : Info de toutes les personnes");
        System.out.println(" CHILD <Nom>     : Liste des enfants");
        System.out.println(" MARRIED <Nom>     : Donne le(a) conjoint(e)");
        System.out.println(" FAMC <Nom>     : Informations sur sa famille");
        System.out.println("\n===== OPTION DE GESTION =====");
        System.out.println(" SAVE <Fichier>  : Sauvegarder le graphe (.ser)");
        System.out.println(" LOAD <Fichier>  : Charger un graphe (.ser)");
        System.out.println(" IMPORT <Fichier>: Lire un nouveau fichier GEDCOM (.ged)");
        System.out.println("\n=============================");
        System.out.println(" HELP            : Affiche les commandes");
        System.out.println(" EXIT            : Quitter");
        System.out.println("=============================");
    }
}