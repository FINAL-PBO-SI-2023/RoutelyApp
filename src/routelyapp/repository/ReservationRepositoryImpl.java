package routelyapp.repository;

import routelyapp.koneksi.Koneksi;
import routelyapp.model.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepositoryImpl implements ReservationRepository {

    @Override
    public List<Reservation> findAllWithDetails() throws SQLException {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT r.id, r.id_user, r.kode_reservasi, r.nama_penumpang, r.no_hp, " +
            "d.kursi, s.asal, s.tujuan, s.tanggal, s.jam, s.terminal, r.status, b.nama_bus " +
            "FROM reservations r " +
            "JOIN reservation_details d ON r.id = d.reservation_id " +
            "JOIN schedules s ON r.id_schedule = s.id " +
            "JOIN buses b ON s.id_bus = b.id";

        try (Connection conn = Koneksi.getKoneksi();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Reservation res = new Reservation();
                res.setId(rs.getInt("id"));
                res.setUserId(rs.getInt("id_user"));  // Tambahkan baris ini
                res.setKodeReservasi(rs.getString("kode_reservasi"));
                res.setNamaBus(rs.getString("nama_bus"));
                res.setNamaPenumpang(rs.getString("nama_penumpang"));
                res.setNoHp(rs.getString("no_hp"));
                res.setKursi(rs.getString("kursi"));
                res.setAsal(rs.getString("asal"));
                res.setTujuan(rs.getString("tujuan"));
                res.setTanggal(rs.getDate("tanggal"));
                res.setJam(rs.getTime("jam"));
                res.setTerminal(rs.getString("terminal"));
                res.setStatus(rs.getString("status"));
                list.add(res);
            }
        }
        return list;
    }

    @Override
    public void updateStatus(int id, String status) throws SQLException {
        String sql = "UPDATE reservations SET status = ? WHERE id = ?";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, status);
            pst.setInt(2, id);
            pst.executeUpdate();
        }
    }

    @Override
    public Reservation getReservationById(int id) throws Exception {
    String sql = "SELECT r.id, r.id_user, r.kode_reservasi, r.nama_penumpang, r.no_hp, " +
         "d.kursi, s.asal, s.tujuan, s.tanggal, s.jam, s.terminal, r.status, b.nama_bus, s.terminal " +
         "FROM reservations r " +
         "JOIN reservation_details d ON r.id = d.reservation_id " +
         "JOIN schedules s ON r.id_schedule = s.id " +
         "JOIN buses b ON s.id_bus = b.id " +
         "WHERE r.id = ?";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Reservation r = new Reservation();
                    r.setId(rs.getInt("id"));
                    r.setKodeReservasi(rs.getString("kode_reservasi"));
                    r.setNamaBus(rs.getString("nama_bus"));
                    r.setNamaPenumpang(rs.getString("nama_penumpang"));
                    r.setNoHp(rs.getString("no_hp"));
                    r.setKursi(rs.getString("kursi"));
                    r.setAsal(rs.getString("asal"));
                    r.setTujuan(rs.getString("tujuan"));
                    r.setTanggal(rs.getDate("tanggal"));
                    r.setJam(rs.getTime("jam"));
                    r.setTerminal(rs.getString("terminal"));
                    r.setStatus(rs.getString("status"));
                    r.setUserId(rs.getInt("id_user"));
                    return r;
                }
            }
        }
        return null;
    }

    @Override
    public int ambilKapasitasBus(int idSchedule) throws SQLException {
        String sql = "SELECT b.kapasitas FROM schedules s JOIN buses b ON s.id_bus = b.id WHERE s.id = ?";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idSchedule);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("kapasitas");
                }
            }
        }
        return 20; // default jika gagal
    }

    @Override
    public boolean kursiSudahDipesan(int idSchedule, String kursi) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservation_details d " +
                     "JOIN reservations r ON d.reservation_id = r.id " +
                     "WHERE r.id_schedule = ? AND d.kursi = ? AND (r.status = 'dipesan' OR r.status = 'dibayar')";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idSchedule);
            pst.setString(2, kursi);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    @Override
    public int insertReservation(Connection conn, int idUser, int idSchedule, String nama, String hp, String kode) throws SQLException {
        String sql = "INSERT INTO reservations (id_user, id_schedule, nama_penumpang, no_hp, status, kode_reservasi) " +
                     "VALUES (?, ?, ?, ?, 'dipesan', ?)";
        try (PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, idUser);
            pst.setInt(2, idSchedule);
            pst.setString(3, nama);
            pst.setString(4, hp);
            pst.setString(5, kode);
            pst.executeUpdate();

            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Gagal mendapatkan ID reservasi.");
                }
            }
        }
    }


    @Override
    public void insertReservationDetails(Connection conn, int reservationId, List<String> kursiList) throws SQLException {
        String sql = "INSERT INTO reservation_details (reservation_id, kursi) VALUES (?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            for (String kursi : kursiList) {
                pst.setInt(1, reservationId);
                pst.setString(2, kursi);
                pst.addBatch();
            }
            pst.executeBatch();
        }
    }

    @Override
    public List<Reservation> getReservationsByUserId(int userId) throws SQLException {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT r.id, r.id_user, r.kode_reservasi, r.nama_penumpang, r.no_hp, " +
            "d.kursi, s.asal, s.tujuan, s.tanggal, s.jam, s.terminal, r.status, b.nama_bus " +
            "FROM reservations r " +
            "JOIN reservation_details d ON r.id = d.reservation_id " +
            "JOIN schedules s ON r.id_schedule = s.id " +
            "JOIN buses b ON s.id_bus = b.id " +
            "WHERE r.id_user = ?";

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, userId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Reservation res = new Reservation();
                    res.setId(rs.getInt("id"));
                    res.setUserId(rs.getInt("id_user"));
                    res.setKodeReservasi(rs.getString("kode_reservasi"));
                    res.setNamaBus(rs.getString("nama_bus"));
                    res.setNamaPenumpang(rs.getString("nama_penumpang"));
                    res.setNoHp(rs.getString("no_hp"));
                    res.setKursi(rs.getString("kursi"));
                    res.setAsal(rs.getString("asal"));
                    res.setTujuan(rs.getString("tujuan"));
                    res.setTanggal(rs.getDate("tanggal"));
                    res.setJam(rs.getTime("jam"));
                    res.setTerminal(rs.getString("terminal"));
                    res.setStatus(rs.getString("status"));
                    list.add(res);
                }
            }
        }
        return list;
    }

    @Override
    public Reservation getReservationByKode(String kode) throws SQLException {
        String sql = "SELECT r.*, b.nama_bus, s.asal, s.tujuan, s.tanggal, s.jam, s.terminal " +
                     "FROM reservations r " +
                     "JOIN schedules s ON r.id_schedule = s.id " +
                     "JOIN buses b ON s.id_bus = b.id " +
                     "WHERE r.kode_reservasi = ?";

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kode);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Reservation res = new Reservation();
                res.setKodeReservasi(rs.getString("kode_reservasi"));
                res.setNamaBus(rs.getString("nama_bus"));
                res.setAsal(rs.getString("asal"));
                res.setTujuan(rs.getString("tujuan"));
                res.setTanggal(rs.getDate("tanggal"));
                res.setJam(rs.getTime("jam"));
                res.setTerminal(rs.getString("terminal"));
                res.setStatus(rs.getString("status"));
                return res;
            }
        }
        return null;
    }

//    // In ReservationRepositoryImpl.java
//    @Override
//    public void updateReservationStatus(int id, String status, String cancelReason) throws SQLException {
//        String sql = "UPDATE reservations SET status = ?, cancel_reason = ? WHERE id = ?";
//        try (Connection conn = Koneksi.getKoneksi();
//             PreparedStatement pst = conn.prepareStatement(sql)) {
//            pst.setString(1, status);
//            pst.setString(2, cancelReason);
//            pst.setInt(3, id);
//            pst.executeUpdate();
//        } catch (Exception e) {
//            throw new SQLException("Gagal mengubah status reservasi: " + e.getMessage(), e);
//        }
//    }

    @Override
    public String getReservationCode(int id) throws SQLException {
        String kode = "";
        String sql = "SELECT kode_reservasi FROM reservations WHERE id = ?";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                kode = rs.getString("kode_reservasi");
            }
        } catch (Exception e) {
            throw new SQLException("Gagal mengambil kode reservasi: " + e.getMessage(), e);
        }
        return kode;
    }

    @Override
    public void deleteReservationDetails(Connection conn, int reservationId) throws SQLException {
        String sql = "DELETE FROM reservation_details WHERE reservation_id = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, reservationId);
            pst.executeUpdate();
        }
    }

    @Override
    public void deleteReservation(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM reservations WHERE id = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    @Override
    public boolean isSeatReservedOrPaid(Connection conn, int scheduleId, String seatCode) throws SQLException {
        String sql = "SELECT r.status FROM reservations r " +
                     "JOIN reservation_details d ON r.id = d.reservation_id " +
                     "WHERE r.id_schedule = ? AND d.kursi = ? AND (r.status = 'dipesan' OR r.status = 'dibayar')";

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, scheduleId);
            pst.setString(2, seatCode);

            try (ResultSet rs = pst.executeQuery()) {
                return rs.next(); // returns true if there's any row matching the criteria
            }
        }
    }
}
