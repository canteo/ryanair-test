package es.ruben.ryanair.exception;

public class RyanairTestException extends RuntimeException {
    public RyanairTestException(String message) {
        super(message);
    }

    public String getException() {
        return this.getClass().getSimpleName();
    }

    public String getDescription() {
        return this.getMessage();
    }
}
