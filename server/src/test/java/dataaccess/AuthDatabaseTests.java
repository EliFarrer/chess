package dataaccess;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuthDatabaseTests {
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
    public void testGetUsernamePositive() {

    }

    @Test
    public void testGetUsernameNegative() {

    }

    @Test
    public void testCreateAuthPositive() {

    }

    @Test
    public void testCreateAuthNegative() {

    }

    @Test
    public void testIsNotAuthorizedPositive() {

    }

    @Test
    public void testIsNotAuthorizedNegative() {

    }

    @Test
    public void testRemoveAuthPositive() {

    }

    @Test
    public void testRemoveAuthNegative() {

    }

    @Test
    public void testIsAuthDatabaseEmptyPositive() {

    }

    @Test
    public void testIsAuthDatabaseEmptyNegative() {

    }

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
