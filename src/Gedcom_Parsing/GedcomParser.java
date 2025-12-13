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

    public GedcomGraph parse(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                ParsedLine parsed = analyzeLine(line);
                processLine(parsed);
            }
        }

        // Validation finale (IMPORTANT)
        graph.construireEtValiderGraphe();

        return graph;
    }

    private void processLine(ParsedLine line) {
        if (line.level == 0) {
            currentIndividu = null;
            currentFamille = null;

            if ("INDI".equals(line.tag)) {
                // On garde l'ID brut (avec les @)
                currentIndividu = new Individu(line.id);
                graph.ajouterIndividu(currentIndividu);
            }
            else if ("FAM".equals(line.tag)) {
                // On garde l'ID brut
                currentFamille = new Famille(line.id);
                graph.ajouterFamille(currentFamille);
            }
        }
        else if (line.level == 1) {
            if (currentIndividu != null) {
                switch (line.tag) {
                    case "NAME":
                        currentIndividu.addPropriete(new TagName(line.value));
                        break;
                    case "SEX":
                        currentIndividu.addPropriete(new TagSexe(line.value));
                        break;
                    case "FAMC":
                        // On garde la valeur brute (@F1@)
                        currentIndividu.setFamcId(line.value);
                        break;
                    case "FAMS":
                        currentIndividu.addFamsId(line.value);
                        break;
                }
            }
            else if (currentFamille != null) {
                switch (line.tag) {
                    case "HUSB":
                        currentFamille.setMari(line.value);
                        break;
                    case "WIFE":
                        currentFamille.setFemme(line.value);
                        break;
                    case "CHIL":
                        currentFamille.addEnfant(line.value);
                        break;
                }
            }
        }
    }

    private ParsedLine analyzeLine(String rawLine) {
        String[] parts = rawLine.split(" ", 3);
        int level = Integer.parseInt(parts[0]);
        String tag = "";
        String id = null;
        String value = null;

        if (parts.length > 1 && parts[1].startsWith("@")) {
            id = parts[1];
            if (parts.length > 2) tag = parts[2];
        } else {
            if (parts.length > 1) tag = parts[1];
            if (parts.length > 2) value = parts[2];
        }
        return new ParsedLine(level, tag, value, id);
    }

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