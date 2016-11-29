package Models;

/**
 * Created by dimav on 10/7/2016.
 */
public class Constants {
    private Integer code;
    private double lex;

    public Constants(Integer code, double lex) {
        this.code = code;
        this.lex = lex;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public double getLex() {
        return lex;
    }

    public void setLex(double lex) {
        this.lex = lex;
    }
}
