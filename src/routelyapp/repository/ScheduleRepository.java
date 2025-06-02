package routelyapp.repository;

import java.sql.SQLException;
import routelyapp.model.Schedule;
import java.util.List;
import java.util.Map;

public interface ScheduleRepository {
    List<Schedule> findAll() throws SQLException;
    Schedule getById(int id) throws SQLException;
    boolean insert(Schedule schedule) throws SQLException;
    boolean update(Schedule schedule) throws SQLException;
    boolean delete(int id) throws SQLException;
    int countReservationsByScheduleId(int scheduleId) throws SQLException;
    
    List<Map<String, Object>> findAllWithBus() throws SQLException;
    List<Map<String, Object>> findFiltered(String asal, String tujuan, String tanggal) throws SQLException;
    public List<String> findDistinctAsal() throws SQLException;
    public List<String> findDistinctTujuan() throws SQLException;

}
