package routelyapp.service;

import routelyapp.model.User;
import routelyapp.repository.UserRepository;
import routelyapp.repository.UserRepositoryImpl;
import java.sql.SQLException;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl() {
        this.userRepository = new UserRepositoryImpl();
    }

    @Override
    public User getUserById(int id) {
        try {
            return userRepository.findById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean isUsernameExists(String username) {
        try {
            return userRepository.findByUsername(username) != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User authenticateUser(String username, String password) {
        try {
            return userRepository.findByUsernameAndPassword(username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean registerUser(String username, String password) {
        try {
            if (isUsernameExists(username)) {
                return false;
            }
            
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setRole("user");
            
            userRepository.save(newUser);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}