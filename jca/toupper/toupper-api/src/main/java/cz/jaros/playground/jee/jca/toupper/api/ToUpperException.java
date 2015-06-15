package cz.jaros.playground.jee.jca.toupper.api;

public class ToUpperException extends Exception {

    private static final long serialVersionUID = 3778231748168266625L;

    public ToUpperException(String message, Throwable cause) {
        super(message, cause);
    }

    public ToUpperException(String message) {
        super(message);
    }

}
