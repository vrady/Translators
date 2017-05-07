package Models;

/**
 * Created by dimav on 07.05.2017.
 */
public class PolizExecution {
    private String stack;

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public String getPoliz() {
        return poliz;
    }

    public void setPoliz(String poliz) {
        this.poliz = poliz;
    }

    private String poliz;

    public PolizExecution(String stack, String poliz) {
        this.stack = stack;
        this.poliz = poliz;
    }
}
