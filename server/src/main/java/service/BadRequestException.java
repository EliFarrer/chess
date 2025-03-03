package service;

public class BadRequestException extends RuntimeException { // 400
    public BadRequestException(String message) {
        super(message);
    }
}
