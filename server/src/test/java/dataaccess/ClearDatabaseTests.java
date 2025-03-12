package dataaccess;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClearDatabaseTests {
    @BeforeEach
    public void setup() {
        try {
            // clear the tables
            DatabaseManager.dropTables();
            // add the tables
            DatabaseManager.createTables();
        } catch (DataAccessException e) {
            throw new RuntimeException("Error: Creating or deleting tables failed.");
        }
    }

    @Test
    public void testClearPositive() {
        DatabaseDAO db = new DatabaseDAO();

        // assert it starts empty
        Assertions.assertDoesNotThrow(() -> {
            Assertions.assertTrue(db.isUserDatabaseEmpty());
        });

        // clear
        Assertions.assertDoesNotThrow(db::clear);

        // assert it is empty still
        Assertions.assertDoesNotThrow(() -> {
            Assertions.assertTrue(db.isUserDatabaseEmpty());
        });

    }
    // don't need a negative testClear test case
    @AfterAll
    public static void reset() {
        try {
            // clear the tables
            DatabaseManager.dropTables();
            // add the tables
            DatabaseManager.createTables();
        } catch (DataAccessException e) {
            throw new RuntimeException("Error: Creating or deleting tables failed.");
        }
    }
}
