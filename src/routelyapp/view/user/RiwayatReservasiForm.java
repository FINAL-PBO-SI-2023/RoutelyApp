package routelyapp.view.user;

import com.formdev.flatlaf.FlatIntelliJLaf;
import routelyapp.model.Reservation;
import routelyapp.repository.ReservationRepositoryImpl;
import routelyapp.service.ReservationService;
import routelyapp.service.ReservationServiceImpl;
import routelyapp.view.dashboard.UserDashboard;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class RiwayatReservasiForm extends JFrame {
    private int idUser;
    private final ReservationService reservationService;

    private JTable tableReservasi;
    private JButton btnKembali;

    public RiwayatReservasiForm(int idUser) {
        this.idUser = idUser;
        this.reservationService = new ReservationServiceImpl(new ReservationRepositoryImpl());

        InitUI();
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        loadRiwayat();
    }

    private void InitUI() {
        setTitle("Riwayat Reservasi");
        setSize(1200, 700);
        setLayout(new BorderLayout());
        
        // Set background color
        getContentPane().setBackground(new Color(245, 245, 245));

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        
        JLabel titleLabel = new JLabel("Riwayat Reservasi", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(51, 51, 51));
        titlePanel.add(titleLabel);
        
        add(titlePanel, BorderLayout.NORTH);

        // Main Panel untuk tabel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Tabel
        tableReservasi = new JTable();
        tableReservasi.setRowHeight(35);
        tableReservasi.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableReservasi.setSelectionBackground(new Color(230, 240, 255));
        tableReservasi.setGridColor(new Color(220, 220, 220));
        tableReservasi.setSelectionForeground(Color.BLACK);
        tableReservasi.setShowGrid(true);
        tableReservasi.setIntercellSpacing(new Dimension(1, 1));
        
        // Style header
        JTableHeader header = tableReservasi.getTableHeader();
        header.setBackground(new Color(52, 144, 220));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        
        // Center align cell content
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        JScrollPane scrollPane = new JScrollPane(tableReservasi);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        btnKembali = new JButton("Kembali");
        btnKembali.setPreferredSize(new Dimension(100, 35));
        btnKembali.setFocusPainted(false);
        btnKembali.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnKembali.setBackground(new Color(108, 117, 125));
        btnKembali.setForeground(Color.WHITE);
        btnKembali.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnKembali.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        btnKembali.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnKembali.setBackground(new Color(90, 98, 104));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnKembali.setBackground(new Color(108, 117, 125));
            }
        });
        
        btnKembali.addActionListener(e -> {
            new UserDashboard(idUser).setVisible(true);
            dispose();
        });
        
        bottomPanel.add(btnKembali);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadRiwayat() {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Header kolom sesuai dengan Data Reservasi
        model.addColumn("Kode Reservasi");
        model.addColumn("Bus");
        model.addColumn("Nama");
        model.addColumn("No HP");
        model.addColumn("Kursi");
        model.addColumn("Asal");
        model.addColumn("Tujuan");
        model.addColumn("Tanggal Keberangkatan");
        model.addColumn("Jam");
        model.addColumn("Status");

        try {
            List<Reservation> reservations = reservationService.getReservationsByUserId(idUser);
            for (Reservation res : reservations) {
                model.addRow(new Object[]{
                    res.getKodeReservasi(),
                    res.getNamaBus(),
                    res.getNamaPenumpang(),
                    res.getNoHp(),
                    res.getKursi(),
                    res.getAsal(),
                    res.getTujuan(),
                    res.getTanggal(),
                    res.getJam(),
                    res.getStatus()
                });
            }

            tableReservasi.setModel(model);
            tableReservasi.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            // Set column widths untuk proportional sizing
            int[] columnWidths = {150, 80, 120, 100, 70, 80, 80, 150, 80, 100};
            for (int i = 0; i < columnWidths.length && i < tableReservasi.getColumnModel().getColumnCount(); i++) {
                tableReservasi.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
                
                // Center align semua kolom
                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                centerRenderer.setHorizontalAlignment(JLabel.CENTER);
                tableReservasi.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            // Khusus untuk kolom Status, beri warna berbeda
            if (tableReservasi.getColumnModel().getColumnCount() > 9) {
                tableReservasi.getColumnModel().getColumn(9).setCellRenderer(new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, 
                            boolean isSelected, boolean hasFocus, int row, int column) {
                        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        setHorizontalAlignment(JLabel.CENTER);
                        
                        if (!isSelected && value != null) {
                            String status = value.toString();
                            // Menggunakan business logic dari service
                            c.setBackground(reservationService.getStatusBackgroundColor(status));
                            setForeground(reservationService.getStatusForegroundColor(status));
                        }
                        
                        return c;
                    }
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Gagal memuat riwayat reservasi: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 461, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

        public static void main(String[] args) {
            try {
                UIManager.setLookAndFeel(new FlatIntelliJLaf());
            } catch (Exception e) {
                System.err.println("Gagal mengatur FlatLaf");
            }

            SwingUtilities.invokeLater(() -> new RiwayatReservasiForm(1).setVisible(true));
        }
    }
