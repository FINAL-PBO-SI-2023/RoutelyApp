package routelyapp.view.admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import routelyapp.model.Bus;
import routelyapp.service.BusService;
import routelyapp.service.BusServiceImpl;

public class FormInputBus extends JFrame {
    private final Integer idBus;
    private final BusService busService = new BusServiceImpl();

    private final Color BACKGROUND = new Color(245, 249, 252);
    private final Color BORDER_COLOR = new Color(173, 216, 230);
    private final Color PRIMARY = new Color(52, 152, 219);
    private final Color SUCCESS = new Color(39, 174, 96);
    private final Color WARNING = new Color(241, 196, 15);
    private final Color GRAY = new Color(108, 117, 125);

    private final JTextField txtNamaBus = new JTextField();
    private final JTextField txtNoPolisi = new JTextField();
    private final JTextField txtKapasitas = new JTextField();

    private final JButton btnSimpan = new JButton("💾 Simpan");
    private final JButton btnBatal = new JButton("🧹 Reset");
    private final JButton btnKembali = new JButton("⬅️ Kembali");

    public FormInputBus(Integer idBus) {
        this.idBus = idBus;
        InitUI();
        if (idBus != null) loadBusData(idBus);
    }

    private void InitUI() {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (Exception ignored) {}

        setTitle("Routely Bus - Form Input Bus");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND);
        mainPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        // Header
        JLabel lblTitle = new JLabel("➕ Tambah Data Bus", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 28));
        lblTitle.setForeground(PRIMARY);
        lblTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Form panel (2 column grid)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setMaximumSize(new Dimension(700, Integer.MAX_VALUE));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.anchor = GridBagConstraints.WEST;

        addFormRow(formPanel, gbc, 0, "🚌 Nama Bus", txtNamaBus);
        addFormRow(formPanel, gbc, 1, "🚐 Nomor Polisi", txtNoPolisi);
        addFormRow(formPanel, gbc, 2, "👥 Kapasitas", txtKapasitas);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Footer buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(BACKGROUND);

        styleButton(btnSimpan, SUCCESS);
        styleButton(btnBatal, WARNING);
        styleButton(btnKembali, GRAY);

        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnBatal);
        buttonPanel.add(btnKembali);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        setContentPane(mainPanel);

        setupEventHandlers();
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JTextField field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        field.setPreferredSize(new Dimension(400, 40));
        field.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));

        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
    }
    
    private void setupEventHandlers() {
        btnSimpan.addActionListener(e -> {
            String nama = txtNamaBus.getText().trim();
            String nopol = txtNoPolisi.getText().trim();
            String kapasitasStr = txtKapasitas.getText().trim();

            if (nama.isEmpty() || nopol.isEmpty() || kapasitasStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Lengkapi semua field terlebih dahulu.");
                return;
            }

            try {
                int kapasitas = Integer.parseInt(kapasitasStr);
                Bus bus = new Bus(idBus, nama, nopol, kapasitas);
                if (busService.simpan(bus)) {
                    JOptionPane.showMessageDialog(this, "✅ Data berhasil disimpan.");
                    new ListDataBus().setVisible(true);
                    dispose();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "⚠️ Kapasitas harus berupa angka.");
            }
        });

        btnBatal.addActionListener(e -> {
            txtNamaBus.setText("");
            txtNoPolisi.setText("");
            txtKapasitas.setText("");
        });

        btnKembali.addActionListener(e -> {
            new ListDataBus().setVisible(true);
            dispose();
        });
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
            .addGap(0, 463, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

     private void loadBusData(int id) {
        Bus bus = busService.getById(id);
        if (bus != null) {
            txtNamaBus.setText(bus.getNamaBus());
            txtNoPolisi.setText(bus.getNomorPolisi());
            txtKapasitas.setText(String.valueOf(bus.getKapasitas()));
        } else {
            JOptionPane.showMessageDialog(this, "⚠️ Data bus tidak ditemukan.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormInputBus(null).setVisible(true));
    }
}