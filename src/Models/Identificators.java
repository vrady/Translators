package Models;

/**
 * Created by dimav on 10/7/2016.
 */
public class Identificators {
    private Integer code;
    private String lex;
    private String type;

    public Identificators(Integer code, String type, String lex) {
        this.code = code;
        this.type = type;
        this.lex = lex;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getLex() {
        return lex;
    }

    public void setLex(String lex) {
        this.lex = lex;
    }
}
