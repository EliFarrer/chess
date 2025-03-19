package dataaccess;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception {
    int statusCode = 500;
    public DataAccessException(String message) {
        super(message);
    }
    public int getStatusCode() {
        return statusCode;
    }
}
