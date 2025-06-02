package routelyapp.service;

import routelyapp.model.Schedule;
import routelyapp.repository.ScheduleRepository;

import javax.swing.JOptionPane;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public List<Schedule> getAll() {
        try {
            return scheduleRepository.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal mengambil data jadwal.", "Error", JOptionPane.ERROR_MESSAGE);
            return List.of();
        }
    }

    @Override
    public Schedule getById(int id) {
        try {
            return scheduleRepository.getById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal mengambil data jadwal.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    @Override
    public void insert(Schedule s) {
        try {
            boolean success = scheduleRepository.insert(s);
            if (success) {
                JOptionPane.showMessageDialog(null, "Jadwal berhasil ditambahkan.");
            } else {
                JOptionPane.showMessageDialog(null, "Gagal menambahkan jadwal.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menambah jadwal.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void update(Schedule s) {
        try {
            boolean success = scheduleRepository.update(s);
            if (success) {
                JOptionPane.showMessageDialog(null, "Jadwal berhasil diubah.");
            } else {
                JOptionPane.showMessageDialog(null, "Gagal mengubah jadwal.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat mengubah jadwal.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public boolean delete(int id) {
        try {
            boolean success = scheduleRepository.delete(id);
            if (success) {
                JOptionPane.showMessageDialog(null, "Jadwal berhasil dihapus.");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Gagal menghapus jadwal.");
                return false;
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(null,
                "Tidak bisa menghapus jadwal karena masih digunakan di reservasi.",
                "Gagal Menghapus", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menghapus jadwal.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    @Override
    public List<Map<String, Object>> getAllWithBus() {
        try {
            return scheduleRepository.findAllWithBus();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal mengambil data jadwal dengan nama bus.", "Error", JOptionPane.ERROR_MESSAGE);
            return List.of();
        }
    }
    
    @Override
    public List<Map<String, Object>> getFiltered(String asal, String tujuan, String tanggal) {
        try {
            return scheduleRepository.findFiltered(asal, tujuan, tanggal);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal mengambil data pencarian jadwal.", "Error", JOptionPane.ERROR_MESSAGE);
            return List.of();
        }
    }
    
    @Override
    public List<String> getAllAsal() {
        try {
            return scheduleRepository.findDistinctAsal();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<String> getAllTujuan() {
        try {
            return scheduleRepository.findDistinctTujuan();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
