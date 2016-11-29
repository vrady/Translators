package Models;

/**
 * Created by vrady on 04.11.16.
 */
public class PDARules {
    private Integer alpha;
    private String metka;
    private Integer beta;
    private Integer stack;
    private String semant;
    private String semantEqual;
    private Integer lexCode;
    private Integer semantBeta;
    private Integer semantStack;

    public PDARules(Integer alpha, String metka, Integer beta, Integer stack,
                    String semant, String semantEqual, Integer lexCode, Integer semantBeta, Integer semantStack) {
        this.alpha = alpha;
        this.metka = metka;
        this.beta = beta;
        this.stack = stack;
        this.semant = semant;
        this.semantEqual = semantEqual;
        this.lexCode = lexCode;
        this.semantBeta = semantBeta;
        this.semantStack = semantStack;
    }

    public Integer getStack() {
        return stack;
    }

    public void setStack(Integer stack) {
        this.stack = stack;
    }

    public String getSemant() {
        return semant;
    }

    public void setSemant(String semant) {
        this.semant = semant;
    }

    public Integer getBeta() {
        return beta;
    }

    public void setBeta(Integer beta) {
        this.beta = beta;
    }

    public String getMetka() {
        return metka;
    }

    public void setMetka(String metka) {
        this.metka = metka;
    }

    public Integer getAlpha() {
        return alpha;
    }

    public void setAlpha(Integer alpha) {
        this.alpha = alpha;
    }

    public String getSemantEqual() {
        return semantEqual;
    }

    public void setSemantEqual(String semantEqual) {
        this.semantEqual = semantEqual;
    }

    public Integer getLexCode() {
        return lexCode;
    }

    public void setLexCode(Integer lexCode) {
        this.lexCode = lexCode;
    }

    public Integer getSemantBeta() {
        return semantBeta;
    }

    public void setSemantBeta(Integer semantBeta) {
        this.semantBeta = semantBeta;
    }

    public Integer getSemantStack() {
        return semantStack;
    }

    public void setSemantStack(Integer semantStack) {
        this.semantStack = semantStack;
    }
}
