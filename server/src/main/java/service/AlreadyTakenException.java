package service;

public class AlreadyTakenException extends RuntimeException {
    int statusCode = 403;
    public AlreadyTakenException(String message) {
        super(message);
    }
    public int getStatusCode() {
        return statusCode;
    }
}
