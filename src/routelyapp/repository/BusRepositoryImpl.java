
package routelyapp.repository;

import routelyapp.koneksi.Koneksi;
import routelyapp.model.Bus;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BusRepositoryImpl implements BusRepository {

    @Override
    public boolean insert(Bus bus) {
        try (Connection conn = Koneksi.getKoneksi()) {
            String sql = "INSERT INTO buses (nama_bus, nomor_polisi, kapasitas) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, bus.getNamaBus());
            pst.setString(2, bus.getNomorPolisi());
            pst.setInt(3, bus.getKapasitas());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Bus bus) {
        try (Connection conn = Koneksi.getKoneksi()) {
            String sql = "UPDATE buses SET nama_bus = ?, nomor_polisi = ?, kapasitas = ? WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, bus.getNamaBus());
            pst.setString(2, bus.getNomorPolisi());
            pst.setInt(3, bus.getKapasitas());
            pst.setInt(4, bus.getId());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        try (Connection conn = Koneksi.getKoneksi()) {
            String sql = "DELETE FROM buses WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            return pst.executeUpdate() > 0;
        }
    }

    @Override
    public Bus getById(int id) throws SQLException {
        try (Connection conn = Koneksi.getKoneksi()) {
            String sql = "SELECT * FROM buses WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Bus(
                    rs.getInt("id"),
                    rs.getString("nama_bus"),
                    rs.getString("nomor_polisi"),
                    rs.getInt("kapasitas")
                );
            }
        }
        return null;
    }

    @Override
    public List<Bus> findAll() {
        List<Bus> list = new ArrayList<>();
        try (Connection conn = Koneksi.getKoneksi()) {
            String sql = "SELECT * FROM buses";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new Bus(
                    rs.getInt("id"),
                    rs.getString("nama_bus"),
                    rs.getString("nomor_polisi"),
                    rs.getInt("kapasitas")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Method tambahan untuk validasi sebelum delete
    public int hitungJadwalDenganBus(int busId) {
        String sql = "SELECT COUNT(*) FROM schedules WHERE id_bus = ?";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, busId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
