package routelyapp.view.user;

import com.formdev.flatlaf.FlatIntelliJLaf;
import routelyapp.view.dashboard.UserDashboard;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import routelyapp.repository.ReservationRepositoryImpl;
import routelyapp.service.ReservationService;
import routelyapp.service.ReservationServiceImpl;

public class FormReservasi extends JFrame {
    private int idSchedule;
    private int idUser;
    private ReservationService reservationService;
    
    // UI Components
    private JTextField txtNama;
    private JTextField txtNoHp;
    private JTextField txtKursiDipilih;
    private JButton btnReservasi;
    private JButton btnKembali;
    private JPanel panelKursi;
    private JScrollPane scrollPaneKursi;
    private JLabel lblSelectedSeat;
    private JButton selectedSeatButton;

    public FormReservasi(int idSchedule, int idUser) {
        this.idSchedule = idSchedule;
        this.idUser = idUser;

        reservationService = new ReservationServiceImpl(
                new ReservationRepositoryImpl()
        );

        initializeFlatLaf();
        InitUI();
        setupUI();
        buatTombolKursi();
    }

    public FormReservasi() {
        initializeFlatLaf();
        initComponents();
        setupUI();
    }

    private void initializeFlatLaf() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            // Custom colors
            UIManager.put("Button.arc", 8);
            UIManager.put("Component.focusWidth", 1);
            UIManager.put("TextField.arc", 8);
            UIManager.put("ScrollPane.arc", 8);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Reservasi Tiket Bus - Pilih Kursi");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Initialize components
        txtNama = new JTextField();
        txtNoHp = new JTextField();
        txtKursiDipilih = new JTextField();
        txtKursiDipilih.setEditable(false);
        
        btnReservasi = new JButton("Buat Reservasi");
        btnKembali = new JButton("Kembali");
        
        panelKursi = new JPanel();
        scrollPaneKursi = new JScrollPane(panelKursi);
        
        lblSelectedSeat = new JLabel("Belum ada kursi dipilih");
    }

    private void setupUI() {
        setLayout(new BorderLayout(20, 20));
        
        // Main container with padding
        JPanel mainContainer = new JPanel(new BorderLayout(20, 20));
        mainContainer.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainContainer.setBackground(new Color(248, 249, 250));
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        
        // Center Panel (Seat Selection)
        JPanel centerPanel = createSeatSelectionPanel();
        
        // Right Panel (Form)
        JPanel rightPanel = createFormPanel();
        
        // Footer Panel
        JPanel footerPanel = createFooterPanel();
        
        mainContainer.add(headerPanel, BorderLayout.NORTH);
        mainContainer.add(centerPanel, BorderLayout.CENTER);
        mainContainer.add(rightPanel, BorderLayout.EAST);
        mainContainer.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainContainer);
        
        // Setup button actions
        setupButtonActions();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(248, 249, 250));
        
        JLabel titleLabel = new JLabel("Pilih Kursi dan Lengkapi Data Reservasi");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 37, 41));
        
        headerPanel.add(titleLabel);
        return headerPanel;
    }

    private JPanel createSeatSelectionPanel() {
        JPanel seatPanel = new JPanel(new BorderLayout(10, 10));
        seatPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
            "Layout Kursi Bus",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            new Color(73, 80, 87)
        ));
        seatPanel.setBackground(Color.WHITE);
        
        // Legend Panel
        JPanel legendPanel = createLegendPanel();
        
        // Seat layout panel
        panelKursi.setBackground(Color.WHITE);
        scrollPaneKursi.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPaneKursi.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneKursi.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPaneKursi.getVerticalScrollBar().setUnitIncrement(16);
        
        seatPanel.add(legendPanel, BorderLayout.NORTH);
        seatPanel.add(scrollPaneKursi, BorderLayout.CENTER);
        
        return seatPanel;
    }

    private JPanel createLegendPanel() {
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        legendPanel.setBackground(Color.WHITE);
        
        // Available seat legend
        JPanel availableLegend = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        availableLegend.setBackground(Color.WHITE);
        JButton availableBtn = new JButton();
        availableBtn.setPreferredSize(new Dimension(30, 30));
        availableBtn.setBackground(new Color(40, 167, 69));
        availableBtn.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 58), 1));
        availableBtn.setEnabled(true);
        JLabel availableLabel = new JLabel("Tersedia");
        availableLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        availableLegend.add(availableBtn);
        availableLegend.add(availableLabel);
        
        // Occupied seat legend
        JPanel occupiedLegend = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        occupiedLegend.setBackground(Color.WHITE);
        JButton occupiedBtn = new JButton();
        occupiedBtn.setPreferredSize(new Dimension(30, 30));
        occupiedBtn.setBackground(new Color(108, 117, 125));
        occupiedBtn.setBorder(BorderFactory.createLineBorder(new Color(73, 80, 87), 1));
        occupiedBtn.setEnabled(true);
        JLabel occupiedLabel = new JLabel("Terisi");
        occupiedLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        occupiedLegend.add(occupiedBtn);
        occupiedLegend.add(occupiedLabel);
        
        // Selected seat legend
        JPanel selectedLegend = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        selectedLegend.setBackground(Color.WHITE);
        JButton selectedBtn = new JButton();
        selectedBtn.setPreferredSize(new Dimension(30, 30));
        selectedBtn.setBackground(new Color(0, 123, 255));
        selectedBtn.setBorder(BorderFactory.createLineBorder(new Color(0, 86, 179), 1));
        selectedBtn.setEnabled(true);
        JLabel selectedLabel = new JLabel("Dipilih");
        selectedLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        selectedLegend.add(selectedBtn);
        selectedLegend.add(selectedLabel);
        
        legendPanel.add(availableLegend);
        legendPanel.add(occupiedLegend);
        legendPanel.add(selectedLegend);
        
        return legendPanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
            "Data Penumpang",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            new Color(73, 80, 87)
        ));
        formPanel.setBackground(Color.WHITE);
        formPanel.setPreferredSize(new Dimension(500, 0));
        
        // Add padding
        JPanel paddedPanel = new JPanel();
        paddedPanel.setLayout(new BoxLayout(paddedPanel, BoxLayout.Y_AXIS));
        paddedPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        paddedPanel.setBackground(Color.WHITE);
        
        // Selected seat display
        JPanel seatInfoPanel = new JPanel(new BorderLayout());
        seatInfoPanel.setBackground(new Color(230, 242, 255));
        seatInfoPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        seatInfoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        JLabel seatLabel = new JLabel("Kursi Dipilih: ");
        seatLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        seatLabel.setForeground(new Color(73, 80, 87));
        
        lblSelectedSeat.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSelectedSeat.setForeground(new Color(0, 86, 179));
        
        seatInfoPanel.add(seatLabel, BorderLayout.WEST);
        seatInfoPanel.add(lblSelectedSeat, BorderLayout.CENTER);
        
        paddedPanel.add(seatInfoPanel);
        paddedPanel.add(Box.createVerticalStrut(20));
        
        // Name field
        paddedPanel.add(createFormField("Nama Lengkap:", txtNama));
        paddedPanel.add(Box.createVerticalStrut(15));
        
        // Phone field
        paddedPanel.add(createFormField("Nomor HP:", txtNoHp));
        paddedPanel.add(Box.createVerticalStrut(30));
        
        // Reservation button
        btnReservasi.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnReservasi.setBackground(new Color(40, 167, 69));
        btnReservasi.setForeground(Color.WHITE);
        btnReservasi.setPreferredSize(new Dimension(0, 45));
        btnReservasi.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnReservasi.setFocusPainted(false);
        btnReservasi.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        paddedPanel.add(btnReservasi);
        
        formPanel.add(paddedPanel);
        return formPanel;
    }

    private JPanel createFormField(String labelText, JTextField textField) {
    JPanel fieldPanel = new JPanel();
    fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.X_AXIS));
    fieldPanel.setBackground(Color.WHITE);
    fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    fieldPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

    JLabel label = new JLabel(labelText);
    label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    label.setForeground(new Color(73, 80, 87));
    label.setPreferredSize(new Dimension(100, 35)); // label lebar tetap
    label.setHorizontalAlignment(SwingConstants.RIGHT);

    textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    textField.setPreferredSize(new Dimension(0, 35));
    textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
    textField.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
        BorderFactory.createEmptyBorder(8, 12, 8, 12)
    ));

    fieldPanel.add(label);
    fieldPanel.add(Box.createRigidArea(new Dimension(10, 0))); // jarak antara label dan field
    fieldPanel.add(textField);

    return fieldPanel;
}

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footerPanel.setBackground(new Color(248, 249, 250));
        
        btnKembali.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnKembali.setBackground(new Color(108, 117, 125));
        btnKembali.setForeground(Color.WHITE);
        btnKembali.setPreferredSize(new Dimension(100, 40));
        btnKembali.setFocusPainted(false);
        btnKembali.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        
        footerPanel.add(btnKembali);
        return footerPanel;
    }

    private void setupButtonActions() {
        btnReservasi.addActionListener(this::btnReservasiActionPerformed);
        btnKembali.addActionListener(this::btnKembaliActionPerformed);
    }

    private void btnReservasiActionPerformed(ActionEvent evt) {
        String nama = txtNama.getText().trim();
        String hp = txtNoHp.getText().trim();
        String kursi = txtKursiDipilih.getText().trim();

        if (nama.isEmpty() || hp.isEmpty() || kursi.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Semua data harus diisi!\nPastikan Anda telah memilih kursi dan mengisi nama serta nomor HP.", 
                "Data Tidak Lengkap", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<String> kursiList = new ArrayList<>();
        kursiList.add(kursi);

        try {
            if (reservationService.kursiSudahDipesan(idSchedule, kursi)) {
                JOptionPane.showMessageDialog(this, 
                    "Kursi " + kursi + " sudah dipesan oleh penumpang lain!\nSilakan pilih kursi yang tersedia.", 
                    "Kursi Tidak Tersedia", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            reservationService.buatReservasi(idUser, idSchedule, nama, hp, kursiList);
            
            JOptionPane.showMessageDialog(this, 
                "Reservasi berhasil dibuat!\n\nDetail Reservasi:\nNama: " + nama + "\nKursi: " + kursi + "\nHP: " + hp, 
                "Reservasi Berhasil", 
                JOptionPane.INFORMATION_MESSAGE);

            new UserDashboard(idUser).setVisible(true);
            this.dispose();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Gagal membuat reservasi!\n\nError: " + e.getMessage() + 
                "\n\nSilakan coba lagi atau hubungi administrator.", 
                "Error Database", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnKembaliActionPerformed(ActionEvent evt) {
        new LihatJadwalForm(idUser).setVisible(true);
        this.dispose();
    }

    private void buatTombolKursi() {
        panelKursi.removeAll();

        int kapasitas = ambilKapasitasBus(idSchedule);
        int baris = (int) Math.ceil(kapasitas / 4.0);
        
        // Create custom layout for seats
        panelKursi.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Add title for bus layout
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 5;
        gbc.insets = new Insets(10, 10, 20, 10);
        JLabel busTitle = new JLabel("🚌 BAGIAN DEPAN BUS", SwingConstants.CENTER);
        busTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        busTitle.setForeground(new Color(73, 80, 87));
        panelKursi.add(busTitle, gbc);

        int nomor = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);

        for (int row = 0; row < baris; row++) {
            for (int col = 0; col < 5; col++) {
                gbc.gridx = col;
                gbc.gridy = row + 1; // +1 because row 0 is for title
                
                if (col == 2) {
                    // Middle column (aisle)
                    JLabel aisleLabel = new JLabel("", SwingConstants.CENTER);
                    aisleLabel.setPreferredSize(new Dimension(40, 60));
                    panelKursi.add(aisleLabel, gbc);
                } else if (nomor <= kapasitas) {
                    String kode = "K" + nomor;
                    JButton btn = createSeatButton(kode);
                    panelKursi.add(btn, gbc);
                    nomor++;
                } else {
                    // Empty space if capacity is reached
                    JLabel emptyLabel = new JLabel();
                    emptyLabel.setPreferredSize(new Dimension(60, 60));
                    panelKursi.add(emptyLabel, gbc);
                }
            }
        }

        // Add back of bus label
        gbc.gridx = 0; gbc.gridy = baris + 1;
        gbc.gridwidth = 5;
        gbc.insets = new Insets(20, 10, 10, 10);
        JLabel backTitle = new JLabel("🚌 BAGIAN BELAKANG BUS", SwingConstants.CENTER);
        backTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        backTitle.setForeground(new Color(73, 80, 87));
        panelKursi.add(backTitle, gbc);

        panelKursi.revalidate();
        panelKursi.repaint();
    }

    private JButton createSeatButton(String kode) {
        JButton btn = new JButton(kode);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setPreferredSize(new Dimension(60, 60));
        btn.setMinimumSize(new Dimension(60, 60));
        btn.setMaximumSize(new Dimension(60, 60));
        btn.setFocusPainted(false);
        
        if (kursiSudahDipesan(kode)) {
            // Occupied seat
            btn.setEnabled(false);
            btn.setBackground(new Color(108, 117, 125));
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createLineBorder(new Color(73, 80, 87), 2));
            btn.setToolTipText("Kursi " + kode + " sudah terisi");
        } else {
            // Available seat
            btn.setBackground(new Color(40, 167, 69));
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 58), 2));
            btn.setToolTipText("Klik untuk memilih kursi " + kode);
            
            btn.addActionListener(e -> {
                selectSeat(kode, btn);
            });
            
            // Hover effect
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    if (btn.isEnabled() && !kode.equals(txtKursiDipilih.getText())) {
                        btn.setBackground(new Color(34, 139, 58));
                    }
                }
                
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if (btn.isEnabled() && !kode.equals(txtKursiDipilih.getText())) {
                        btn.setBackground(new Color(40, 167, 69));
                    }
                }
            });
        }
        
        return btn;
    }

    private void selectSeat(String kode, JButton btn) {
        // Reset previous selection
        if (selectedSeatButton != null && selectedSeatButton != btn) {
            selectedSeatButton.setBackground(new Color(40, 167, 69));
            selectedSeatButton.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 58), 2));
        }
        
        // Select new seat
        txtKursiDipilih.setText(kode);
        lblSelectedSeat.setText(kode);
        selectedSeatButton = btn;
        
        btn.setBackground(new Color(0, 123, 255));
        btn.setBorder(BorderFactory.createLineBorder(new Color(0, 86, 179), 2));
        
        // Visual feedback
        Timer timer = new Timer(100, new ActionListener() {
            int count = 0;
            public void actionPerformed(ActionEvent e) {
                if (count % 2 == 0) {
                    btn.setBackground(new Color(0, 86, 179));
                } else {
                    btn.setBackground(new Color(0, 123, 255));
                }
                count++;
                if (count >= 4) {
                    ((Timer) e.getSource()).stop();
                    btn.setBackground(new Color(0, 123, 255));
                }
            }
        });
        timer.start();
    }

    private boolean kursiSudahDipesan(String kursi) {
        try {
            return reservationService.kursiSudahDipesan(idSchedule, kursi);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Gagal memeriksa status kursi: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return true; // Assume occupied if error occurs
        }
    }

    private int ambilKapasitasBus(int idSchedule) {
        try {
            return reservationService.ambilKapasitasBus(idSchedule);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Gagal mengambil kapasitas bus: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return 20; // Default capacity if error occurs
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 406, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 495, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
   public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatIntelliJLaf());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            new FormReservasi(1, 1).setVisible(true); // contoh
        });
    }
}