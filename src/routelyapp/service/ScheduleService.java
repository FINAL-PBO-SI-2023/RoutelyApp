package routelyapp.service;
import routelyapp.model.Schedule;
import java.util.List;
import java.util.Map;

public interface ScheduleService {
    List<Schedule> getAll();
    Schedule getById(int id);
    void insert(Schedule s);
    void update(Schedule s);
    boolean delete(int id);
    
    List<Map<String, Object>> getAllWithBus();
    List<Map<String, Object>> getFiltered(String asal, String tujuan, String tanggal);
    public List<String> getAllAsal();
    public List<String> getAllTujuan();
}
