package routelyapp.repository;

import routelyapp.model.Schedule;
import routelyapp.koneksi.Koneksi;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleRepositoryImpl implements ScheduleRepository {

    @Override
    public List<Schedule> findAll() throws SQLException {
        List<Schedule> list = new ArrayList<>();
        String sql = "SELECT s.*, b.nama_bus AS bus_name " +
                     "FROM schedules s JOIN buses b ON s.id_bus = b.id";

        try (Connection conn = Koneksi.getKoneksi();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Schedule s = new Schedule();
                s.setId(rs.getInt("id"));
                s.setIdBus(rs.getInt("id_bus"));
                s.setBusName(rs.getString("bus_name"));
                s.setAsal(rs.getString("asal"));
                s.setTujuan(rs.getString("tujuan"));
                s.setTanggal(rs.getDate("tanggal"));
                s.setJam(rs.getTime("jam"));
                s.setTerminal(rs.getString("terminal"));
                s.setHarga(rs.getDouble("harga"));
                list.add(s);
            }
        }

        return list;
    }

    @Override
    public Schedule getById(int id) throws SQLException {
        String sql = "SELECT * FROM schedules WHERE id = ?";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                Schedule s = new Schedule();
                s.setId(rs.getInt("id"));
                s.setIdBus(rs.getInt("id_bus"));
                s.setAsal(rs.getString("asal"));
                s.setTujuan(rs.getString("tujuan"));
                s.setTanggal(rs.getDate("tanggal"));
                s.setJam(rs.getTime("jam"));
                s.setTerminal(rs.getString("terminal"));
                s.setHarga(rs.getDouble("harga"));
                return s;
            }
        }
        return null;
    }

    @Override
    public boolean insert(Schedule s) throws SQLException {
        String sql = "INSERT INTO schedules (id_bus, asal, tujuan, tanggal, jam, harga, terminal) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, s.getIdBus());
            pst.setString(2, s.getAsal());
            pst.setString(3, s.getTujuan());
            pst.setDate(4, s.getTanggal());
            pst.setTime(5, s.getJam());
            pst.setDouble(6, s.getHarga());
            pst.setString(7, s.getTerminal());

            return pst.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(Schedule s) throws SQLException {
        String sql = "UPDATE schedules SET id_bus=?, asal=?, tujuan=?, tanggal=?, jam=?, harga=?, terminal=? WHERE id=?";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, s.getIdBus());
            pst.setString(2, s.getAsal());
            pst.setString(3, s.getTujuan());
            pst.setDate(4, s.getTanggal());
            pst.setTime(5, s.getJam());
            pst.setDouble(6, s.getHarga());
            pst.setString(7, s.getTerminal());
            pst.setInt(8, s.getId());

            return pst.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM schedules WHERE id = ?";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);
            return pst.executeUpdate() > 0;
        }
    }

    @Override
    public int countReservationsByScheduleId(int scheduleId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservations WHERE id_schedule = ?";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, scheduleId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    @Override
    public List<Map<String, Object>> findAllWithBus() throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT s.id, b.nama_bus, s.asal, s.tujuan, s.tanggal, s.jam, s.harga, s.terminal " +
                     "FROM schedules s JOIN buses b ON s.id_bus = b.id";

        try (Connection conn = Koneksi.getKoneksi();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, Object> data = new HashMap<>();
                data.put("id", rs.getInt("id"));
                data.put("nama_bus", rs.getString("nama_bus"));
                data.put("asal", rs.getString("asal"));
                data.put("tujuan", rs.getString("tujuan"));
                data.put("tanggal", rs.getDate("tanggal"));
                data.put("jam", rs.getTime("jam"));
                data.put("harga", rs.getDouble("harga"));
                data.put("terminal", rs.getString("terminal"));
                list.add(data);
            }
        }
        return list;
    }
    
    @Override
    public List<Map<String, Object>> findFiltered(String asal, String tujuan, String tanggal) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT s.id, b.nama_bus, s.asal, s.tujuan, s.tanggal, s.jam, s.harga, s.terminal " +
             "FROM schedules s JOIN buses b ON s.id_bus = b.id " +
             "WHERE (? IS NULL OR s.asal LIKE ?) " +
             "AND (? IS NULL OR s.tujuan LIKE ?) " +
             "AND (? IS NULL OR DATE(s.tanggal) = ?)";


        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, asal);
            pst.setString(2, asal != null ? "%" + asal + "%" : null);
            pst.setString(3, tujuan);
            pst.setString(4, tujuan != null ? "%" + tujuan + "%" : null);
            pst.setString(5, tanggal);
            pst.setString(6, tanggal);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("id", rs.getInt("id"));
                    data.put("nama_bus", rs.getString("nama_bus"));
                    data.put("asal", rs.getString("asal"));
                    data.put("tujuan", rs.getString("tujuan"));
                    data.put("tanggal", rs.getDate("tanggal"));
                    data.put("jam", rs.getTime("jam"));
                    data.put("harga", rs.getDouble("harga"));
                    data.put("terminal", rs.getString("terminal"));
                    list.add(data);
                }
            }
        }

        return list;
    }
    
    @Override
    public List<String> findDistinctAsal() throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT asal FROM schedules ORDER BY asal";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("asal"));
            }
        }
        return list;
    }

    @Override
    public List<String> findDistinctTujuan() throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT tujuan FROM schedules ORDER BY tujuan";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("tujuan"));
            }
        }
        return list;
    }

}
