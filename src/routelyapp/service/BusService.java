package routelyapp.service;

import routelyapp.model.Bus;
import java.util.List;

public interface BusService {
    List<Bus> getAll();
    Bus getById(int id);
    boolean simpan(Bus bus);
    boolean hapus(int id);
}
