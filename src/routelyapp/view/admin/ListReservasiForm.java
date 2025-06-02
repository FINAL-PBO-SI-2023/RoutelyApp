package routelyapp.view.admin;

import routelyapp.service.ReservationService;
import routelyapp.service.ReservationServiceImpl;
import routelyapp.model.Reservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import routelyapp.repository.ReservationRepositoryImpl;
import routelyapp.view.dashboard.AdminDashboard;
import com.formdev.flatlaf.FlatLightLaf;
import com.toedter.calendar.JDateChooser;
import javax.swing.table.DefaultTableCellRenderer;

public class ListReservasiForm extends JFrame {
    private ReservationService reservationService;
    private int selectedId = -1;
    
    // Components
    private JLabel jLabel1;
    private JScrollPane scrollReservasi;
    private JTable tableReservasi;
    private JButton btnKembali;
    private JButton btnUbahStatus;
    private JButton btnHapus;
    private JTextField txtFilterNama;
    private com.toedter.calendar.JDateChooser dateChooserTanggal;
    private JButton btnFilter;
    private JButton btnResetFilter;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    public ListReservasiForm() {
        // Set FlatLaf Look and Feel
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        initUI();
        initializeService();
        setupTable();
        loadReservasi();
        setupEventListeners();
        
        // Set fullscreen/maximized
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private void initUI() {
        setTitle("Data Reservasi - Routely App");
        setLayout(new BorderLayout());
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        
        // Filter panel
        JPanel filterPanel = createFilterPanel();
        
        // Table panel
        JPanel tablePanel = createTablePanel();
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        
        // Add components to main panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.CENTER);
        topPanel.setBackground(Color.WHITE);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        jLabel1 = new JLabel("Data Reservasi");
        jLabel1.setFont(new Font("Segoe UI", Font.BOLD, 24));
        jLabel1.setForeground(new Color(51, 51, 51));
        
        headerPanel.add(jLabel1);
        return headerPanel;
    }
    
    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            "Filter Pencarian",
            0, 0, new Font("Segoe UI", Font.PLAIN, 12), new Color(100, 100, 100)
        ));
        
        // Nama filter
        JLabel lblNama = new JLabel("Nama:");
        lblNama.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtFilterNama = new JTextField(15);
        txtFilterNama.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtFilterNama.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(5, 8, 5, 8)
        ));
        
        // Tanggal filter
        JLabel lblTanggal = new JLabel("Tanggal:");
        lblTanggal.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        dateChooserTanggal = new JDateChooser();
        dateChooserTanggal.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateChooserTanggal.setPreferredSize(new Dimension(130, 28));
        dateChooserTanggal.setDateFormatString("yyyy-MM-dd");
        dateChooserTanggal.setBackground(Color.WHITE);

        // Ganti txtFilterTanggal menjadi dateChooserTanggal
        filterPanel.add(lblTanggal);
        filterPanel.add(dateChooserTanggal);

        
        // Filter buttons
        btnFilter = createStyledButton("Filter", new Color(52, 152, 219), Color.WHITE);
        btnResetFilter = createStyledButton("Reset", new Color(149, 165, 166), Color.WHITE);
        
        filterPanel.add(lblNama);
        filterPanel.add(txtFilterNama);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(lblTanggal);
        filterPanel.add(dateChooserTanggal);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(btnFilter);
        filterPanel.add(btnResetFilter);
        
        return filterPanel;
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        // Create table
        tableReservasi = new JTable();
        tableReservasi.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableReservasi.setRowHeight(30);
        tableReservasi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableReservasi.setGridColor(new Color(230, 230, 230));
        tableReservasi.setSelectionBackground(new Color(52, 152, 219, 50));
        tableReservasi.setSelectionForeground(Color.BLACK);
        
        // Header styling
        tableReservasi.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tableReservasi.getTableHeader().setBackground(new Color(52, 152, 219));
        tableReservasi.getTableHeader().setForeground(Color.WHITE);
        tableReservasi.getTableHeader().setPreferredSize(new Dimension(0, 35));
        
        scrollReservasi = new JScrollPane(tableReservasi);
        scrollReservasi.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scrollReservasi.getViewport().setBackground(Color.WHITE);
        
        tablePanel.add(scrollReservasi, BorderLayout.CENTER);
        
        // 💡 Tambahkan ini agar isi tabel rata tengah
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tableReservasi.setDefaultRenderer(Object.class, centerRenderer);
    
        return tablePanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        btnUbahStatus = createStyledButton("Ubah Status", new Color(46, 204, 113), Color.WHITE);
        btnHapus = createStyledButton("Hapus", new Color(231, 76, 60), Color.WHITE);
        btnKembali = createStyledButton("Kembali", new Color(149, 165, 166), Color.WHITE);
        
        buttonPanel.add(btnUbahStatus);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnKembali);
        
        return buttonPanel;
    }
    
    private JButton createStyledButton(String text, Color bgColor, Color textColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void initializeService() {
        try {
            ReservationRepositoryImpl repo = new ReservationRepositoryImpl();
            reservationService = new ReservationServiceImpl(repo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal inisialisasi service: " + e.getMessage());
        }
    }
    
    private void setupTable() {
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Kode Reservasi");
        tableModel.addColumn("Bus");
        tableModel.addColumn("Nama");
        tableModel.addColumn("No HP");
        tableModel.addColumn("Kursi");
        tableModel.addColumn("Asal");
        tableModel.addColumn("Tujuan");
        tableModel.addColumn("Tanggal Keberangkatan");
        tableModel.addColumn("Jam");
        tableModel.addColumn("Terminal");
        tableModel.addColumn("Status");
        
        tableReservasi.setModel(tableModel);
        
        // Hide ID column
        tableReservasi.removeColumn(tableReservasi.getColumnModel().getColumn(0));
        
        // Set column widths
        tableReservasi.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableReservasi.getColumnModel().getColumn(0).setPreferredWidth(150);  // Kode
        tableReservasi.getColumnModel().getColumn(1).setPreferredWidth(120);  // Bus
        tableReservasi.getColumnModel().getColumn(2).setPreferredWidth(200);  // Nama
        tableReservasi.getColumnModel().getColumn(3).setPreferredWidth(100);  // No HP
        tableReservasi.getColumnModel().getColumn(4).setPreferredWidth(80);   // Kursi
        tableReservasi.getColumnModel().getColumn(5).setPreferredWidth(100);  // Asal
        tableReservasi.getColumnModel().getColumn(6).setPreferredWidth(100);  // Tujuan
        tableReservasi.getColumnModel().getColumn(7).setPreferredWidth(200);   // Tanggal
        tableReservasi.getColumnModel().getColumn(8).setPreferredWidth(100);  // Jam
        tableReservasi.getColumnModel().getColumn(9).setPreferredWidth(100);  // Terminal
        tableReservasi.getColumnModel().getColumn(9).setPreferredWidth(100);  // Status
    }
    
    private void setupEventListeners() {
        // Table selection listener
        tableReservasi.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tableReservasi.getSelectedRow();
                if (row != -1) {
                    int modelRow = tableReservasi.convertRowIndexToModel(row);
                    selectedId = Integer.parseInt(tableModel.getValueAt(modelRow, 0).toString());
                }
            }
        });
        
        // Button listeners
        btnKembali.addActionListener(this::btnKembaliActionPerformed);
        btnUbahStatus.addActionListener(this::btnUbahStatusActionPerformed);
        btnHapus.addActionListener(this::btnHapusActionPerformed);
        btnFilter.addActionListener(this::btnFilterActionPerformed);
        btnResetFilter.addActionListener(this::btnResetFilterActionPerformed);
        
        // Enter key listeners for filter fields
        txtFilterNama.addActionListener(this::btnFilterActionPerformed);
        dateChooserTanggal.getDateEditor().addPropertyChangeListener(evt -> {
            if ("date".equals(evt.getPropertyName())) {
                btnFilterActionPerformed(null);
            }
        });
    }
    
    private void btnKembaliActionPerformed(ActionEvent evt) {
        new AdminDashboard().setVisible(true);
        this.dispose();
    }
    
    private void btnUbahStatusActionPerformed(ActionEvent evt) {
        if (selectedId == -1) {
            showErrorMessage("Pilih reservasi terlebih dahulu.");
            return;
        }

        int row = tableReservasi.getSelectedRow();
        if (row == -1) return;
        
        int modelRow = tableReservasi.convertRowIndexToModel(row);
        String statusSekarang = tableModel.getValueAt(modelRow, 11).toString();

        String[] options = {"Dipesan", "Dibayar", "Dibatalkan"};
        int defaultOption = getDefaultStatusOption(statusSekarang);

        String statusBaru = (String) JOptionPane.showInputDialog(
                this,
                "Pilih status baru:",
                "Ubah Status Reservasi",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[defaultOption]);

        if (statusBaru != null && !statusBaru.equals(statusSekarang)) {
            int konfirmasi = JOptionPane.showConfirmDialog(this,
                    "Ubah status dari '" + statusSekarang + "' ke '" + statusBaru + "'?",
                    "Konfirmasi", JOptionPane.YES_NO_OPTION);

            if (konfirmasi == JOptionPane.YES_OPTION) {
                try {
                    reservationService.changeStatus(selectedId, statusBaru);
                    showSuccessMessage("Status berhasil diubah.");
                    loadReservasi();
                    selectedId = -1;
                } catch (Exception e) {
                    showErrorMessage("Gagal ubah status: " + e.getMessage());
                }
            }
        }
    }
    
    private void btnHapusActionPerformed(ActionEvent evt) {
        if (selectedId == -1) {
            showErrorMessage("Pilih reservasi terlebih dahulu.");
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Yakin ingin menghapus reservasi ini dari database?\n" +
                "Data yang dihapus tidak dapat dikembalikan.",
                "Konfirmasi Penghapusan", JOptionPane.YES_NO_OPTION);

        if (konfirmasi == JOptionPane.YES_OPTION) {
            try {
                reservationService.removeReservation(selectedId, "");
                showSuccessMessage("Reservasi berhasil dihapus dari database.");
                loadReservasi();
                selectedId = -1;
            } catch (Exception e) {
                showErrorMessage("Gagal menghapus reservasi: " + e.getMessage());
            }
        }
    }
    
    private void btnFilterActionPerformed(ActionEvent evt) {
        String nama = txtFilterNama.getText().trim();
        String tanggal = "";
        if (dateChooserTanggal.getDate() != null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            tanggal = sdf.format(dateChooserTanggal.getDate());
        }

        
        if (nama.isEmpty() && tanggal.isEmpty()) {
            loadReservasi();
            return;
        }
        
        try {
            // Sementara menggunakan filtering manual sampai repository method diperbaiki
            List<Reservation> allReservations = reservationService.getAllReservations();
            List<Reservation> filteredList = new ArrayList<>();
            
            for (Reservation r : allReservations) {
                boolean matchNama = nama.isEmpty() || 
                    (r.getNamaPenumpang() != null && r.getNamaPenumpang().toLowerCase().contains(nama.toLowerCase()));
                
                boolean matchTanggal = tanggal.isEmpty() || 
                    (r.getTanggal() != null && r.getTanggal().toString().contains(tanggal));
                
                if (matchNama && matchTanggal) {
                    filteredList.add(r);
                }
            }
            
            populateTable(filteredList);
            
            // Show info about filtered results
            if (!nama.isEmpty() || !tanggal.isEmpty()) {
                String filterInfo = "Filter diterapkan: ";
                if (!nama.isEmpty()) filterInfo += "Nama='" + nama + "' ";
                if (!tanggal.isEmpty()) filterInfo += "Tanggal='" + tanggal + "' ";
                filterInfo += "| Hasil: " + filteredList.size() + " data";
                
                JOptionPane.showMessageDialog(this, filterInfo, "Info Filter", JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception e) {
            showErrorMessage("Gagal melakukan filter: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void btnResetFilterActionPerformed(ActionEvent evt) {
        txtFilterNama.setText("");
        dateChooserTanggal.setDate(null);
        loadReservasi();
    }
    
    private void loadReservasi() {
        try {
            List<Reservation> list = reservationService.getAllReservations();
            populateTable(list);
        } catch (Exception e) {
            showErrorMessage("Gagal load reservasi: " + e.getMessage());
        }
    }
    
    private void populateTable(List<Reservation> reservations) {
        tableModel.setRowCount(0);
        
        for (Reservation r : reservations) {
            tableModel.addRow(new Object[]{
                r.getId(),
                r.getKodeReservasi(),
                r.getNamaBus(),
                r.getNamaPenumpang(),
                r.getNoHp(),
                r.getKursi(),
                r.getAsal(),
                r.getTujuan(),
                r.getTanggal(),
                r.getJam(),
                r.getTerminal(),
                r.getStatus()
            });
        }
    }
    
    private int getDefaultStatusOption(String currentStatus) {
        switch (currentStatus) {
            case "Dibayar":
                return 1;
            case "Dibatalkan":
                return 2;
            default:
                return 0;
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
            .addGap(0, 483, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> {
            new ListReservasiForm().setVisible(true);
        });
    }
}