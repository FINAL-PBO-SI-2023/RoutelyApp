package routelyapp.koneksi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {
    private static Connection conn;

    public static Connection getKoneksi() throws SQLException {
        if (conn == null || conn.isClosed()) {
            try {
                String url = "jdbc:mysql://localhost:3306/routely";
                String user = "root";
                String pass = ""; 
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(url, user, pass);
            } catch (ClassNotFoundException | SQLException e) {
                throw new SQLException("Gagal koneksi: " + e.getMessage());
            }
        }
        return conn;
    }
}
