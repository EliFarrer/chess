package service;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;

import java.util.Objects;

public class ClearService {
    private final UserDAO dataAccess;
    public ClearService(MemoryUserDAO dao) {
        // if we pass in a dao, it will use that, if not it will use a newly created DAO.
        this.dataAccess = Objects.requireNonNullElseGet(dao, () -> new MemoryUserDAO(null, null, null));
    }

    public void clearGame() throws DataAccessException {
        try {
            dataAccess.clear();
        } catch (DataAccessException ex) {
            throw new DataAccessException(ex.getMessage());
        }

    }

}
