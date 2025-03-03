package service;

public class AlreadyTakenException extends RuntimeException {   // 403
    public AlreadyTakenException(String message) {
        super(message);
    }
}
