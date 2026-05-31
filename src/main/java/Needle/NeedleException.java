package Needle;

public class NeedleException extends Exception {

    public NeedleException(String message) {
        super(message);
    }

    public NeedleException(String message, Throwable cause) {
        super(message, cause);
    }

    public NeedleException(Throwable cause) {
        super(cause);
    }
}