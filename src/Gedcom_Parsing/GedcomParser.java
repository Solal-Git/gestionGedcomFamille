package Gedcom_Parsing;

import Gedcom_Exceptions.GenderMissMatchException;
import Gedcom_Exceptions.GenealogyException;
import Gedcom_Exceptions.RefMissingException;
import Gedcom_Exceptions.TwiceChildException;
import Gedcom_elements.*;
import Gedcom_Tag.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GedcomParser {
    private GedcomGraph graph;
    private Individu currentIndividu = null;
    private Famille currentFamille = null;
    private String lastTagLevel1 = "";
    private TagMultimedia currentMultimedia = null; // Pour construire l'objet petit à petit

    public GedcomParser() {
        graph = new GedcomGraph();
    }

    public GedcomGraph parse(String filePath) throws IOException, TwiceChildException, GenealogyException, GenderMissMatchException, RefMissingException {
        //J'AI MIS LES 2 POUR DEBUG LE LOAD ET LE IMPORT
        if (!filePath.equals("In/" + filePath)) {
            filePath = "In/" + filePath;
        }
        if (!filePath.endsWith(".ged")) {
            filePath += ".ged";
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                ParsedLine parsed = analyzeLine(line);
                processLine(parsed);
            }
        }

        graph.buildAndValidGraph();

        return graph;
    }

    private void processLine(ParsedLine line) {
        if (line.level == 0) {
            currentIndividu = null;
            currentFamille = null;
            lastTagLevel1 = ""; // Reset

            if ("INDI".equals(line.tag)) {
                currentIndividu = new Individu(line.id);
                graph.addIndividual(currentIndividu);
            } else if ("FAM".equals(line.tag)) {
                currentFamille = new Famille(line.id);
                graph.addFamilly(currentFamille);
            }
        }

        // --- NIVEAU 1 ---
        else if (line.level == 1) {
            lastTagLevel1 = line.tag; // On mémorise le tag (ex: BIRT ou OBJE)

            if (currentIndividu != null) {
                switch (line.tag) {
                    case "NAME": currentIndividu.setName(new TagName(line.value)); break;
                    case "SEX":  currentIndividu.setSex(new TagSexe(line.value)); break;
                    case "FAMC": currentIndividu.setFamcId(line.value); break;
                    case "FAMS": currentIndividu.addFamsId(line.value); break;
                    case "OBJE": // Début d'un bloc multimédia
                        currentMultimedia = new TagMultimedia();
                        currentIndividu.setMultimedia(currentMultimedia);
                        break;
                }
            }
            else if (currentFamille != null) {
                switch (line.tag) {
                    case "HUSB":
                        currentFamille.setMari(line.value); // Stocke l'ID du mari (ex: @I1@)
                        break;

                    case "WIFE":
                        currentFamille.setFemme(line.value); // Stocke l'ID de la femme
                        break;

                    case "CHIL":
                        try {
                            currentFamille.addEnfant(line.value);
                        } catch (TwiceChildException e) {
                            // On attrape l'exception si l'enfant est cité deux fois
                            System.err.println("Attention (Doublon ignoré) : " + e.getMessage());
                        }
                        break;
                    case "MARR":
                        break;
                    case "OBJE":
                        currentMultimedia = new TagMultimedia();
                        currentFamille.setMultimedia(currentMultimedia);
                        break;
                }
            }
        }
        else if (line.level == 2) {
            if (currentFamille != null) {
                if ("MARR".equals(lastTagLevel1) && "DATE".equals(line.tag)) {
                    currentFamille.setDateMariage(new TagDate(line.value));
                }
            }
            if ("OBJE".equals(lastTagLevel1) && currentMultimedia != null) {
                switch (line.tag) {
                    case "FILE": currentMultimedia.setFichier(line.value); break;
                    case "TITL": currentMultimedia.setTitre(line.value); break;
                    case "FORM": currentMultimedia.setFormat(line.value); break;
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