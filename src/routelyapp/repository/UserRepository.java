package routelyapp.repository;

import routelyapp.model.User;
import java.sql.SQLException;

public interface UserRepository {
    User findById(int id) throws SQLException;
    User findByUsername(String username) throws SQLException;
    User findByUsernameAndPassword(String username, String password) throws SQLException;
    void save(User user) throws SQLException;
}