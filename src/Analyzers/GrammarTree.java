package Analyzers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by dimav on 28.11.16.
 */
public class GrammarTree {

    private LinkedHashMap<String, String[][]> grammarMap;
    private LinkedHashSet<String> nonterimalLexemesSet;

    public GrammarTree(String grammarFileName) throws FileNotFoundException {
        grammarMap = new LinkedHashMap<>();
        nonterimalLexemesSet = new LinkedHashSet<>();
        parseGrammarLexemesFile(grammarFileName);
    }



    private void parseGrammarLexemesFile(String grammarFileName) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(grammarFileName);
        Scanner scanner = new Scanner(fis);
        String[] fullLine;
        String[] decLine;
        String[][] fullArray;

        while(scanner.hasNextLine()){
            fullLine = scanner.nextLine().split(" *::= *");
            decLine = fullLine[1].split("\\|");
            fullArray = new String[decLine.length][];
            for (int i = 0; i < fullArray.length; i++) {
                fullArray[i] = decLine[i].split(" +");
            }
            grammarMap.put(fullLine[0], fullArray);
        }

        scanner.close();

        verify();
        addNonTerminalLexemes();
    }

    private boolean isTerminal(String lexeme) {
        return lexeme.length() > 2 && lexeme.charAt(0) == '<' && lexeme.charAt(lexeme.length() - 1) == '>';
    }

    private void addNonTerminalLexemes() {
        for (String key : grammarMap.keySet()) {
            for (String[] line : grammarMap.get(key)) {
                for (String lexeme : line) {
                    if (!isTerminal(lexeme) && !nonterimalLexemesSet.contains(lexeme)) {
                        nonterimalLexemesSet.add(lexeme);
                    }
                }
            }
        }
    }

    private void verify() {
        for (String key : grammarMap.keySet()) {
            for (String[] line : grammarMap.get(key)) {
                for (String lexeme : line) {
                    if (!grammarMap.containsKey(lexeme) && isTerminal(lexeme)) {
                        throw new IllegalArgumentException(key + " не содержит :" + lexeme + ";");
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (String key : grammarMap.keySet()) {
            builder.append(key);
            builder.append(":=");
            for (String[] line : grammarMap.get(key)) {
                for (String subLine : line) {
                    builder.append(subLine);
                }
                builder.append("|");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    private LinkedHashMap<String, Integer> getLexemesMap() {
        int index = 0;
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        for (String key : grammarMap.keySet()) {
            map.put(key, index++);
        }
        for (String key : nonterimalLexemesSet) {
            map.put(key, index++);
        }
        map.put("#", index);
        return map;
    }

    private String[][] createPrecedenceTable(LinkedHashMap<String, Integer> lexemesMap) {
        String[][] table = new String[lexemesMap.size() + 1][lexemesMap.size() + 1];
        int index = 1;
        for (String key : lexemesMap.keySet()) {
            table[0][index] = key;
            table[index++][0] = key;
        }
        return table;
    }

    private void fillEquals(String[][] precedenceTable, LinkedHashMap<String, Integer> lexemesMap) {
        for (String[][] line : grammarMap.values()) {
            for (String[] lexemes : line) {
                for (int i = 0; i < lexemes.length - 1; i++) {
                    precedenceTable[lexemesMap.get(lexemes[i]) + 1][lexemesMap.get(lexemes[i + 1]) + 1] = "=";
                }
            }
        }
    }

    private HashMap<String, Iterable<String>> getFirstsMap(LinkedHashMap<String, Integer> lexemesMap) {
        HashMap<String, Iterable<String>> firstMap = new HashMap<>();
        for (String key :lexemesMap.keySet()) {
            if (isTerminal(key)) {
                firstMap.put(key, getFirstPlus(key));
            }
        }
        return firstMap;
    }

    private HashMap<String, Iterable<String>> getLastMap(LinkedHashMap<String, Integer> lexemesMap) {
        HashMap<String, Iterable<String>> lastMap = new HashMap<>();
        for (String key :lexemesMap.keySet()) {
            if (isTerminal(key)) {
                lastMap.put(key, getLastPlus(key));
            }
        }
        return lastMap;
    }

    private void fillMore(String[][] precedenceTable, LinkedHashMap<String, Integer> lexemesMap, HashMap<String, Iterable<String>> firstMap) {

        for (int i = 1; i < precedenceTable.length; i++) {
            for (int j = 1; j < precedenceTable[i].length; j++) {
                if (precedenceTable[i][j] != null && precedenceTable[i][j].equals("=")) {
                    if (firstMap.containsKey(precedenceTable[0][j])) {
                        for (String lexeme : firstMap.get(precedenceTable[0][j])) {
                            precedenceTable[i][lexemesMap.get(lexeme) + 1] = "<";
                        }
                    }
                }
            }
        }
    }

    private Iterable<String> getFirstPlus(String parent) {
        LinkedList<String> queue = new LinkedList<>();
        TreeSet<String> firsts = new TreeSet<>();
        queue.push(parent);
        String lexeme;
        while (!queue.isEmpty()) {
            lexeme = queue.removeFirst();
            for (String[] line : grammarMap.get(lexeme)) {
                if (!firsts.contains(line[0])) {
                    firsts.add(line[0]);
                    if (isTerminal(line[0])) {
                        queue.add(line[0]);
                    }
                }
            }
        }
        return firsts;
    }

    private void fillLess(String[][] precedenceTable
            , LinkedHashMap<String
            , Integer> lexemesMap
            , HashMap<String, Iterable<String>> firstMap
            , HashMap<String, Iterable<String>> lastMap) {
        for (int i = 1; i < precedenceTable.length; i++) {
            for (int j = 1; j < precedenceTable[i].length; j++) {
                if (precedenceTable[i][j] != null && precedenceTable[i][j].equals("=")) {
                    if (lastMap.containsKey(precedenceTable[i][0])) {
                        if (isTerminal(precedenceTable[0][j])) {
                            for (String lexeme : lastMap.get(precedenceTable[i][0])) {
                                for (String lexemeS : firstMap.get(precedenceTable[0][j])) {
                                    precedenceTable[lexemesMap.get(lexeme) + 1][lexemesMap.get(lexemeS) + 1] = ">";
                                }
                            }
                        } else {
                            for (String lexeme : lastMap.get(precedenceTable[i][0])) {
                                precedenceTable[lexemesMap.get(lexeme) + 1][j] = ">";
                            }
                        }
                    }
                }
            }
        }
    }

    private Iterable<String> getLastPlus(String parent) {
        LinkedList<String> queue = new LinkedList<>();
        TreeSet<String> lasts = new TreeSet<>();
        queue.push(parent);
        String lexeme;
        while (!queue.isEmpty()) {
            lexeme = queue.removeFirst();
            for (String[] line : grammarMap.get(lexeme)) {
                if (!lasts.contains(line[line.length - 1])) {
                    lasts.add(line[line.length - 1]);
                    if (isTerminal(line[line.length - 1])) {
                        queue.add(line[line.length - 1]);
                    }
                }
            }
        }
        return lasts;
    }

    private void fillSharp(String[][] precedenceTable) {
        for (int i = 1; i < precedenceTable.length - 1; i++) {
            precedenceTable[precedenceTable.length - 1][i] = "<";
            precedenceTable[i][precedenceTable.length - 1] = ">";
        }
    }

    private void fillEmpties(String[][] precedenceTable) {
        for (int i = 0; i < precedenceTable.length; i++) {
            for (int j = 0; j < precedenceTable[i].length; j++) {
                if (precedenceTable[i][j] == null) {
                    precedenceTable[i][j] = "-";
                }
            }
        }
    }

    public String[][] getPrecedenceTable() {
        LinkedHashMap<String, Integer> lexemesMap = getLexemesMap();
        String[][] precedenceTable = createPrecedenceTable(lexemesMap);
        HashMap<String, Iterable<String>> firstMap = getFirstsMap(lexemesMap);
        HashMap<String, Iterable<String>> lastMap = getLastMap(lexemesMap);
        fillEquals(precedenceTable, lexemesMap);
        fillMore(precedenceTable, lexemesMap, firstMap);
        fillLess(precedenceTable, lexemesMap, firstMap, lastMap);
        fillSharp(precedenceTable);
        fillEmpties(precedenceTable);
        return precedenceTable;
    }

    public static void main(String argv[]) throws FileNotFoundException {
        GrammarTree grammarTree = new GrammarTree("grammarTable.txt");
        String[][] table = grammarTree.getPrecedenceTable();
        System.out.println();
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                if (table[i][j] != null)
                    System.out.printf("%13s", table[i][j]);
                else
                    System.out.print("       |      ");
            }
            System.out.println();
        }
    }
}
