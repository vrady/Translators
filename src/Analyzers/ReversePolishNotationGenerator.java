package Analyzers;

import Models.Lexeme;
import Models.PolizProcessing;

import java.util.*;

public class ReversePolishNotationGenerator {
    private List<Lexeme> lexemes;
    private LinkedList<Lexeme> poliz;
    private Map<Lexeme, Integer> labels;
    private Map<String, Integer> delimiters;
    private ArrayList<PolizProcessing> polizProcessing;

    public LinkedList<Lexeme> getPoliz() {
        return poliz;
    }

    public Map<Lexeme, Integer> getLabels() {
        return labels;
    }

    public ArrayList<PolizProcessing> getPolizProcessing() {
        return polizProcessing;
    }

    private Integer getPriority(Lexeme lexeme) {
        Integer priority = delimiters.get(lexeme.getLexName());

        if (lexeme.getCommonLexName() != null && lexeme.getCommonLexName().equals("label")) {
            return 0;
        }
        if (lexeme.getCommonLexName() != null && lexeme.getCommonLexName().equals("labelLoop")) {
            return 0;
        }

        if (priority == null) {
            throw new IllegalArgumentException("Немає обмежувача в таблиці");
        } else {
            return priority;
        }
    }

    {
        this.delimiters = new HashMap<>();
        delimiters.put("if", 0);
        delimiters.put("for", 0);
        delimiters.put("(", 0);
        delimiters.put("[", 1);

        delimiters.put("then", 1);
        delimiters.put("fi", 1);
        delimiters.put("by", 1);
        delimiters.put("to", 1);
        delimiters.put("do", 1);
        delimiters.put("rof", 1);
        delimiters.put(")", 1);
        delimiters.put("]", 2);
        delimiters.put("\n", 1);
        delimiters.put("¶", 1);
        delimiters.put("?", 2);
        delimiters.put(":", 2);
        delimiters.put(",", 1);

        delimiters.put("Read", 2);
        delimiters.put("Write", 2);

        delimiters.put("=", 3);
        delimiters.put("or", 4);
        delimiters.put("and", 5);
        delimiters.put("not", 6);

        delimiters.put("<", 7);
        delimiters.put(">", 7);
        delimiters.put("<=", 7);
        delimiters.put(">=", 7);
        delimiters.put("==", 7);
        delimiters.put("!=", 7);

        delimiters.put("+", 8);
        delimiters.put("-", 8);
        delimiters.put("*", 9);
        delimiters.put("/", 9);
    }

    public ReversePolishNotationGenerator(List<Lexeme> lexemes) {
        this.lexemes = lexemes;
        this.labels = new HashMap<>();
        this.polizProcessing = new ArrayList<>();
    }

    public String generatePoliz() {
        Stack<Lexeme> stack = new Stack<>();
        poliz = new LinkedList<>();
        int labelCounter = 0;
        int workLabelCounter = 0;
        int cycle = -1;
        Lexeme cycleParam = new Lexeme(0, "cycleParam");
//        Lexeme r = new Lexeme("r[0]");
//        r.setLex(37);
//        Lexeme r1 = new Lexeme("r[1]");
//        r1.setLex(37);
        Lexeme ternary = null;
        Lexeme ternaryOperation = null;
        boolean flag = false;
        boolean IO = false;

        for (Lexeme lexeme : lexemes) {
            if (lexeme.getLexCode() == 16) flag = false; // перевірка на тіло програми }
            if (flag) {
                polizProcessing.add(new PolizProcessing(lexeme.getLexName(), getStackString(stack), getPolizString(poliz)));
                if (lexeme.isConstant() || lexeme.isId()) {
                    poliz.add(lexeme);
                } else {
                    if (!(lexeme.getLexName().equals("(") || lexeme.getLexName().equals("if") || lexeme.getLexName().equals("for"))) {
                        Integer lexemePrior = getPriority(lexeme);
                        while (stack.size() > 0 && (getPriority(stack.peek()) >= lexemePrior)) {
                            Lexeme stackLexeme = stack.pop();
                            poliz.add(stackLexeme);
                        }
                        if (lexeme.getLexName().equals(",")) {
                            if (IO) {
                                Lexeme Write = new Lexeme("Write");
                                Write.setLex(6);
                                poliz.add(Write);
                            } else {
                                Lexeme Read = new Lexeme("Read");
                                Read.setLex(5);
                                poliz.add(Read);
                            }
                        }
                        if (lexeme.getLexName().equals(")") && stack.peek().getLexName().equals("(")) {
                            stack.pop();
                            if (stack.size() > 0 && stack.peek().getLexName().equals("Read")) {
                                Lexeme Read = new Lexeme("Read");
                                Read.setLex(5);
                                poliz.add(Read);
                                stack.pop();
                            }
                            if (stack.size() > 0 && stack.peek().getLexName().equals("Write")) {
                                Lexeme Write = new Lexeme("Write");
                                Write.setLex(6);
                                poliz.add(Write);
                                stack.pop();
                            }
                        }
                        // ЦИКЛ
                        if (lexeme.getLexName().equals("=") && stack.size() > 0 && stack.peek().getCommonLexName().equals("labelLoop")) {
                            if (cycle == 1) {
                                cycleParam = poliz.get(poliz.size() - 1);
                                cycle = 0;
                            }
                        }
                        if (lexeme.getLexName().equals("by")) {
//                            labels.put(r, null);
//                            labels.put(r1, null);
                            Lexeme r = new Lexeme("r[" + workLabelCounter + "]");
                            r.setLex(37);
                            workLabelCounter++;
                            Lexeme r1 = new Lexeme("r[" + workLabelCounter + "]");
                            r1.setLex(37);

                            labels.put(r, null);
                            labels.put(r1, null);

                            poliz.add(r);
                            Lexeme cycleParametr = new Lexeme("1");
                            cycleParametr.setLex(38);
                            poliz.add(cycleParametr);
                            Lexeme equal = new Lexeme("=");
                            equal.setLex(30);
                            poliz.add(equal);
                            Lexeme m0 = stack.peek();
                            labels.put(m0, poliz.size() + 2);
                            poliz.add(m0);
                            poliz.add(new Lexeme(":"));
                            poliz.add(r1);
                        }
                        if (lexeme.getLexName().equals("to")) {
                            Lexeme tmp = stack.pop();
                            Lexeme equal = new Lexeme("=");
                            equal.setLex(30);
                            poliz.add(equal);

                            workLabelCounter--;
                            Lexeme r = new Lexeme("r[" + workLabelCounter + "]");
                            r.setLex(37);
                            workLabelCounter++;
                            Lexeme r1 = new Lexeme("r[" + workLabelCounter + "]");
                            r1.setLex(37);

                            poliz.add(r);
                            Lexeme zero = new Lexeme("0");
                            zero.setLex(38);
                            poliz.add(zero);
                            Lexeme equalEqual = new Lexeme("==");
                            equalEqual.setLex(35);
                            poliz.add(equalEqual);
                            poliz.add(stack.peek());
                            poliz.add(new Lexeme("УПЛ"));
                            poliz.add(cycleParam);
                            poliz.add(cycleParam);
                            poliz.add(r1);
                            Lexeme plus = new Lexeme("+");
                            plus.setLex(24);
                            poliz.add(plus);
                            poliz.add(equal);
                            labels.put(stack.peek(), poliz.size() + 2);
                            poliz.add(stack.peek());
                            poliz.add(new Lexeme(":"));
                            poliz.add(r);
                            poliz.add(zero);
                            poliz.add(equal);
                            poliz.add(cycleParam);
                            stack.push(tmp);
                        }
                        if (lexeme.getLexName().equals("do")) {
                            Lexeme tmp1 = stack.pop();
                            Lexeme tmp2 = stack.pop();
                            Lexeme minus = new Lexeme("-");
                            minus.setLex(25);
                            poliz.add(minus);
                            Lexeme r1 = new Lexeme("r[" + workLabelCounter + "]");
                            r1.setLex(37);
                            poliz.add(r1);
                            workLabelCounter++;
                            Lexeme mul = new Lexeme("*");
                            mul.setLex(26);
                            poliz.add(mul);
                            Lexeme zero = new Lexeme("0");
                            zero.setLex(38);
                            poliz.add(zero);
                            Lexeme lessEqual = new Lexeme("<=");
                            lessEqual.setLex(33);
                            poliz.add(lessEqual);
                            poliz.add(stack.peek());
                            poliz.add(new Lexeme("УПЛ"));
                            stack.push(tmp2);
                            stack.push(tmp1);
                        }
                        if (lexeme.getLexName().equals("rof")) {
                            poliz.add(stack.peek());
                            poliz.add(new Lexeme("БП"));
                            stack.pop();
                            stack.pop();
                            labels.put(stack.peek(), poliz.size() + 2);
                            poliz.add(stack.pop());
                            poliz.add(new Lexeme(":"));
                            stack.pop();
                        }
                        if (lexeme.getLexName().equals("then") && stack.peek().getLexName().equals("if")) {
                            Lexeme labelUPL = new Lexeme("m[" + labelCounter + "]");
                            labelUPL.setCommonLexName("label");
                            labelUPL.setLex(100);
                            labels.put(labelUPL, null);
                            poliz.add(labelUPL);
                            poliz.add(new Lexeme("УПЛ"));
                            stack.push(labelUPL);
                            labelCounter++;
                        }
                        if (lexeme.getLexName().equals("[")) {
                            ternaryOperation = poliz.removeLast();
                            ternary = poliz.removeLast();
                        }
                        if (lexeme.getLexName().equals("?") && stack.peek().getLexName().equals("[")) {
                            Lexeme labelUPL = new Lexeme("m[" + labelCounter + "]");
                            labelUPL.setCommonLexName("label");
                            labelUPL.setLex(100);
                            labels.put(labelUPL, null);
                            poliz.add(labelUPL);
                            poliz.add(new Lexeme("УПЛ"));
                            poliz.add(ternary);
                            stack.push(labelUPL);
                            labelCounter++;
                        }
                        if (lexeme.getLexName().equals(":")) {
                            poliz.add(ternaryOperation);
                            Lexeme BP = new Lexeme("m[" + labelCounter + "]");
                            BP.setCommonLexName("label");
                            BP.setLex(100);
                            labels.put(BP, null);
                            poliz.add(BP);
                            poliz.add(new Lexeme("БП"));
                            Lexeme UPL = stack.pop();
                            labels.put(UPL, poliz.size() + 2);
                            poliz.add(UPL);
                            poliz.add(new Lexeme(":"));
                            poliz.add(ternary);
                            stack.push(BP);
                            labelCounter++;
                        }
                        if (lexeme.getLexName().equals("]")) {
                            poliz.add(ternaryOperation);
                            Lexeme BP = stack.pop();
                            labels.put(BP, poliz.size() + 2);
                            poliz.add(BP);
                            poliz.add(new Lexeme(":"));
                            stack.pop();
                        }
                        if ((lexeme.getLexName().equals("fi") && stack.size() > 0)) {
                            Lexeme UPL = stack.pop();
                            labels.put(UPL, poliz.size() + 2);
                            poliz.add(UPL);
                            poliz.add(new Lexeme(":"));
                            stack.pop();
                        }
                    }
                    if (!(lexeme.getLexName().equals(")") || lexeme.getLexName().equals("then") // слова які не заносяться в стек
                            || lexeme.getLexName().equals(":") || lexeme.getLexName().equals("]")
                            || lexeme.getLexName().equals("?") || lexeme.getLexName().equals(",")
                            || lexeme.getLexName().equals("fi") || lexeme.getLexName().equals("\n")
                            || lexeme.getLexName().equals("¶")
                            || lexeme.getLexName().equals("by") || lexeme.getLexName().equals("to")
                            || lexeme.getLexName().equals("do") || lexeme.getLexName().equals("rof"))) {
                        if (lexeme.getLexName().equals("Read")) IO = false;
                        if (lexeme.getLexName().equals("Write")) IO = true;
                        stack.push(lexeme);
                    }
                    if (lexeme.getLexName().equals("for")) {
//                        stack.push(new Lexeme("for"));

                        Lexeme m0 = new Lexeme("m[" + labelCounter + "]");
                        m0.setCommonLexName("labelLoop");
                        m0.setLex(100);
                        labels.put(m0, null);
                        labelCounter++;

                        Lexeme m1 = new Lexeme("m[" + labelCounter + "]");
                        m1.setCommonLexName("labelLoop");
                        m1.setLex(100);
                        labels.put(m1, null);
                        labelCounter++;

                        Lexeme m2 = new Lexeme("m[" + labelCounter + "]");
                        m2.setCommonLexName("labelLoop");
                        m2.setLex(100);
                        labels.put(m2, null);
                        labelCounter++;

                        stack.push(m2);
                        stack.push(m1);
                        stack.push(m0);
                        cycle = 1;
                    }
                }
            }
            if (lexeme.getLexCode() == 15) flag = true; // перевірка на тіло програми {
        }
        polizProcessing.add(new PolizProcessing("", getStackString(stack), getPolizString(poliz)));

        return getPolizString(poliz);
    }

    private String getStackString(Stack<Lexeme> stack) {
        StringBuilder outputStack = new StringBuilder();
        for (Lexeme lexeme : stack) {
            outputStack.append(lexeme.getLexName()).append(" ");
        }
        return outputStack.toString();
    }

    private String getPolizString(LinkedList<Lexeme> poliz) {
        StringBuilder outputPoliz = new StringBuilder();
        for (Lexeme lexeme : poliz) {
            outputPoliz.append(lexeme.getLexName()).append(" ");
        }
        return outputPoliz.toString();
    }
}
