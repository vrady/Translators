package Analyzers;

import Models.Lexeme;
import Models.PolizExecution;

import javax.swing.*;
import java.util.*;

/**
 * Created by dimav on 02.05.2017.
 */
public class ReversePolishNotationExecution {
    private List<Lexeme> poliz;
    private Map<Lexeme, Integer> identificators;
    private Stack<Lexeme> stack;
    private String solvedPoliz;
    private Map<String, Double> ids;
    private ArrayList<PolizExecution> polizExecutions;

    public ArrayList<PolizExecution> getPolizExecutions() {
        return polizExecutions;
    }

    public Map<String, Double> getIds() {
        return ids;
    }

    public ReversePolishNotationExecution(ReversePolishNotationGenerator reversePolishNotationGenerator) {
        this.poliz = reversePolishNotationGenerator.getPoliz();
        this.identificators = reversePolishNotationGenerator.getLabels();
        this.stack = new Stack<>();
        this.ids = new HashMap<>();
        this.solvedPoliz = "";
        this.polizExecutions = new ArrayList<>();
    }

    public void runProgram() throws Exception {
        polizExecutions.add(new PolizExecution(getStackString(), getPolizString(0)));
        for (int i = 0; i < poliz.size(); i++) {
            if (poliz.get(i).getLex() == 37 || poliz.get(i).getLex() == 38) {
                if (poliz.get(i).getLex() == 38) {
                    poliz.get(i).setValue(Double.parseDouble(poliz.get(i).getLexName()));
                }
                stack.push(poliz.get(i));
            }
            if (doubleOperator(poliz.get(i))) {
                Lexeme tmp2 = stack.pop();
                Lexeme tmp1 = stack.pop();

                if (Objects.equals(poliz.get(i).getLexName(), "=")) {
                    solveDoubleOperator(tmp1, tmp2, poliz.get(i));
                } else stack.push(solveDoubleOperator(tmp1, tmp2, poliz.get(i)));
            }
            if (singleOperator(poliz.get(i))) {
                Lexeme tmp = stack.pop();
                tmp.setBooleanValue(!tmp.isBooleanValue());
                stack.push(tmp);
            }
            if (Objects.equals(poliz.get(i).getLexName(), "УПЛ")) {
                if (!stack.peek().isBooleanValue()) {
                    i = identificators.get(poliz.get(i - 1)) - 1;
                    stack.pop();
                    polizExecutions.add(new PolizExecution(getStackString(), getPolizString(i + 1)));
                    if (i >= poliz.size()) {
                        break;
                    }
                    continue;
                } else stack.pop();
            }
            if (Objects.equals(poliz.get(i).getLexName(), "БП")) {
                i = identificators.get(poliz.get(i - 1)) - 1;
                if (i >= poliz.size()) {
                    break;
                }
                continue;
            }
            if (Objects.equals(poliz.get(i).getLexName(), "Read")) {
                stack.peek().setValue(inputData(stack.peek()));
                ids.put(stack.peek().getLexName(), stack.peek().getValue());
                stack.pop();
            }
            if (Objects.equals(poliz.get(i).getLexName(), "Write")) {
                outputData(stack.pop());
            }
            polizExecutions.add(new PolizExecution(getStackString(), getPolizString(i + 1)));
        }
    }

    private void outputData(Lexeme lexeme) {
        solvedPoliz += ids.get(lexeme.getLexName()).toString() + "\n";
    }

    private Double inputData(Lexeme lexeme) {
        JFrame frame = new JFrame("Input var");
        try {
            return Double.parseDouble(JOptionPane.showInputDialog(frame, "Variable: " + lexeme.getLexName()));
        } catch (Exception e) {
            throw new IllegalArgumentException("Неправильно введене значення змінної");
        }
    }

    private Lexeme solveDoubleOperator(Lexeme first, Lexeme second, Lexeme operator) throws Exception {
        checkIds(first, second);
        if (operator.getLex() == 30) {
            ids.put(first.getLexName(), second.getValue());
            return second;
        }
        if (first.getLex() != 99 && first.getValue() == null) {
            throw new Exception("Змінна " + first.getLexName() + " не ініціалізована");
        }
        if (second.getLex() != 99 && second.getValue() == null) {
            throw new Exception("Змінна " + second.getLexName() + " не ініціалізована");
        }
        if (operator.getLex() == 17) { // or
            if (first.isBooleanValue() || second.isBooleanValue()) {
                return new Lexeme("solvedOr", true);
            } else return new Lexeme("solvedOr", false);
        }
        if (operator.getLex() == 18) { //and
            if (first.isBooleanValue() && second.isBooleanValue()) {
                return new Lexeme("solvedAnd", true);
            } else return new Lexeme("solvedAnd", false);
        }
        if (operator.getLex() == 24) {
            return new Lexeme("solvedSum", first.getValue() + second.getValue());
        }
        if (operator.getLex() == 25) {
            return new Lexeme("solvedMinus", first.getValue() - second.getValue());
        }
        if (operator.getLex() == 26) {
            return new Lexeme("solvedMul", first.getValue() * second.getValue());
        }
        if (operator.getLex() == 27) {
            return new Lexeme("solvedDiv", first.getValue() / second.getValue());
        }
        if (operator.getLex() == 31) {
            return new Lexeme("solvedLess", first.getValue() < second.getValue());
        }
        if (operator.getLex() == 32) {
            return new Lexeme("solvedMore", first.getValue() > second.getValue());
        }
        if (operator.getLex() == 33) {
            return new Lexeme("solvedLessEqual", first.getValue() <= second.getValue());
        }
        if (operator.getLex() == 34) {
            return new Lexeme("solvedMoreEqual", first.getValue() >= second.getValue());
        }
        if (operator.getLex() == 35) {
            return new Lexeme("solvedEqual", Objects.equals(first.getValue(), second.getValue()));
        }
        if (operator.getLex() == 36) {
            return new Lexeme("solvedNotEqual", !Objects.equals(first.getValue(), second.getValue()));
        }

        return null;
    }

    private void checkIds(Lexeme first, Lexeme second) {
        if (ids.containsKey(first.getLexName())) {
            first.setValue(ids.get(first.getLexName()));
        }
        if (ids.containsKey(second.getLexName())) {
            second.setValue(ids.get(second.getLexName()));
        }
    }

    private boolean doubleOperator(Lexeme lexeme) {
        return lexeme.getLex() == 17 || lexeme.getLex() == 18 || lexeme.getLex() == 24 || lexeme.getLex() == 25
                || lexeme.getLex() == 26 || lexeme.getLex() == 27 || lexeme.getLex() == 31 || lexeme.getLex() == 32
                || lexeme.getLex() == 33 || lexeme.getLex() == 34 || lexeme.getLex() == 35 || lexeme.getLex() == 36
                || lexeme.getLex() == 30;
    }

    private boolean singleOperator(Lexeme lexeme) {
        return lexeme.getLex() == 19;
    }

    public String getSolvedPoliz() {
        return solvedPoliz;
    }

    private String getStackString() {
        StringBuilder outputStack = new StringBuilder();
        for (Lexeme lexeme : stack) {
            outputStack.append(lexeme.getLexName()).append(" ");
        }
        return outputStack.toString();
    }

    private String getPolizString(int index) {
        StringBuilder outputPoliz = new StringBuilder();
        for (int i = index; i < poliz.size(); i++) {
            outputPoliz.append(poliz.get(i).getLexName()).append(" ");
        }
        return outputPoliz.toString();
    }

}
