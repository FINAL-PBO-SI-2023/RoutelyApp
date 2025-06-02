package routelyapp.service;

import routelyapp.model.Bus;
import routelyapp.repository.BusRepository;
import routelyapp.repository.BusRepositoryImpl;

import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Collections;
import java.util.List;

public class BusServiceImpl implements BusService {

    private final BusRepository busRepo = new BusRepositoryImpl();

    @Override
    public boolean simpan(Bus bus) {
        try {
            if (bus.getId() == null) {
                return busRepo.insert(bus);
            } else {
                return busRepo.update(bus);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Gagal menyimpan data bus.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    @Override
    public boolean hapus(int id) {
        try {
            boolean result = busRepo.delete(id);
            if (result) {
                JOptionPane.showMessageDialog(null, "Data bus berhasil dihapus.");
            }
            return result;
        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(null,
                    "Tidak bisa menghapus data bus karena masih digunakan dalam jadwal atau reservasi.",
                    "Gagal Menghapus", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Terjadi kesalahan saat menghapus data bus.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    @Override
    public Bus getById(int id) {
        try {
            return busRepo.getById(id);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Terjadi kesalahan saat mengambil data bus.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    @Override
    public List<Bus> getAll() {
        try {
            return busRepo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Terjadi kesalahan saat mengambil daftar bus.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return Collections.emptyList();
        }
    }
}
