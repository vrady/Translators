package Models;

/**
 * Created by dimav on 19.04.2017.
 */
public class PolizProcessing {
    private String inputPoliz;
    private String stackPoliz;
    private String outputPoliz;

    public PolizProcessing(String inputPoliz, String stackPoliz, String outputPoliz) {
        this.inputPoliz = inputPoliz;
        this.stackPoliz = stackPoliz;
        this.outputPoliz = outputPoliz;
    }

    public String getInputPoliz() {
        return inputPoliz;
    }

    public void setInputPoliz(String inputPoliz) {
        this.inputPoliz = inputPoliz;
    }

    public String getStackPoliz() {
        return stackPoliz;
    }

    public void setStackPoliz(String stackPoliz) {
        this.stackPoliz = stackPoliz;
    }

    public String getOutputPoliz() {
        return outputPoliz;
    }

    public void setOutputPoliz(String outputPoliz) {
        this.outputPoliz = outputPoliz;
    }
}
