package service;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;

import java.util.Objects;

public class ClearService {
    private final UserDAO dataAccess;
    public ClearService() {
        // if we pass in a dao, it will use that, if not it will use a newly created DAO.
        this.dataAccess = new MemoryUserDAO(null, null, null);
    }

    public void clearGame() {
        try {
            dataAccess.clear();
//            return new ClearResult("");
        } catch (DataAccessException ex) {
//            return new ClearResult("did not clear");
        }

    }

}
