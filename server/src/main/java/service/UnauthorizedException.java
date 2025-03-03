package service;

public class UnauthorizedException extends RuntimeException {   // 401
    public UnauthorizedException(String message) {
      super(message);
    }
}
