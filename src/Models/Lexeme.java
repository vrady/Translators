package Models;

/**
 * Created by dimav on 10/3/2016.
 */
public class Lexeme {

    private Integer counter;
    private Integer line;
    private Integer lex;
    private Integer con;
    private Integer id;
    private String lexName;
    private Integer lexCode;
    public Lexeme(int line, int lex, int con, int id) {
        this.line = line;
        this.lex = lex;
        this.con = con;
        this.id = id;
    }

    public Integer getLexCode() {
        return lexCode;
    }

    public void setLexCode(Integer lexCode) {
        this.lexCode = lexCode;
    }

    public String getLexName() {
        return lexName;
    }

    public void setLexName(String lexName) {
        this.lexName = lexName;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    public Integer getLex() {
        return lex;
    }

    public void setLex(Integer lex) {
        this.lex = lex;
    }

    public Integer getCon() {
        return con;
    }

    public void setCon(Integer con) {
        this.con = con;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public static Lexeme reservedWord(int line, int code) {
        return new Lexeme(line, code, -1, -1);
    }
    public static Lexeme сonstant(int line, int con) {
        return new Lexeme(line, 38, con, -1);
    }
    public static Lexeme id(int line, int id) {
        return new Lexeme(line, 37, -1, id);
    }


    public boolean isConstant() {
        return this.con != -1;
    }
    public boolean isId() {
        return this.id != -1;
    }

    public boolean isExprFinish(){
        return isConstant() || isId() || this.getLex() == 29;
    }
}
