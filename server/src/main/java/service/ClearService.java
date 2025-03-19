package service;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.DatabaseDAO;

import java.util.Objects;

public class ClearService {
    private final DataAccess dataAccess;
    public ClearService(DataAccess dao) {
        // if we pass in a dao, it will use that, if not it will use a newly created DAO.
        this.dataAccess = Objects.requireNonNullElseGet(dao, DatabaseDAO::new);
    }

    public void clearGame() throws DataAccessException {
        try {
            dataAccess.clear();
        } catch (DataAccessException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
}
