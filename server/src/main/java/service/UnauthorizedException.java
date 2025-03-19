package service;

public class UnauthorizedException extends RuntimeException {
    int statusCode = 401;
    public UnauthorizedException(String message) {
      super(message);
    }
    public int getStatusCode() {
        return statusCode;
    }
}
