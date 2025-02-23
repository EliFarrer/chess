package service;
import dataaccess.UserDAO;

public class ClearService {
    private final UserDAO dataAccess;
    public ClearService(UserDAO dataAccess) {
        this.dataAccess = dataAccess;
    }

    public void clearGame() {
        dataAccess.clearAllEntries();
    }

}
