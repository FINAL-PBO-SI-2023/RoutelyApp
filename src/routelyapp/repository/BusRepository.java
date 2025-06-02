package routelyapp.repository;

import routelyapp.model.Bus;
import java.sql.SQLException;
import java.util.List;

public interface BusRepository {
    List<Bus> findAll() throws SQLException;
    Bus getById(int id) throws SQLException;
    boolean insert(Bus bus)throws SQLException;
    boolean update(Bus bus)throws SQLException;
    boolean delete(int id)throws SQLException;
}
