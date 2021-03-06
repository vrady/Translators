package Analyzers;

import Models.Constants;
import Models.Identificators;
import Models.Lexeme;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.TreeSet;

/**
 * Created by dimav on 10/3/2016.
 */
public class LexicalAnalyzer {

    private ArrayList<Double> constants;
    private ArrayList<String> ids;
    private ArrayList<Lexeme> lexemes;
    private ArrayList<Identificators> identificators = new ArrayList<>();
    private ArrayList<Constants> constantses = new ArrayList<>();
    private String idState;
    private StringBuilder currentLexeme;
    public ArrayList<Constants> getConstantses() {
        return this.constantses;
    }
    public ArrayList<Identificators> getIdentificators() {
        return this.identificators;
    }
    public ArrayList<Double> getConstants() {
        return this.constants;
    }
    public ArrayList<String> getIds() {
        return this.ids;
    }
    public ArrayList<Lexeme> getLexemes() {
        return this.lexemes;
    }
    private int line;
    private int constantsCounter = 0;
    private int idCounter = 0;
    private boolean varmode;
    private final static HashMap<String, Integer> lexemesTable = new HashMap<>();

    static {
        lexemesTable.put("program", 1);
        lexemesTable.put("var", 2);
        lexemesTable.put("int", 3);
        lexemesTable.put("real", 4);
        lexemesTable.put("Read", 5);
        lexemesTable.put("Write", 6);
        lexemesTable.put("for", 7);
        lexemesTable.put("by", 8);
        lexemesTable.put("to", 9);
        lexemesTable.put("do", 10);
        lexemesTable.put("rof", 11);
        lexemesTable.put("if", 12);
        lexemesTable.put("then", 13);
        lexemesTable.put("fi", 14);
        lexemesTable.put("{", 15);
        lexemesTable.put("}", 16);
        lexemesTable.put("or", 17);
        lexemesTable.put("and", 18);
        lexemesTable.put("not", 19);
        lexemesTable.put("\n", 20);
        lexemesTable.put(",", 21);
        lexemesTable.put("?", 22);
        lexemesTable.put(":", 23);
        lexemesTable.put("+", 24);
        lexemesTable.put("-", 25);
        lexemesTable.put("*", 26);
        lexemesTable.put("/", 27);
        lexemesTable.put("(", 28);
        lexemesTable.put(")", 29);
        lexemesTable.put("=", 30);
        lexemesTable.put("<", 31);
        lexemesTable.put(">", 32);
        lexemesTable.put("<=", 33);
        lexemesTable.put(">=", 34);
        lexemesTable.put("==", 35);
        lexemesTable.put("!=", 36);
        lexemesTable.put("idn", 37);
        lexemesTable.put("con", 38);
        lexemesTable.put("[", 39);
        lexemesTable.put("]", 40);
    }

    public final static String[] lexemesArray = new String[41];

    static {
        lexemesArray[0] = null;
        lexemesArray[1] = "program";
        lexemesArray[2] = "var";
        lexemesArray[3] = "int";
        lexemesArray[4] = "real";
        lexemesArray[5] = "Read";
        lexemesArray[6] = "Write";
        lexemesArray[7] = "for";
        lexemesArray[8] = "by";
        lexemesArray[9] = "to";
        lexemesArray[10] = "do";
        lexemesArray[11] = "rof";
        lexemesArray[12] = "if";
        lexemesArray[13] = "then";
        lexemesArray[14] = "fi";
        lexemesArray[15] = "{";
        lexemesArray[16] = "}";
        lexemesArray[17] = "or";
        lexemesArray[18] = "and";
        lexemesArray[19] = "not";
        lexemesArray[20] = "¶";
        lexemesArray[21] = ",";
        lexemesArray[22] = "?";
        lexemesArray[23] = ":";
        lexemesArray[24] = "+";
        lexemesArray[25] = "-";
        lexemesArray[26] = "*";
        lexemesArray[27] = "/";
        lexemesArray[28] = "(";
        lexemesArray[29] = ")";
        lexemesArray[30] = "=";
        lexemesArray[31] = "<";
        lexemesArray[32] = ">";
        lexemesArray[33] = "<=";
        lexemesArray[34] = ">=";
        lexemesArray[35] = "==";
        lexemesArray[36] = "!=";
        lexemesArray[37] = "idn";
        lexemesArray[38] = "con";
        lexemesArray[39] = "[";
        lexemesArray[40] = "]";
    }

    private static TreeSet<Character> delimitationTable = new TreeSet<>();

    static {
        delimitationTable.add('+');
        delimitationTable.add('-');
        delimitationTable.add(',');
        delimitationTable.add('(');
        delimitationTable.add(')');
        delimitationTable.add('{');
        delimitationTable.add('}');
        delimitationTable.add('*');
        delimitationTable.add('/');
        delimitationTable.add('?');
        delimitationTable.add(':');
        delimitationTable.add('=');
        delimitationTable.add(' ');
        delimitationTable.add('\n');
        delimitationTable.add('¶');
        delimitationTable.add('[');
        delimitationTable.add(']');
    }

    private boolean isDelimiter(char l) {
        return delimitationTable.contains(l);
    }

    private boolean isCurrentCharacter(char l) {
        return l >= 'A' && l <= 'z' || l == '_';
    }

    private void addNumber() {
        String lexeme = currentLexeme.toString();
        double num = Double.parseDouble(lexeme);
        if (!constants.contains(num)) {
            constants.add(num);
            constantsCounter++;
            constantses.add(new Constants(constantsCounter,num));
        }
        lexemes.add(new Lexeme(this.line, 38, Double.toString(num), "con", constants.lastIndexOf(num), -1));
        currentLexeme = new StringBuilder();
    }

    private void addVar() {
        String lexeme = currentLexeme.toString();
        if (lexeme.equals("{")) {
            varmode = false;
            addNewLexem();
            return;
        }
        if (lexeme.equals("int")) {
            idState = "int";
            addNewLexem();
            return;
        }
        if (lexeme.equals("real")) {
            idState = "real";
            addNewLexem();
            return;
        }
        if (lexemesTable.containsKey(lexeme)) {
            throw new IllegalArgumentException("A variable can not be a reserved word\nNumber of line: " + this.line);
        }
        if (ids.contains(lexeme)) {
            throw new IllegalArgumentException("A variable can not be declared twice\nNumber of line: " + this.line);
        }
        idCounter++;
        identificators.add(new Identificators(idCounter, idState, lexeme));
        ids.add(lexeme);
        lexemes.add(new Lexeme(this.line, 37, lexeme, "idn", -1, ids.indexOf(lexeme)));
        currentLexeme = new StringBuilder();
    }

    private void addNewLexem() {
        String lexeme = currentLexeme.toString();
        if (lexeme.equals("var")) {
            varmode = true;
        }
        if (lexemes.size() != 0 && (Objects.equals(lexemes.get(lexemes.size() - 1).getLex(), lexemesTable.get("program")))) {
            idState = "program";
            addVar();
            return;
        }
        if (lexemesTable.containsKey(lexeme)) {
            lexemes.add(new Lexeme(this.line, lexemesTable.get(lexeme), lexeme, lexeme, -1, -1));
            currentLexeme = new StringBuilder();
            return;
        }
        if (ids.contains(lexeme)) {
            lexemes.add(new Lexeme(this.line, 37, lexeme, "idn", -1, ids.indexOf(lexeme)));
            currentLexeme = new StringBuilder();
            return;
        }
        throw new IllegalArgumentException("Unknown lexeme: " + lexeme + "\nNumber of line: " + this.line);
    }

    public LexicalAnalyzer(String text) {
        if (text == null) {
            throw new NullPointerException();
        }
        constants = new ArrayList<>();
        ids = new ArrayList<>();
        lexemes = new ArrayList<>();
        varmode = false;
        this.line = 1;
        analyze(text);
    }

    private void analyze(String text) {
        currentLexeme = new StringBuilder();
        int mode = 1;
        for (int i = 0; i < text.length(); i++) {
            mode = getMode(mode, text.charAt(i));
        }
    }

    private int getMode(int mode, char ch) {
        switch (mode) {
            case 1: {
                return mode1(ch);
            }
            case 2: {
                return mode2(ch);
            }
            case 3: {
                return mode3(ch);
            }
            case 4: {
                return mode4(ch);
            }
            case 5: {
                return mode5(ch);
            }
            case 6: {
                return mode6(ch);
            }
            case 7: {
                return mode7(ch);
            }
            case 8: {
                return mode8(ch);
            }
            case 9: {
                return mode9(ch);
            }
            case 10: {
                return mode10(ch);
            }
        }
        return -1;
    }

    private int mode1(char l) {
        if (isCurrentCharacter(l)) {
            currentLexeme.append(l);
            return 2;
        }
        if (l == '+' || l == '-') {
            currentLexeme.append(l);
            return 3;
        }
        if (Character.isDigit(l)) {
            currentLexeme.append(l);
            return 4;
        }
        if (l == '.') {
            currentLexeme.append(l);
            return 6;
        }
        if (l == '<') {
            return 7;
        }
        if (l == '>') {
            return 8;
        }
        if (l == '=') {
            return 9;
        }
        if (l == '!') {
            return 10;
        }
        if (isDelimiter(l)) {
            if (l != ' ' && l != '\n') {
                if (l == '{') {
                    varmode = false;
                }
                lexemes.add(new Lexeme(this.line, lexemesTable.get(Character.toString(l)), Character.toString(l), Character.toString(l), -1, -1));
            } else if (l == '\n') {
                lexemes.add(new Lexeme(this.line, lexemesTable.get(Character.toString(l)), Character.toString(l), Character.toString(l), -1, -1));
                this.line++;
            }
            return 1;
        }
        throw new IllegalArgumentException("Unknown symbol: " + l + "\nNumber of line: " + this.line);
    }

    private int mode2(char l) {
        if (isCurrentCharacter(l) || Character.isDigit(l)) {
            currentLexeme.append(l);
            return 2;
        }
        if (varmode) {
            addVar();
        } else {
            addNewLexem();
        }
        return mode1(l);
    }

    private int mode3(char l) {
        if (Character.isDigit(l) && (!lexemes.get(lexemes.size()-1).isExprFinish())) {
            currentLexeme.append(l);
            return 4;
        }
        if (l == '.') {
            currentLexeme.append(l);
            return 5;
        }
        addNewLexem();
        return mode1(l);
    }

    private int mode4(char l) {
        if (Character.isDigit(l)) {
            currentLexeme.append(l);
            return 4;
        }
        if (l == '.') {
            currentLexeme.append(l);
            return 5;
        }
        if (varmode) {
            throw new IllegalArgumentException("A variable can not start with a digit\nNumber of line: " + this.line);
        } else {
            addNumber();
        }
        return mode1(l);
    }

    private int mode5(char l) {
        if (Character.isDigit(l)) {
            currentLexeme.append(l);
            return 5;
        }
        addNumber();
        return mode1(l);
    }

    private int mode6(char l) {
        if (Character.isDigit(l)) {
            currentLexeme.append(l);
            return 5;
        } else {
            throw new IllegalArgumentException("The point is not the delimiter\nNumber of line: " + this.line);
        }
    }

    private int mode7(char l) {
        if (l == '=') {
            lexemes.add(new Lexeme(this.line, lexemesTable.get("<="), "<=", "<=", -1, -1));
            return 1;
        }
        lexemes.add(new Lexeme(this.line, lexemesTable.get("<"), "<", "<", -1, -1));
        return mode1(l);
    }

    private int mode8(char l) {
        if (l == '=') {
            lexemes.add(new Lexeme(this.line, lexemesTable.get(">="), ">=", ">=", -1, -1));
            return 1;
        }
        lexemes.add(new Lexeme(this.line, lexemesTable.get(">"), ">", ">", -1, -1));
        return mode1(l);
    }

    private int mode9(char l) {
        if (l == '=') {
            lexemes.add(new Lexeme(this.line, lexemesTable.get("=="), "==", "==", -1, -1));
            return 1;
        }
        lexemes.add(new Lexeme(this.line, lexemesTable.get("="), "=", "=", -1, -1));
        return mode1(l);
    }

    private int mode10(char l) {
        if (l == '=') {
            lexemes.add(new Lexeme(this.line, lexemesTable.get("!="), "!=", "!=", -1, -1));
            return 1;
        } else {
            throw new IllegalArgumentException("! is not the delimiter\nNumber of line: " + this.line);
        }
    }

    static String readFile(){
        String file = null;
        try (InputStream stream = new FileInputStream("C:\\Users\\dimav\\Documents\\Translators\\src\\input1.txt")) {
            byte[] b = new byte[stream.available()];
            stream.read(b);
            file = new String(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}