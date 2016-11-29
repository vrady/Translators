package Analyzers;

import Models.Lexeme;
import Models.PDALexeme;
import Models.PDARules;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Created by vrady on 03.11.16.
 */
public class SyntaxAnalyzerPDA {

    private ArrayList<Lexeme> lexemes;
    private ArrayList<Double> listConstants;
    private ArrayList<String> listIds;
    private int index;
    private LinkedList<Integer> stack;
    private ArrayList<PDALexeme> pdaLexemes;
    private ArrayList<PDARules> pdaRules;

    public SyntaxAnalyzerPDA(LexicalAnalyzer analyzer) {
        this.lexemes = analyzer.getLexemes();
        this.listConstants = analyzer.getConstants();
        this.listIds = analyzer.getIds();
        this.index = 0;
        this.pdaRules = new ArrayList<>();
        this.pdaLexemes = new ArrayList<>();
        this.stack = new LinkedList<>();

        initRules(pdaRules);
        analyze();
    }

    private void analyze() {
        int mode = 1;
        for (index = 0; index < lexemes.size(); index++) {
            mode = getState(mode, stack);
            if (lexemes.get(lexemes.size() - 1).getLex() != 16) {
                throw new IllegalArgumentException("Excepted '}' at end of the file");
            }
        }
    }

    private int getState(int state, LinkedList<Integer> stack) {
        for (PDARules pdaRule : pdaRules) {
            if (Objects.equals(pdaRule.getLexCode(), lexemes.get(index).getLex())) {
                if (pdaRule.getAlpha() == state) {

                    if (pdaRule.getBeta() != null) {
                        if (pdaRule.getStack() == null) {
                            pdaLexemes.add(new PDALexeme(index, getLexeme(), state, getStack()));
                            return pdaRule.getBeta();
                        } else {
                            stack.push(pdaRule.getStack());
                            pdaLexemes.add(new PDALexeme(index, getLexeme(), state, getStack()));
                            return pdaRule.getBeta();
                        }
                    } else {
                        pdaLexemes.add(new PDALexeme(index, getLexeme(), state, getStack()));
                        if (state == 8) {
                            stack.push(pdaRule.getSemantStack());
                            return pdaRule.getSemantBeta();
                        } else return stack.pop();
                    }

                }
            }

        }
        for (PDARules pdaRule : pdaRules) {
            if (pdaRule.getAlpha() == state) {
                if (pdaRule.getSemantBeta() != null) {
                    if (pdaRule.getSemantBeta() == -1) {
                        pdaLexemes.add(new PDALexeme(index, getLexeme(), state, getStack()));
                        return getState(stack.pop(), stack);
                    }
                    stack.push(pdaRule.getSemantStack());
                    pdaLexemes.add(new PDALexeme(index, getLexeme(), state, getStack()));
                    return getState(pdaRule.getSemantBeta(), stack);
                } else {
                    throw new IllegalArgumentException("Exception " + pdaRule.getSemant()
                            + " at row: " + lexemes.get(index).getLine());
                }
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        String file = LexicalAnalyzer.readFile();
        LexicalAnalyzer analyzer = new LexicalAnalyzer(file);
        new SyntaxAnalyzerPDA(analyzer);
    }

    public ArrayList<PDALexeme> getPdaLexemes() {
        return pdaLexemes;
    }

    private String getLexeme() {
        if (lexemes.get(index).getLex() == 37) {
            return listIds.get(lexemes.get(index).getId());
        }
        if (lexemes.get(index).getLex() == 38) {
            return "" + listConstants.get(lexemes.get(index).getCon());
        }
        return LexicalAnalyzer.lexemesArray[lexemes.get(index).getLex()];
    }

    private String getStack() {
        String string = "";
        for (Integer el : stack) {
            string += " " + el;
        }
        return string;
    }

    private void initRules(ArrayList<PDARules> pdaRules) {
        pdaRules.add(new PDARules(1, "program", 2, null, "[!=]error: Expected program", "", 1, null, null));
        pdaRules.add(new PDARules(2, "IDN", 3, null, "[!=]error Expected IDN after program", "", 37, null, null));
        pdaRules.add(new PDARules(3, "var", 4, null, "[!=]error Expected var", "", 2, null, null));
        pdaRules.add(new PDARules(4, "int", 5, null, "[!=]error Expected int or real", "", 3, null, null));
        pdaRules.add(new PDARules(4, "real", 5, null, "[!=]error Expected int or real", "", 4, null, null));
        pdaRules.add(new PDARules(5, "IDN", 6, null, "[!=]error Expected IDN after type", "", 37, null, null));
        pdaRules.add(new PDARules(6, ",", 5, null, "[!=]error Expected , or enter or {", "", 21, null, null));
        pdaRules.add(new PDARules(6, "¶", 4, null, "[!=]error Expected , or enter or {", "", 20, null, null));
        pdaRules.add(new PDARules(6, "{", 101, 7, "[!=]error Expected , or enter or {", "", 15, null, null));
        pdaRules.add(new PDARules(7, "¶", 8, null, "[!=]error", "Expected enter", 20, null, null));
        pdaRules.add(new PDARules(8, "}", null, null, "[!=]п/а опер 101 ↓7", "[=]вихід", 16, 101, 7));

        pdaRules.add(new PDARules(101, "Write", 102, null, "[!=]error Expected operator", "", 6, null, null));
        pdaRules.add(new PDARules(101, "Read", 102, null, "[!=]error Expected operator", "", 5, null, null));
        pdaRules.add(new PDARules(101, "for", 105, null, "[!=]error Expected operator", "", 7, null, null));
        pdaRules.add(new PDARules(101, "if", 301, 111, "[!=]error Expected operator", "", 12, null, null));
        pdaRules.add(new PDARules(101, "IDN", 114, null, "[!=]error Expected operator", "", 37, null, null));
        pdaRules.add(new PDARules(102, "(", 103, null, "[!=]error Expected (", "", 28, null, null));
        pdaRules.add(new PDARules(103, "IDN", 104, null, "[!=]error Expected IDN after (", "", 37, null, null));
        pdaRules.add(new PDARules(104, ",", 103, null, "[!=]error Expected , or ) after IDN", "вихід", 21, null, null));
        pdaRules.add(new PDARules(104, ")", null, null, "[!=]error Expected , or ) after IDN", "вихід", 29, null, null));
        pdaRules.add(new PDARules(105, "IDN", 106, null, "[!=]error Expected IDN after FOR", "", 37, null, null));
        pdaRules.add(new PDARules(106, "=", 201, 107, "[!=]error Expected = after IDN in FOR", "", 30, null, null));
        pdaRules.add(new PDARules(107, "by", 201, 108, "[!=]error Expected by in FOR", "", 8, null, null));
        pdaRules.add(new PDARules(108, "to", 201, 109, "[!=]error Expected to in FOR", "", 9, null, null));
        pdaRules.add(new PDARules(109, "do", 101, 110, "[!=]error Expected do in FOR", "", 10, null, null));
        pdaRules.add(new PDARules(110, "rof", null, null, "[!=]error Expected rof in FOR", "вихід", 11, null, null));
        pdaRules.add(new PDARules(111, "then", 101, 112, "[!=]error Expected then in IF", "", 13, null, null));
        pdaRules.add(new PDARules(112, "¶", 113, null, "[!=]error Expected enter in IF", "", 20, null, null));
        pdaRules.add(new PDARules(113, "fi", null, null, "[!=]п/а опер 101 ↓112", "вихід", 14, 101, 112));
        pdaRules.add(new PDARules(114, "=", 115, null, "[!=]error Expected = after IDN", "", 30, null, null));
        pdaRules.add(new PDARules(115, "{", 301, 116, "[!=]error Expected { after = in Ternary", "", 15, 201, 119));
        pdaRules.add(new PDARules(116, "?", 201, 117, "[!=]error Expected ? after logical expression in Ternary", "", 22, null, null));
        pdaRules.add(new PDARules(117, ":", 201, 118, "[!=]error Expected : after expression in Ternary", "", 23, null, null));
        pdaRules.add(new PDARules(118, "}", null, null, "[!=]error Expected } after expression in Ternary", "вихід", 16, null, null));
        pdaRules.add(new PDARules(119, "any lexeme", null, null, "EXIT", "", null, -1, null));

        pdaRules.add(new PDARules(201, "IDN", 202, null, "[!=]error Expected IDN or CON or ( in Expression", "", 37, null, null));
        pdaRules.add(new PDARules(201, "CON", 202, null, "[!=]error Expected IDN or CON or ( in Expression", "", 38, null, null));
        pdaRules.add(new PDARules(201, "(", 201, 203, "[!=]error Expected IDN or CON or ( in Expression", "", 28, null, null));
        pdaRules.add(new PDARules(202, "+", 201, null, "[!=]error Expected +-*/", "", 24, -1, null));
        pdaRules.add(new PDARules(202, "-", 201, null, "[!=]error Expected +-*/", "", 25, -1, null));
        pdaRules.add(new PDARules(202, "*", 201, null, "[!=]error Expected +-*/", "", 26, -1, null));
        pdaRules.add(new PDARules(202, "/", 201, null, "[!=]error Expected +-*/", "", 27, -1, null));
        pdaRules.add(new PDARules(203, ")", 202, null, "[!=]error Expected ) after IDN or CON", "", 29, null, null));

        pdaRules.add(new PDARules(301, "not", 301, null, "[!=]п/а вираз 201 ↓302", "", 19, 201, 302));
        pdaRules.add(new PDARules(301, "(", 301, 304, "[!=]п/а лог.в 301 ↓304", "", 28, 201, 302));
        pdaRules.add(new PDARules(302, "<", 201, 303, "[!=]error Expected < > != == <= >=", "", 31, null, null));
        pdaRules.add(new PDARules(302, ">", 201, 303, "[!=]error Expected < > != == <= >=", "", 32, null, null));
        pdaRules.add(new PDARules(302, "!=", 201, 303, "[!=]error Expected < > != == <= >=", "", 36, null, null));
        pdaRules.add(new PDARules(302, "==", 201, 303, "[!=]error Expected < > != == <= >=", "", 35, null, null));
        pdaRules.add(new PDARules(302, "<=", 201, 303, "[!=]error Expected < > != == <= >=", "", 33, null, null));
        pdaRules.add(new PDARules(302, ">=", 201, 303, "[!=]error Expected < > != == <= >=", "", 34, null, null));
        pdaRules.add(new PDARules(303, "and", 301, null, "[!=]вихід", "", 18, -1, null));
        pdaRules.add(new PDARules(303, "or", 301, null, "[!=]вихід", "", 17, -1, null));
        pdaRules.add(new PDARules(304, ")", 303, null, "[!=]error Expected ) after Expression in LogicalExpression", "", 29, null, null));
    }
}
