package routelyapp.repository;

import java.sql.Connection;
import routelyapp.model.Reservation;
import java.sql.SQLException;
import java.util.List;

public interface ReservationRepository {
    List<Reservation> findAllWithDetails() throws SQLException;
    void updateStatus(int id, String status) throws SQLException;
    Reservation getReservationById(int id) throws Exception;
    int ambilKapasitasBus(int idSchedule) throws SQLException;
    boolean kursiSudahDipesan(int idSchedule, String kursi) throws SQLException;
    int insertReservation(Connection conn, int idUser, int idSchedule, String nama, String hp, String kode) throws SQLException;
    public void insertReservationDetails(Connection conn, int reservationId, List<String> kursiList) throws SQLException;
    List<Reservation> getReservationsByUserId(int userId) throws SQLException;
    Reservation getReservationByKode(String kode) throws SQLException;
    
    // If you want true deletion instead:
    void deleteReservation(Connection conn, int id) throws SQLException;
    void deleteReservationDetails(Connection conn, int reservationId) throws SQLException;
    String getReservationCode(int id) throws SQLException;
    
    boolean isSeatReservedOrPaid(Connection conn, int scheduleId, String seatCode) throws SQLException;
}
