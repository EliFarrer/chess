package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
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
    public void testCreateAuthPositive() {
        DatabaseDAO db = new DatabaseDAO();
        Assertions.assertDoesNotThrow(() -> {
            AuthData authData = db.createAuth("eli");
            Assertions.assertNotNull(authData);
            Assertions.assertFalse(db.isAuthDatabaseEmpty());
        });
    }

    @Test
    public void testCreateAuthNegative() {

    }

    @Test
    public void testGetUsernamePositive() {
        DatabaseDAO db = new DatabaseDAO();
        String expectedUsername = "eli";
        Assertions.assertDoesNotThrow(() -> {
            AuthData authData = db.createAuth(expectedUsername);
            String actualUsername = db.getUsername(authData.authToken());
            Assertions.assertEquals(expectedUsername, actualUsername);
        });
    }

    @Test
    public void testGetUsernameNegative() {

    }

    @Test
    public void testIsNotAuthorizedPositive() {
        DatabaseDAO db = new DatabaseDAO();
        String expectedUsername = "eli";
        Assertions.assertDoesNotThrow(() -> {
            AuthData authData = db.createAuth(expectedUsername);
            Assertions.assertFalse(db.isNotAuthorized(authData.authToken()));
            Assertions.assertTrue(db.isNotAuthorized("hello"));
        });
    }

    @Test
    public void testIsNotAuthorizedNegative() {

    }

    @Test
    public void testRemoveAuthPositive() {
    DatabaseDAO db = new DatabaseDAO();
    Assertions.assertDoesNotThrow(() ->{
        AuthData authData = db.createAuth("eli");
        Assertions.assertFalse(db.isAuthDatabaseEmpty());
        db.removeAuth(authData.authToken());
        Assertions.assertTrue(db.isAuthDatabaseEmpty());
    });

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
