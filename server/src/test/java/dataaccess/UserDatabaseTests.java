package dataaccess;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import service.AlreadyTakenException;
import service.BadRequestException;
import service.UnauthorizedException;
import service.UserService;

import java.util.HashMap;

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
            Assertions.assertEquals(expectedUserData, actualUserData);
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
            Assertions.assertEquals(expectedUserData, actualUserData);
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
