package dataaccess;

import model.UserData;

public interface UserDAO {
    // in here are all the declarations of the methods we need to run eventually like clear and add and stuff like that
    void clearAllEntries();

    void createUser(UserData userData);

    UserData getUser(String username);

}
