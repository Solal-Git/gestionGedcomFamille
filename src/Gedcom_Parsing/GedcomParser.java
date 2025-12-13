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

