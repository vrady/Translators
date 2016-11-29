package Analyzers;

import Models.Errors;
import Models.Lexeme;

import java.util.ArrayList;

/**
 * Created by vrady on 27.10.16.
 */
public class SyntaxAnalyzer {

    private static ArrayList<Lexeme> lexemes;

    private ArrayList<Errors> errors;

    private Integer lexNumber;

    ArrayList<Errors> getErrors() {
        return errors;
    }

    SyntaxAnalyzer(LexicalAnalyzer analyzer) {
        lexemes = analyzer.getLexemes();
        errors = new ArrayList<>();
        lexNumber = 0;

        program();
    }

    private boolean outOfRange() {
        return lexNumber < lexemes.size();
    }

    private int getLexCode() {
        return lexemes.get(lexNumber).getLex();
    }

    private int getLexLine() {
        return lexemes.get(lexNumber).getLine();
    }

    private boolean program() {
        if (outOfRange() && getLexCode() == 1) { // program
            lexNumber++;
            if (outOfRange() && getLexCode() == 37) { //IDN
                lexNumber++;
                if (outOfRange() && getLexCode() == 20) lexNumber++;
                if (outOfRange() && getLexCode() == 2) {
                    lexNumber++;
                    if (declarationList()) {
                        if (outOfRange() && getLexCode() == 15) { // {
                            lexNumber++;
                            if (operatorList()) { //
                                if (outOfRange() && getLexCode() == 16) { // }
                                    lexNumber++;
                                    System.out.println("Помилок немає");
                                    return true;
                                } else {
                                    System.out.println("Рядок " + getLexLine() + ": відсутні закриваючі дужки } program()");
                                    errors.add(new Errors(getLexLine(), "відсутні закриваючі дужки } program()"));
                                    return false;
                                }
                            } else {
                                System.out.println("Рядок " + getLexLine() + ": невірний список операторів program()");
                                errors.add(new Errors(getLexLine(), "невірний список операторів program()"));
                                return false;
                            }
                        } else {
                            System.out.println("Рядок " + getLexLine() + ": відсутні відкриваючі дужки { program()");
                            errors.add(new Errors(getLexLine(), "відсутні відкриваючі дужки { program()"));
                            return false;
                        }
                    } else {
                        System.out.println("Рядок " + getLexLine() + ": невірний список оголошення program()");
                        errors.add(new Errors(getLexLine(), "невірний список оголошення program()"));
                        return false;
                    }
                } else {
                    System.out.println("Рядок " + getLexLine() + ": немає var program()");
                    errors.add(new Errors(getLexLine(), "немає var program()"));
                    return false;
                }
            } else {
                System.out.println("Рядок " + getLexLine() + ": немає назви програми program()");
                errors.add(new Errors(getLexLine(), "немає назви програми program()"));
                return false;
            }
        } else {
            System.out.println("Рядок " + getLexLine() + ": програма має починатись зі слова program program()");
            errors.add(new Errors(getLexLine(), "програма має починатись зі слова program program()"));
            return false;
        }
    }

    private boolean declarationList() {
        if (variableType()) {
            if (idList()) {
                while (outOfRange() && (getLexCode() == 20)) {
                    lexNumber++;
                    if (variableType()) {
                        if (!idList()) {
                            System.out.println("Рядок " + getLexLine() + ": оголосити список ід declarationList()");
                            errors.add(new Errors(getLexLine(), "оголосити список ід declarationList()"));
                            return false;
                        }
                    } else {
                        System.out.println("Рядок " + getLexLine() + ": немає типу declarationList()");
                        errors.add(new Errors(getLexLine(), "немає типу declarationList()"));
                        return false;
                    }
                }
                return true;
            } else {
                System.out.println("Рядок " + getLexLine() + ": оголосити список ід declarationList()");
                errors.add(new Errors(getLexLine(), "оголосити список ід declarationList()"));
                return false;
            }
        } else {
            System.out.println("Рядок " + getLexLine() + ": відсутній тип declarationList()");
            errors.add(new Errors(getLexLine(), "відсутній тип declarationList()"));
            return false;
        }
    }

    private boolean variableType() {
        if (outOfRange() && (getLexCode() == 3 || getLexCode() == 4)) {
            lexNumber++;
            return true;
        } else {
            System.out.println("Рядок " + getLexLine() + ": тип може бути int або real variableType()");
            errors.add(new Errors(getLexLine(), "тип може бути int або real variableType()"));
            return false;
        }
    }

    private boolean idList() {
        if (outOfRange() && getLexCode() == 37) {
            lexNumber++;
            while (outOfRange() && getLexCode() == 21) {
                lexNumber++;
                if (outOfRange() && getLexCode() == 37) {
                    lexNumber++;
                } else {
                    System.out.println("Рядок " + getLexLine() + ": не ідентифікатор idList()");
                    errors.add(new Errors(getLexLine(), "не ідентифікатор idList()"));
                    return false;
                }
            }
            return true;
        } else {
            System.out.println("Рядок " + getLexLine() + ": має бути список ідентифікаторів idList()");
            errors.add(new Errors(getLexLine(), "має бути список ідентифікаторів idList()"));
            return false;
        }
    }

    private boolean operatorList() { // переход на нову строчку працює, розділяє оператори
        if (outOfRange() && getLexCode() == 16) { // }
            System.out.println("Рядок " + getLexLine() + ": пустий список операторів operatorList()");
            errors.add(new Errors(getLexLine(), "пустий список операторів operatorList()"));
            return false;
        } else if (operator()) {
            if (outOfRange() && getLexCode() == 20) { // переход на нову строку
                lexNumber++;
                if (outOfRange() && getLexCode() == 14) { //fi
                    return true;
                }
                while (outOfRange() && getLexCode() != 16) {
                    if (operator()) {
                        if (outOfRange() && getLexCode() == 20) {
                            lexNumber++;
                            if (outOfRange() && getLexCode() == 14) {
                                return true;
                            }
                        } else {
                            System.out.println("Рядок " + getLexLine() + ": немає переходу на новий рядок operatorList()");
                            errors.add(new Errors(getLexLine(), "немає переходу на новий рядок operatorList()"));
                            return false;
                        }
                    } else {
                        System.out.println("Рядок " + getLexLine() + ": невірний оператор operatorList()");
                        errors.add(new Errors(getLexLine(), "невірний оператор operatorList()"));
                        return false;
                    }
                }
                return true;
            } else {
                System.out.println("Рядок " + getLexLine() + ": немає переходу на новий рядок operatorList()");
                errors.add(new Errors(getLexLine(), "немає переходу на новий рядок operatorList()"));
                return false;
            }
        } else {
            System.out.println("Рядок " + getLexLine() + ": невірний оператор operatorList()");
            errors.add(new Errors(getLexLine(), "невірний оператор operatorList()"));
            return false;
        }
    }

    private boolean operator() {
//        ОПЕРАТОР ПРИСВОЄННЯ І ТЕРНАРНИЙ ++++++++++++++++++++++
        if (outOfRange() && getLexCode() == 37) { // IDN
            lexNumber++;
            if (outOfRange() && getLexCode() == 30) { // =
                lexNumber++;
                int i = 0;
                boolean state = false;
                while (outOfRange() && getLexCode() != 20) {
                    if (getLexCode() == 22) {
                        state = true;
                        break;
                    } else state = false;
                    i++;
                    lexNumber++;
                }
                lexNumber -= i;
                if (!state) {
                    if (expression()) {
                        return true;
                    } else {
                        System.out.println("Рядок " + getLexLine() + ": має бути вираз operator()");
                        errors.add(new Errors(getLexLine(), ": має бути вираз operator()"));
                        return false;
                    }
                } else if (logicalExpression()) {
                    if (outOfRange() && getLexCode() == 22) {
                        lexNumber++;
                        if (expression()) {
                            if (outOfRange() && getLexCode() == 23) {
                                lexNumber++;
                                if (expression()) {
                                    return true;
                                } else {
                                    System.out.println("Рядок " + getLexLine() + ": тернарний оператор має містити останній вираз operator()");
                                    errors.add(new Errors(getLexLine(), ": тернарний оператор має містити останній вираз operator()"));
                                    return false;
                                }
                            } else {
                                System.out.println("Рядок " + getLexLine() + ": тернарний оператор має містить : між виразами operator()");
                                errors.add(new Errors(getLexLine(), ": тернарний оператор має містить : між виразами operator()"));
                                return false;
                            }
                        } else {
                            System.out.println("Рядок " + getLexLine() + ": тернарний оператор має містити перший вираз operator()");
                            errors.add(new Errors(getLexLine(), ": тернарний оператор має містити перший вираз operator()"));
                            return false;
                        }
                    } else {
                        System.out.println("Рядок " + getLexLine() + ": тернарний оператор має містить ? перед виразами operator()");
                        errors.add(new Errors(getLexLine(), ": тернарний оператор має містить ? перед виразами operator()"));
                        return false;
                    }
                }
            } else {
                System.out.println("Рядок " + getLexLine() + ": оператор присвоєння(має бути =) operator()");
                errors.add(new Errors(getLexLine(), ": оператор присвоєння(має бути =) operator()"));
                return false;
            }
//            ОПЕРАТОР ВВОДУ ВИВОДУ ++++++++++
        } else if (outOfRange() && (getLexCode() == 5 || getLexCode() == 6)) { // Read Write
            lexNumber++;
            if (outOfRange() && getLexCode() == 28) { // (
                lexNumber++;
                idList();
                if (outOfRange() && getLexCode() == 29) { // )
                    lexNumber++;
                    return true;
                } else {
                    System.out.println("Рядок " + getLexLine() + ": оператор Read Write(має бути закриваюча дужка) operator()");
                    errors.add(new Errors(getLexLine(), ": оператор Read Write(має бути закриваюча дужка) operator()"));
                    return false;
                }
            } else {
                System.out.println("Рядок " + getLexLine() + ": оператор Read Write(має бути відкриваюча дужка) operator()");
                errors.add(new Errors(getLexLine(), ": оператор Read Write(має бути відкриваюча дужка) operator()"));
                return false;
            }
//            ОПЕРАТОР ЦИКЛУ +++++++++++
        } else if (outOfRange() && getLexCode() == 7) { //for
            lexNumber++;
            if (outOfRange() && getLexCode() == 37) { // IDN
                lexNumber++;
                if (outOfRange() && getLexCode() == 30) { // =
                    lexNumber++;
                    if (expression()) {
                        if (outOfRange() && getLexCode() == 8) { // by
                            lexNumber++;
                            if (expression()) {
                                if (outOfRange() && getLexCode() == 9) { //to
                                    lexNumber++;
                                    if (expression()) {
                                        if (outOfRange() && getLexCode() == 10) { //do
                                            lexNumber++;
                                            if (operator()) {
                                                if (outOfRange() && getLexCode() == 11) { //rof
                                                    lexNumber++;
                                                    return true;
                                                } else {
                                                    System.out.println("Рядок " + getLexLine() + ": цикл має закінчуватись на rof operator()");
                                                    errors.add(new Errors(getLexLine(), ": цикл має закінчуватись на rof operator()"));
                                                    return false;
                                                }
                                            } else {
                                                System.out.println("Рядок " + getLexLine() + ": цикл має містити оператор після do operator()");
                                                errors.add(new Errors(getLexLine(), ": цикл має містити оператор після do operator()"));
                                                return false;
                                            }
                                        } else {
                                            System.out.println("Рядок " + getLexLine() + ": цикл має містити do operator()");
                                            errors.add(new Errors(getLexLine(), ": цикл має містити do operator()"));
                                            return false;
                                        }
                                    } else {
                                        System.out.println("Рядок " + getLexLine() + ": цикл має містити вираз після після to operator()");
                                        errors.add(new Errors(getLexLine(), ": цикл має містити вираз після після to operator()"));
                                        return false;
                                    }
                                } else {
                                    System.out.println("Рядок " + getLexLine() + ": цикл має містити to operator()");
                                    errors.add(new Errors(getLexLine(), ": цикл має містити to operator()"));
                                    return false;
                                }
                            } else {
                                System.out.println("Рядок " + getLexLine() + ": цикл має містити вираз після після by operator()");
                                errors.add(new Errors(getLexLine(), ": цикл має містити вираз після після by operator()"));
                                return false;
                            }
                        } else {
                            System.out.println("Рядок " + getLexLine() + ": цикл має містити by operator()");
                            errors.add(new Errors(getLexLine(), ": цикл має містити by operator()"));
                            return false;
                        }
                    } else {
                        System.out.println("Рядок " + getLexLine() + ": цикл має містити вираз після після = operator()");
                        errors.add(new Errors(getLexLine(), ": цикл має містити вираз після після = operator()"));
                        return false;
                    }
                } else {
                    System.out.println("Рядок " + getLexLine() + ": цикл має містити = після IDN operator()");
                    errors.add(new Errors(getLexLine(), ": цикл має містити = після IDN operator()"));
                    return false;
                }
            } else {
                System.out.println("Рядок " + getLexLine() + ": цикл має містити IDN після for operator()");
                errors.add(new Errors(getLexLine(), ": цикл має містити IDN після for operator()"));
                return false;
            }
//            ОПЕРАТОР УМОВНИЙ ПЕРЕХІД +++++++++++++++++
        } else if (outOfRange() && getLexCode() == 12) { //if
            lexNumber++;
            if (logicalExpression()) {
                if (outOfRange() && getLexCode() == 13) { //then
                    lexNumber++;
                    if (operatorList()) {
                        if (outOfRange() && getLexCode() == 14) { //fi
                            lexNumber++;
                            return true;
                        } else {
                            System.out.println("Рядок " + getLexLine() + ": умовний оператор закінчується на fi operator()");
                            errors.add(new Errors(getLexLine(), ": умовний оператор закінчується на fi operator()"));
                            return false;
                        }
                    }
                } else {
                    System.out.println("Рядок " + getLexLine() + ": умовний оператор містить then operator()");
                    errors.add(new Errors(getLexLine(), ": умовний оператор містить then operator()"));
                    return false;
                }
            }
        }
        System.out.println("Рядок " + getLexLine() + ": невідомий оператор operator()");
        errors.add(new Errors(getLexLine(), ": невідомий оператор operator()"));
        return false;
    }

    private boolean expression() {
        if (terminal()) {
            while (outOfRange() && (getLexCode() == 24 || getLexCode() == 25)) { // + -
                lexNumber++;
                terminal();
            }
            return true;
        } else {
            System.out.println("Рядок " + getLexLine() + ": має бути термінал expression()");
            errors.add(new Errors(getLexLine(), "має бути термінал expression()"));
            return false;
        }
    }

    private boolean terminal() {
        if (multiplier()) {
            while (outOfRange() && (getLexCode() == 26 || getLexCode() == 27)) { // * /
                lexNumber++;
                multiplier();
            }
            return true;
        } else {
            System.out.println("Рядок " + getLexLine() + ": має бути множник terminal()");
            errors.add(new Errors(getLexLine(), "має бути множник terminal()"));
            return false;
        }
    }

    private boolean multiplier() {
        if (outOfRange() && getLexCode() == 28) { // (
            lexNumber++;
            expression();
            if (outOfRange() && getLexCode() == 29) { // )
                lexNumber++;
                return true;
            } else {
                System.out.println("Рядок " + getLexLine() + ": має бути закриваюча дужка multiplier()");
                errors.add(new Errors(getLexLine(), "має бути закриваюча дужка multiplier()"));
                return false;
            }
        } else if (outOfRange() && (getLexCode() == 37 || getLexCode() == 38)) { //IDN CON
            lexNumber++;
            return true;
        } else {
            System.out.println("Рядок " + getLexLine() + ": має бути IDN або CON або відкриваюча дужка для виразу multiplier()");
            errors.add(new Errors(getLexLine(), "має бути IDN або CON multiplier()"));
            return false;
        }
    }

    private boolean logicalExpression() {
        if (logicalTerminal()) {
            while (outOfRange() && getLexCode() == 17) { // or
                lexNumber++;
                logicalTerminal();
            }
            return true;
        } else {
            System.out.println("Рядок " + getLexLine() + ": має бути логічний вираз logicalExpression()");
            errors.add(new Errors(getLexLine(), "має бути логічний вираз logicalExpression()"));
            return false;
        }
    }

    private boolean logicalTerminal() {
        if (logicalMultiplier()) {
            while (outOfRange() && getLexCode() == 18) { // and
                lexNumber++;
                logicalTerminal();
            }
            return true;
        } else {
            System.out.println("Рядок " + getLexLine() + ": має бути логічний термінал logicalTerminal()");
            errors.add(new Errors(getLexLine(), "має бути логічний термінал logicalTerminal()"));
            return false;
        }
    }

    private boolean logicalMultiplier() {
        if (outOfRange() && getLexCode() == 28) { // (
            lexNumber++;
            if (logicalExpression()) {
                if (outOfRange() && getLexCode() == 29) { // )
                    lexNumber++;
                    return true;
                } else {
                    System.out.println("Рядок " + getLexLine() + ": має бути закриваюча дужка logicalMultiplier()");
                    errors.add(new Errors(getLexLine(), "має бути закриваюча дужка logicalMultiplier()"));
                    return false;
                }
            }
        } else if (outOfRange() && getLexCode() == 19) { // not
            lexNumber++;
            if (logicalMultiplier()) {
                return true;
            } else {
                System.out.println("Рядок " + getLexLine() + ": має бути логічний множник після not logicalMultiplier()");
                errors.add(new Errors(getLexLine(), "має бути логічний множник після not logicalMultiplier()"));
                return false;
            }
        } else if (relation()) {
            return true;
        }
        System.out.println("Рядок " + getLexLine() + ": має бути логічний множник logicalMultiplier()");
        errors.add(new Errors(getLexLine(), "має бути логічний множник logicalMultiplier()"));
        return false;
    }

    private boolean relation() {
        if (expression()) {
            if (tokenRelation()) {
                if (expression()) {
                    return true;
                } else {
                    System.out.println("Рядок " + getLexLine() + ": має бути останній вираз relation()");
                    errors.add(new Errors(getLexLine(), "має бути останній вираз relation()"));
                    return false;
                }
            } else {
                System.out.println("Рядок " + getLexLine() + ": має бути знак відношення relation()");
                errors.add(new Errors(getLexLine(), "має бути знак відношення relation()"));
                return false;
            }
        } else {
            System.out.println("Рядок " + getLexLine() + ": має бути перший вираз relation()");
            errors.add(new Errors(getLexLine(), "має бути перший вираз relation()"));
            return false;
        }
    }

    private boolean tokenRelation() { //знак відношення
        if (outOfRange() && (getLexCode() == 31 || getLexCode() == 32 || getLexCode() == 33
                || getLexCode() == 34 || getLexCode() == 35 || getLexCode() == 36)) {
            lexNumber++;
            return true;
        } else {
            System.out.println("Рядок " + getLexLine() + ": має бути знак відношення tokenRelation()");
            errors.add(new Errors(getLexLine(), "має бути знак відношення tokenRelation()"));
            return false;
        }
    }

    public static void main(String argv[]) {
        String file = LexicalAnalyzer.readFile();
        LexicalAnalyzer analyzer = new LexicalAnalyzer(file);
        new SyntaxAnalyzer(analyzer);
    }
}
