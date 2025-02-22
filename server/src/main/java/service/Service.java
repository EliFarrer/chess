package service;
import dataaccess.DataAccess;

public class Service {
    private final DataAccess dataAccess;
    public Service(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public void clearGame() {
        dataAccess.clearAllEntries();
    }

}
