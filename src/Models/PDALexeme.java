package Models;

/**
 * Created by vrady on 04.11.16.
 */
public class PDALexeme {
    private Integer number;
    private String lexeme;
    private Integer state;
    private String stack;

    public PDALexeme(Integer number, String lexeme, Integer state, String stack) {
        this.number = number;
        this.lexeme = lexeme;
        this.state = state;
        this.stack = stack;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
