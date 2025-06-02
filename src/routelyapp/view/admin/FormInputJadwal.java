package routelyapp.view.admin;

import com.formdev.flatlaf.FlatLightLaf;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.border.EmptyBorder;
import routelyapp.model.Bus;
import routelyapp.model.Schedule;
import routelyapp.repository.ScheduleRepositoryImpl;
import routelyapp.service.BusService;
import routelyapp.service.BusServiceImpl;
import routelyapp.service.ScheduleService;
import routelyapp.service.ScheduleServiceImpl;

public class FormInputJadwal extends JFrame {
    private Integer jadwalId = null;
    private final ScheduleService scheduleService;

    private javax.swing.JTextField txtTujuan;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JTextField txtLokasi;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JTextField txtJam;
    private javax.swing.JButton btnBatal;
    private javax.swing.JTextField txtHarga;
    private javax.swing.JButton btnKembali;
    private com.toedter.calendar.JDateChooser tanggalChooser;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JComboBox<String> comboBus;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JTextField txtAsal;
    private javax.swing.JLabel jLabel7;

    private final Color PRIMARY_BLUE = new Color(135, 206, 235);
    private final Color LIGHT_BLUE = new Color(173, 216, 230);
    private final Color DARKER_BLUE = new Color(70, 130, 180);
    private final Color WHITE = Color.WHITE;
    private final Color GRAY_TEXT = new Color(64, 64, 64);
    private final Color SUCCESS_GREEN = new Color(34, 139, 34);
    private final Color DANGER_RED = new Color(220, 53, 69);
    private final Color FIELD_BACKGROUND = new Color(248, 250, 252);

    public FormInputJadwal(Integer id) {
        try {
            FlatLightLaf.setup();
            UIManager.put("Button.arc", 8);
            UIManager.put("Component.arc", 8);
            UIManager.put("TextComponent.arc", 8);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.jadwalId = id;
        this.scheduleService = new ScheduleServiceImpl(new ScheduleRepositoryImpl());

        initUI();
        setupEventHandlers();
        loadBusKeComboBox();

        if (jadwalId != null) {
            loadJadwalById(jadwalId);
        }

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Routely Bus - " + (jadwalId != null ? "Edit Jadwal" : "Tambah Jadwal"));
        setIconImage(createBusIcon());
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 0));
        setBackground(WHITE);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(WHITE);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        JPanel headerPanel = createHeaderPanel();
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(headerPanel);

        JPanel formWrapper = new JPanel();
        formWrapper.setLayout(new BorderLayout());
        formWrapper.setBackground(WHITE);

        JPanel formPanel = createFormPanel();
        formWrapper.add(formPanel, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(formWrapper);
        scrollPane.setBorder(null);
        scrollPane.setBackground(WHITE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(scrollPane);

        JPanel buttonPanel = createButtonPanel();
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(buttonPanel);

        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // 👈 Jarak atas dan bawah
        
        // Title
        String titleText = jadwalId != null ? "✏️ Edit Jadwal Bus" : "➕ Tambah Jadwal Bus";
        jLabel1 = new JLabel(titleText, SwingConstants.CENTER);
        jLabel1.setFont(new Font("Segoe UI Emoji", Font.BOLD, 28));
        jLabel1.setBorder(new EmptyBorder(10, 0, 0, 0));
        jLabel1.setForeground(DARKER_BLUE);
        
        
        // Subtitle
        String subtitleText = jadwalId != null ? "Ubah informasi jadwal perjalanan" : "Buat jadwal perjalanan baru";
        JLabel subtitle = new JLabel(subtitleText, SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        subtitle.setForeground(GRAY_TEXT);
        
        JPanel titlePanel = new JPanel(new BorderLayout(0, 8));
        titlePanel.setBackground(WHITE);
        titlePanel.add(jLabel1, BorderLayout.CENTER);
        titlePanel.add(subtitle, BorderLayout.SOUTH);
        
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Inisialisasi komponen
        initFormComponents();
        
        // Row 1: Bus
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(jLabel2, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(comboBus, gbc);
        
        // Row 2: Asal
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(jLabel3, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(txtAsal, gbc);
        
        // Row 3: Tujuan
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(jLabel5, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(txtTujuan, gbc);
        
        // Row 4: Terminal
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(jLabel7, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(txtLokasi, gbc);
        
        // Row 5: Tanggal
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(jLabel4, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(tanggalChooser, gbc);
        
        // Row 6: Jam
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(jLabel6, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(txtJam, gbc);
        
        // Row 7: Harga
        gbc.gridx = 0; gbc.gridy = 6; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(jLabel8, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(txtHarga, gbc);
        
        return formPanel;
    }
    
    private void initFormComponents() {
        // Labels
        jLabel2 = createStyledLabel("🚌 Pilih Bus");
        jLabel3 = createStyledLabel("🏠 Kota Asal");
        jLabel5 = createStyledLabel("🏢 Kota Tujuan");
        jLabel7 = createStyledLabel("🚏 Terminal");
        jLabel4 = createStyledLabel("📅 Tanggal");
        jLabel6 = createStyledLabel("🕐 Jam (HH:MM)");
        jLabel8 = createStyledLabel("💰 Harga (Rp)");
        
        // Text Fields
        txtAsal = createStyledTextField("Masukkan kota asal");
        txtTujuan = createStyledTextField("Masukkan kota tujuan");
        txtLokasi = createStyledTextField("Masukkan nama terminal");
        txtJam = createStyledTextField("Contoh: 14:30");
        txtHarga = createStyledTextField("Masukkan harga tiket");
        
        // ComboBox
        comboBus = new JComboBox<>();
        comboBus.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        comboBus.setBackground(FIELD_BACKGROUND);
        comboBus.setPreferredSize(new Dimension(0, 60));
        
        // Date Chooser
        tanggalChooser = new com.toedter.calendar.JDateChooser();
        tanggalChooser.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        tanggalChooser.setBackground(FIELD_BACKGROUND);
        tanggalChooser.setPreferredSize(new Dimension(300, 40));
        tanggalChooser.setDateFormatString("dd/MM/yyyy");
    }
    
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        label.setForeground(GRAY_TEXT);
        label.setPreferredSize(new Dimension(150, 30));
        return label;
    }
    
    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        field.setBackground(FIELD_BACKGROUND);
        field.setPreferredSize(new Dimension(0, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_BLUE, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        // Placeholder effect
        field.setForeground(Color.GRAY);
        field.setText(placeholder);
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(DARKER_BLUE, 2),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_BLUE, 1),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });
        
        return field;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(WHITE);
        
        btnSimpan = createStyledButton("💾 Simpan", SUCCESS_GREEN);
        btnBatal = createStyledButton("🔄 Reset", new Color(255, 193, 7));
        btnKembali = createStyledButton("⬅️ Kembali", new Color(108, 117, 125));
        
        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnBatal);
        buttonPanel.add(btnKembali);
        
        return buttonPanel;
    }
    
    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        button.setForeground(WHITE);
        button.setBackground(backgroundColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, 45));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = backgroundColor;
            
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor.darker());
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor);
            }
        });
        
        return button;
    }
    
    private Image createBusIcon() {
        try {
            java.awt.image.BufferedImage icon = new java.awt.image.BufferedImage(32, 32, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = icon.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(PRIMARY_BLUE);
            g2.fillRoundRect(2, 8, 28, 16, 6, 6);
            g2.setColor(WHITE);
            g2.fillOval(6, 20, 6, 6);
            g2.fillOval(20, 20, 6, 6);
            g2.dispose();
            return icon;
        } catch (Exception e) {
            return null;
        }
    }

    private void setupEventHandlers() {
        btnKembali.addActionListener(e -> {
            new ListJadwalForm().setVisible(true);
            dispose();
        });
        
        btnBatal.addActionListener(e -> {
            resetForm();
        });
        
        btnSimpan.addActionListener(e -> {
            saveJadwal();
        });
    }
    
    private void resetForm() {
        txtAsal.setText("Masukkan kota asal");
        txtAsal.setForeground(Color.GRAY);
        txtTujuan.setText("Masukkan kota tujuan");
        txtTujuan.setForeground(Color.GRAY);
        txtLokasi.setText("Masukkan nama terminal");
        txtLokasi.setForeground(Color.GRAY);
        txtJam.setText("Contoh: 14:30");
        txtJam.setForeground(Color.GRAY);
        txtHarga.setText("Masukkan harga tiket");
        txtHarga.setForeground(Color.GRAY);
        tanggalChooser.setDate(null);
        if (comboBus.getItemCount() > 0) {
            comboBus.setSelectedIndex(0);
        }
    }
    
    private String getFieldValue(JTextField field, String placeholder) {
        String value = field.getText().trim();
        return value.equals(placeholder) ? "" : value;
    }
    
    private void saveJadwal() {
        String asal = getFieldValue(txtAsal, "Masukkan kota asal");
        String tujuan = getFieldValue(txtTujuan, "Masukkan kota tujuan");
        String terminal = getFieldValue(txtLokasi, "Masukkan nama terminal");
        Date tanggal = tanggalChooser.getDate();
        String jamStr = getFieldValue(txtJam, "Contoh: 14:30");
        String hargaStr = getFieldValue(txtHarga, "Masukkan harga tiket");

        // Validasi input
        if (asal.isEmpty() || tujuan.isEmpty() || terminal.isEmpty() ||
            tanggal == null || jamStr.isEmpty() || hargaStr.isEmpty() ||
            comboBus.getSelectedItem() == null) {
            showWarningMessage("Harap lengkapi semua data dengan benar.");
            return;
        }

        try {
            int idBus = Integer.parseInt(comboBus.getSelectedItem().toString().split(" - ")[0]);
            double harga = Double.parseDouble(hargaStr);
            Time jam = Time.valueOf(jamStr + ":00");

            Schedule schedule = new Schedule();
            schedule.setIdBus(idBus);
            schedule.setAsal(asal);
            schedule.setTujuan(tujuan);
            schedule.setTerminal(terminal);
            schedule.setTanggal(new java.sql.Date(tanggal.getTime()));
            schedule.setJam(jam);
            schedule.setHarga(harga);

            if (jadwalId != null) {
                schedule.setId(jadwalId);
                scheduleService.update(schedule);
            } else {
                scheduleService.insert(schedule);
            }

            new ListJadwalForm().setVisible(true);
            dispose();

        } catch (NumberFormatException e) {
            showWarningMessage("Format harga tidak valid. Gunakan angka saja.");
        } catch (IllegalArgumentException e) {
            showWarningMessage("Format jam tidak valid. Gunakan format HH:MM (contoh: 14:30)");
        } catch (Exception e) {
            showWarningMessage("Terjadi kesalahan: " + e.getMessage());
        }
    }
    
    private void showWarningMessage(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Peringatan",
            JOptionPane.WARNING_MESSAGE
        );
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 414, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 486, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    private void loadBusKeComboBox() {
        comboBus.removeAllItems();
        BusService busService = new BusServiceImpl();
        List<Bus> buses = busService.getAll();

        comboBus.removeAllItems();
        for (Bus bus : buses) {
            comboBus.addItem(bus.getId() + " - " + bus.getNamaBus());
        }
    }
    
    private void loadJadwalById(int id) {
        try {
            Schedule schedule = scheduleService.getById(id);
            if (schedule != null) {
                // Set field values dan ubah warna text
                txtAsal.setText(schedule.getAsal());
                txtAsal.setForeground(Color.BLACK);
                txtTujuan.setText(schedule.getTujuan());
                txtTujuan.setForeground(Color.BLACK);
                txtLokasi.setText(schedule.getTerminal());
                txtLokasi.setForeground(Color.BLACK);
                tanggalChooser.setDate(schedule.getTanggal());
                txtJam.setText(schedule.getJam().toString().substring(0, 5)); // HH:MM format
                txtJam.setForeground(Color.BLACK);
                txtHarga.setText(String.valueOf((int)schedule.getHarga()));
                txtHarga.setForeground(Color.BLACK);

                // Pilih bus yang sesuai
                int idBus = schedule.getIdBus();
                for (int i = 0; i < comboBus.getItemCount(); i++) {
                    if (comboBus.getItemAt(i).startsWith(idBus + " -")) {
                        comboBus.setSelectedIndex(i);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            showWarningMessage("Gagal memuat data jadwal: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                FlatLightLaf.setup();
                new FormInputJadwal(null).setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}