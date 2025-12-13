package Gedcom_Parsing;

import Gedcom_Exceptions.GedcomNatException;
import Gedcom_elements.GedcomGraph;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GedcomParser {
    private GedcomGraph graph;
    public GedcomParser() {
        graph = new GedcomGraph();
    }

    public GedcomGraph parse(String filePath) throws IOException, GedcomNatException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNum = 0;

            while ((line = reader.readLine()) != null) {
                lineNum++;
                line = line.trim();
                if (line.isEmpty()) continue;

                // 1. On découpe la ligne brute en morceaux utilisables
                ParsedLine parsed = analyzeLine(line);

                // 2. On traite selon le niveau (0, 1, 2...)
                processLine(parsed);
            }
        }

        // 3. Une fois la lecture finie, on lance la validation (Etape 2)
        // C'est ici qu'on transforme les Strings en Objets et qu'on répare les erreurs
        graph.construireEtValiderGraphe();

        return graph;
    }
}
