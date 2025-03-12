package dataaccess;

import model.UserData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import javax.xml.crypto.Data;


public class UserDatabaseTests {
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
    public void testCreateUserPositive() {
        // string username to user text
        DatabaseDAO db = new DatabaseDAO();
        UserData expectedUserData = new UserData("eli", "ile", "eli@ile.com");

        Assertions.assertDoesNotThrow(() -> db.createUser(expectedUserData));

        Assertions.assertDoesNotThrow(() -> {
            UserData actualUserData = db.getUser(expectedUserData.username());
            Assertions.assertEquals(expectedUserData.username(), actualUserData.username());
            Assertions.assertTrue(BCrypt.checkpw(expectedUserData.password(), actualUserData.password()));
            Assertions.assertEquals(expectedUserData.email(), actualUserData.email());
        });

    }

    @Test
    public void testCreateUserNegativeBadData() {
        DatabaseDAO db = new DatabaseDAO();
        UserData expectedUserData = new UserData("eli", "ile", "eli@ile.com");
        Assertions.assertDoesNotThrow(() -> db.createUser(expectedUserData));
        Assertions.assertThrows(DataAccessException.class, () -> db.createUser(expectedUserData));
    }

    @Test
    public void testCreateUserNegativeDuplicate() {
        DatabaseDAO db = new DatabaseDAO();
        UserData expectedUserData = new UserData(null, "ile", "eli@ile.com");
        Assertions.assertThrows(DataAccessException.class, () -> db.createUser(expectedUserData));
    }

    @Test
    public void testGetUserPositive() {
        DatabaseDAO db = new DatabaseDAO();
        UserData expectedUserData = new UserData("eli", "ile", "eli@ile.com");

        Assertions.assertDoesNotThrow(() -> {
            db.createUser(expectedUserData);
            UserData actualUserData = db.getUser(expectedUserData.username());
            Assertions.assertEquals(expectedUserData.username(), actualUserData.username());
            Assertions.assertTrue(BCrypt.checkpw(expectedUserData.password(), actualUserData.password()));
            Assertions.assertEquals(expectedUserData.email(), actualUserData.email());
        });
    }

    @Test
    public void testGetUserNegativeBadData() {
        DatabaseDAO db = new DatabaseDAO();
        UserData expectedUserData = new UserData("eli", "ile", "eli@ile.com");

        Assertions.assertDoesNotThrow(() -> db.createUser(expectedUserData));

        Assertions.assertThrows(DataAccessException.class, () -> db.getUser(null));
    }

    @Test
    public void testUserExistsPositive() {
        DatabaseDAO db = new DatabaseDAO();
        UserData expectedUserData = new UserData("eli", "ile", "eli@ile.com");

        Assertions.assertDoesNotThrow(() -> {
            db.createUser(expectedUserData);
            Assertions.assertTrue(db.userExists(expectedUserData.username()));
        });
    }

    @Test
    public void testUserExistsNegative() {
        DatabaseDAO db = new DatabaseDAO();
        UserData expectedUserData = new UserData("eli", "ile", "eli@ile.com");

        Assertions.assertDoesNotThrow(() -> {
            Assertions.assertFalse(db.userExists(expectedUserData.username()));
        });
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
