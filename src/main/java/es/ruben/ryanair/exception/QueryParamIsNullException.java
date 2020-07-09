package es.ruben.ryanair.exception;

public class QueryParamIsNullException extends RyanairTestException {
    public QueryParamIsNullException(String param) {
        super("'" + param + "' query param must not be null");
    }
}
