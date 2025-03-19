package service;

public class BadRequestException extends RuntimeException {
    int statusCode = 400;
    public BadRequestException(String message) {
        super(message);
    }
    public int getStatusCode() {
        return statusCode;
    }
}
