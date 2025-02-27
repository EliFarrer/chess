package service;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;

public class ClearService {
    private final UserDAO dataAccess;
    public ClearService() {
        this.dataAccess = new MemoryUserDAO();
    }

    public void clearGame() {
        dataAccess.clearAllEntries();
    }

}
