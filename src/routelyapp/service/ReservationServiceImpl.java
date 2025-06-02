package routelyapp.service;

import routelyapp.model.Reservation;
import routelyapp.repository.ReservationRepository;
import java.awt.Color;
import java.sql.*;
import java.sql.SQLException;
import java.util.List;
import routelyapp.koneksi.Koneksi;

public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    
    // Konstanta warna untuk status
    private static final Color CANCELED_BG = new Color(248, 215, 218);
    private static final Color CANCELED_FG = new Color(114, 28, 36);
    private static final Color RESERVED_BG = new Color(217, 237, 247);
    private static final Color RESERVED_FG = new Color(12, 84, 96);
    private static final Color PAID_BG = new Color(212, 237, 218);
    private static final Color PAID_FG = new Color(21, 87, 36);
    private static final Color DEFAULT_BG = Color.WHITE;
    private static final Color DEFAULT_FG = Color.BLACK;
    
    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public List<Reservation> getAllReservations() throws SQLException {
        return reservationRepository.findAllWithDetails();
    }

    @Override
    public void changeStatus(int id, String status) throws SQLException {
        reservationRepository.updateStatus(id, status);
    }

    @Override
    public Reservation getReservationById(int id) throws Exception {
        return reservationRepository.getReservationById(id);
    }

    @Override
    public void removeReservation(int id, String alasan) throws SQLException {
        Connection conn = null;
        try {
            conn = Koneksi.getKoneksi();
            conn.setAutoCommit(false);

            // Hapus reservasi
            reservationRepository.deleteReservationDetails(conn, id);
            reservationRepository.deleteReservation(conn, id);

            conn.commit();
        } catch (Exception e) {
            if (conn != null) {
                conn.rollback();
            }
            throw new SQLException("Gagal menghapus reservasi: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
    
     @Override
    public int ambilKapasitasBus(int idSchedule) throws SQLException {
        return reservationRepository.ambilKapasitasBus(idSchedule);
    }

    @Override
    public boolean kursiSudahDipesan(int idSchedule, String kursi) throws SQLException {
        return reservationRepository.kursiSudahDipesan(idSchedule, kursi);
    }

    @Override
    public void buatReservasi(int idUser, int idSchedule, String nama, String hp, List<String> kursiList) throws SQLException {
        Connection conn = null;
        try {
            conn = Koneksi.getKoneksi();
            conn.setAutoCommit(false);

            String kode = "RSV" + System.currentTimeMillis();

            // Insert ke tabel reservations
            int reservationId = reservationRepository.insertReservation(conn, idUser, idSchedule, nama, hp, kode);

            // Insert ke tabel reservation_details untuk setiap kursi
                reservationRepository.insertReservationDetails(conn, reservationId, kursiList);
           
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
    
    @Override
    public List<Reservation> getReservationsByUserId(int userId) throws SQLException {
        return reservationRepository.getReservationsByUserId(userId);
    }
    

    @Override
    public Reservation getReservationByKode(String kode) throws SQLException {
        return reservationRepository.getReservationByKode(kode);
    }
    
//    // Option 1: For status-based cancellation (keeping records in the database)
//    @Override
//    public void cancelReservation(int id, String alasan) throws SQLException {
//        Connection conn = null;
//        try {
//            conn = Koneksi.getKoneksi();
//            conn.setAutoCommit(false);
//
//            // Verifikasi reservasi ada
//            Reservation reservasi = reservationRepository.getReservationById(id);
//            if (reservasi == null) {
//                throw new SQLException("Reservasi dengan ID " + id + " tidak ditemukan");
//            }
//
//            // Update status langsung di tabel reservations
//            reservationRepository.updateReservationStatus(id, "Dibatalkan", alasan);
//
//            conn.commit();
//        } catch (Exception e) {
//            if (conn != null) {
//                conn.rollback();
//            }
//            throw new SQLException("Gagal membatalkan reservasi: " + e.getMessage(), e);
//        } finally {
//            if (conn != null) {
//                conn.close();
//            }
//        }
//    }
//    
    
    @Override
    public boolean kursiSudahDipesanAtauDibayar(int idSchedule, String kursi) throws SQLException {
        Connection conn = null;
        try {
            conn = Koneksi.getKoneksi();
            return reservationRepository.isSeatReservedOrPaid(conn, idSchedule, kursi);
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    // ===== BUSINESS LOGIC UNTUK DISPLAY =====
    
    /**
     * Mendapatkan warna background berdasarkan status reservasi
     */
    public Color getStatusBackgroundColor(String status) {
        if (status == null) return DEFAULT_BG;
        
        String statusLower = status.toLowerCase().trim();
        
        if (statusLower.contains("dibatalkan")) {
            return CANCELED_BG;
        } else if (statusLower.contains("dipesan")) {
            return RESERVED_BG;
        } else if (statusLower.contains("dibayar")) {
            return PAID_BG;
        }
        
        return DEFAULT_BG;
    }
    
    /**
     * Mendapatkan warna foreground/text berdasarkan status reservasi
     */
    public Color getStatusForegroundColor(String status) {
        if (status == null) return DEFAULT_FG;
        
        String statusLower = status.toLowerCase().trim();
        
        if (statusLower.contains("dibatalkan")) {
            return CANCELED_FG;
        } else if (statusLower.contains("dipesan")) {
            return RESERVED_FG;
        } else if (statusLower.contains("dibayar")) {
            return PAID_FG;
        }
        
        return DEFAULT_FG;
    }
    
    /**
     * Mengecek apakah status adalah status yang dibatalkan
     */
    public boolean isStatusCanceled(String status) {
        return status != null && status.toLowerCase().trim().contains("dibatalkan");
    }
    
    /**
     * Mengecek apakah status adalah status yang dipesan
     */
    public boolean isStatusReserved(String status) {
        return status != null && status.toLowerCase().trim().contains("dipesan");
    }
    
    /**
     * Mengecek apakah status adalah status yang dibayar
     */
    public boolean isStatusPaid(String status) {
        return status != null && status.toLowerCase().trim().contains("dibayar");
    }
    
    /**
     * Mendapatkan display text yang terformat untuk status
     */
    public String getFormattedStatus(String status) {
        if (status == null) return "Unknown";
        
        String statusLower = status.toLowerCase().trim();
        
        if (statusLower.contains("dibatalkan")) {
            return "Dibatalkan";
        } else if (statusLower.contains("dipesan")) {
            return "Dipesan";
        } else if (statusLower.contains("dibayar")) {
            return "Dibayar";
        }
        
        return status; // Return original jika tidak match
    }
}