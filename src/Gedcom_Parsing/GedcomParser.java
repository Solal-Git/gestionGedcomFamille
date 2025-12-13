package Gedcom_Parsing;

import Gedcom_elements.*;
import GedcomTag.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GedcomParser {
    private GedcomGraph graph;
    private Individu currentIndividu = null;
    private Famille currentFamille = null;

    public GedcomParser() {
        graph = new GedcomGraph();
    }

        /**
         * Lit le fichier et retourne le graphe rempli (mais pas encore validé/lié)
         */
        public GedcomGraph parse(String filePath) throws IOException {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) continue;

                    // 1. Analyse syntaxique de la ligne
                    ParsedLine parsed = analyzeLine(line);

                    // 2. Traitement des données
                    processLine(parsed);
                }
            }

            // IMPORTANT : Ici, on devrait appeler la méthode de validation/liaison
            // que nous avons vue à l'étape 2 (construireEtValiderGraphe).
            // Comme elle n'est pas dans le bout de code que tu m'as envoyé ci-dessus,
            // je la mets en commentaire, mais il faudra l'ajouter dans GedcomGraph !

            graph.construireEtValiderGraphe();

            return graph;
        }

        /**
         * Cœur du parser : décide quoi faire selon le tag et le niveau
         */
        private void processLine(ParsedLine line) {

            // --- NIVEAU 0 : Nouveaux Objets ---
            if (line.level == 0) {
                // Reset du contexte
                currentIndividu = null;
                currentFamille = null;

                if ("INDI".equals(line.tag)) {
                    currentIndividu = new Individu(line.id);
                    graph.ajouterIndividu(currentIndividu);
                }
                else if ("FAM".equals(line.tag)) {
                    currentFamille = new Famille(line.id);
                    graph.ajouterFamille(currentFamille);
                }
            }

            // --- NIVEAU 1 : Propriétés des Objets ---
            else if (line.level == 1) {

                // Si on est dans un INDIVIDU
                if (currentIndividu != null) {
                    switch (line.tag) {
                        case "NAME":
                            currentIndividu.addPropriete(new TagName(line.value));
                            break;
                        case "SEX":
                            currentIndividu.addPropriete(new TagSexe(line.value));
                            break;
                        case "FAMC": // Enfant de...
                            currentIndividu.setFamcId(line.value);
                            break;
                        case "FAMS": // Parent de...
                            currentIndividu.addFamsId(line.value);
                            break;
                    }
                }

                // Si on est dans une FAMILLE
                else if (currentFamille != null) {
                    switch (line.tag) {
                        case "HUSB": // Le mari
                            currentFamille.setMari(line.value);
                            break;
                        case "WIFE": // La femme
                            currentFamille.setFemme(line.value);
                            break;
                        case "CHIL": // Un enfant
                            currentFamille.addEnfant(line.value);
                            break;
                    }
                }
            }
            // Tu peux gérer le niveau 2 (DATE, PLAC) ici si tu veux plus tard
        }

        /**
         * Découpe une ligne GEDCOM brute.
         * Cas 1 : "0 @I1@ INDI" (Niveau ID Tag)
         * Cas 2 : "1 NAME Jean" (Niveau Tag Valeur)
         */
        private ParsedLine analyzeLine(String rawLine) {
            String[] parts = rawLine.split(" ", 3); // Max 3 morceaux

            int level = Integer.parseInt(parts[0]);
            String tag = "";
            String id = null;
            String value = null;

            // Détection du format avec ID (@...@) en deuxième position
            if (parts.length > 1 && parts[1].startsWith("@")) {
                // Format : 0 @I1@ INDI
                id = parts[1];
                if (parts.length > 2) tag = parts[2];
            } else {
                // Format : 1 NAME Jean
                if (parts.length > 1) tag = parts[1];
                if (parts.length > 2) value = parts[2];
            }

            return new ParsedLine(level, tag, value, id);
        }
        /**
         * Classe interne simple pour stocker le résultat du découpage
         */
        private static class ParsedLine {
            int level;
            String tag;
            String value;
            String id;

            public ParsedLine(int level, String tag, String value, String id) {
                this.level = level;
                this.tag = tag;
                this.value = value;
                this.id = id;
            }
        }
    }
