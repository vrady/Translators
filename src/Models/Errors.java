package Models;

/**
 * Created by vrady on 27.10.16.
 */
public class Errors {
    private Integer line;
    private String text;

    public Errors(Integer line, String text) {
        this.line = line;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }
}
