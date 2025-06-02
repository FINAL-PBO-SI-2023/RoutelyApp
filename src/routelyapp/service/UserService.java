package routelyapp.service;

import routelyapp.model.User;

public interface UserService {
    User getUserById(int id);
    boolean isUsernameExists(String username);
    User authenticateUser(String username, String password);
    boolean registerUser(String username, String password);
}