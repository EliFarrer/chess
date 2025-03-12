package dataaccess;

import model.UserData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;


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
    public void testCreateUserPositive() {  // invalid data,
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

    // go into ta lab, how do I do a negative test case here
    // what about writing tests for helper functions?
    @Test
    public void testCreateUserNegative() {
        // this is the already registered case
        DatabaseDAO db = new DatabaseDAO();
        UserData expectedUserData = new UserData("eli", "ile", "eli@ile.com");
        Assertions.assertDoesNotThrow(() -> db.createUser(expectedUserData));
        Assertions.assertThrows(DataAccessException.class, () -> db.createUser(expectedUserData));

        // note that the bad data is handled by the service, already registered should be as well.
    }

    @Test
    public void testGetUserPositive() {
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
    public void testGetUserNegative() {

    }

    @Test
    public void testIsUserDatabaseEmptyPositive() {

    }
    @Test
    public void testIsUserDatabaseEmptyNegative() {

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
