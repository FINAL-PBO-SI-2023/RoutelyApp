package routelyapp.service;

import java.awt.*;
import routelyapp.model.Reservation;

import java.sql.SQLException;
import java.util.List;

public interface ReservationService {
    List<Reservation> getAllReservations() throws SQLException;
    void changeStatus(int id, String status) throws SQLException;
    Reservation getReservationById(int id) throws Exception;
    void removeReservation(int id,String alasan) throws SQLException;
    int ambilKapasitasBus(int idSchedule) throws SQLException;
    boolean kursiSudahDipesan(int idSchedule, String kursi) throws SQLException;
    void buatReservasi(int idUser, int idSchedule, String nama, String hp, List<String> kursiList) throws SQLException;
    List<Reservation> getReservationsByUserId(int userId) throws SQLException;
    Reservation getReservationByKode(String kode) throws SQLException;
//    public void cancelReservation(int id, String alasan) throws SQLException;
    boolean kursiSudahDipesanAtauDibayar(int idSchedule, String kursi) throws SQLException;
    
    Color getStatusBackgroundColor(String status);
    Color getStatusForegroundColor(String status);
    boolean isStatusCanceled(String status);
    boolean isStatusReserved(String status);
    boolean isStatusPaid(String status);
    String getFormattedStatus(String status);
}
