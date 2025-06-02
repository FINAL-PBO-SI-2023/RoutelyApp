package routelyapp.view.user;

import com.formdev.flatlaf.FlatLightLaf;
import routelyapp.repository.ScheduleRepositoryImpl;
import routelyapp.service.ScheduleService;
import routelyapp.service.ScheduleServiceImpl;
import routelyapp.view.dashboard.UserDashboard;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import javax.swing.table.JTableHeader;

public class LihatJadwalForm extends JFrame {
    private final ScheduleService scheduleService = new ScheduleServiceImpl(new ScheduleRepositoryImpl());
    private final int idUser;
    private int selectedId = -1;

    private JComboBox<String> cbAsal, cbTujuan;
    private com.toedter.calendar.JDateChooser dateChooser;
    private JTable tableJadwal;
    private DefaultTableModel model;

    public LihatJadwalForm(int idUser) {
        this.idUser = idUser;
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        FlatLightLaf.setup();
        initUI();
        loadComboBoxData();
        loadTableData(null, null, null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void initUI() {
    setTitle("Jadwal Bus - Routely App");

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setBackground(Color.WHITE);

    JLabel lblTitle = new JLabel("Jadwal Bus");
    lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));
    lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
    lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
    mainPanel.add(lblTitle);

    // === Filter Panel ===
    JPanel filterPanel = new JPanel(new GridBagLayout());
    filterPanel.setBorder(BorderFactory.createTitledBorder("Filter Pencarian"));
    filterPanel.setBackground(Color.WHITE);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(8, 8, 8, 8);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    cbAsal = new JComboBox<>();
    cbTujuan = new JComboBox<>();
    dateChooser = new com.toedter.calendar.JDateChooser();
    dateChooser.setDateFormatString("yyyy-MM-dd");

    JButton btnCari = new JButton("Cari Jadwal");
    btnCari.setBackground(new Color(52, 152, 219));
    btnCari.setForeground(Color.WHITE);
    btnCari.setPreferredSize(new Dimension(120, 30));
    btnCari.setFont(new Font("Segoe UI", Font.PLAIN, 14));

    gbc.gridy = 0;
    gbc.gridx = 0; gbc.weightx = 0; filterPanel.add(new JLabel("Asal:"), gbc);
    gbc.gridx = 1; gbc.weightx = 1; filterPanel.add(cbAsal, gbc);
    gbc.gridx = 2; gbc.weightx = 0; filterPanel.add(new JLabel("Tujuan:"), gbc);
    gbc.gridx = 3; gbc.weightx = 1; filterPanel.add(cbTujuan, gbc);
    gbc.gridx = 4; gbc.weightx = 0; filterPanel.add(new JLabel("Tanggal:"), gbc);
    gbc.gridx = 5; gbc.weightx = 1; filterPanel.add(dateChooser, gbc);
    gbc.gridx = 6; gbc.weightx = 0; filterPanel.add(btnCari, gbc);

    JPanel filterContainer = new JPanel(new BorderLayout());
    filterContainer.setBackground(Color.WHITE);
    filterContainer.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
    filterContainer.add(filterPanel, BorderLayout.CENTER);
    mainPanel.add(filterContainer);

    // === Table Jadwal ===
    model = new DefaultTableModel(new Object[]{"ID", "Bus", "Asal", "Tujuan", "Tanggal", "Jam", "Terminal", "Harga"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    tableJadwal = new JTable(model);
    tableJadwal.setFillsViewportHeight(true);
    tableJadwal.setRowHeight(30);
    tableJadwal.setSelectionBackground(new Color(52, 152, 219, 50));
    tableJadwal.setSelectionForeground(Color.BLACK);

    // Header font & alignment
    JTableHeader header = tableJadwal.getTableHeader();
    header.setFont(new Font("Segoe UI", Font.BOLD, 13));
    header.setBackground(new Color(52, 152, 219));
    header.setForeground(Color.WHITE);
    header.setPreferredSize(new Dimension(header.getPreferredSize().width, 30)); 
    ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

    // Center all cell content (except hidden ID)
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
    for (int i = 1; i < model.getColumnCount(); i++) {
        tableJadwal.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }

    // Hide column ID
    tableJadwal.getColumnModel().getColumn(0).setMinWidth(0);
    tableJadwal.getColumnModel().getColumn(0).setMaxWidth(0);
    tableJadwal.getColumnModel().getColumn(0).setWidth(0);

    JScrollPane scrollPane = new JScrollPane(tableJadwal);
    scrollPane.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createEmptyBorder(10, 0, 10, 0),
        BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true)
    ));
    scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));

    JPanel tableWrapper = new JPanel(new BorderLayout());
    tableWrapper.setBackground(Color.WHITE);
    tableWrapper.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));
    tableWrapper.add(scrollPane, BorderLayout.CENTER);
    mainPanel.add(tableWrapper);

    // === Bottom Buttons ===
    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
    bottomPanel.setBackground(Color.WHITE);
    bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 30));

    JButton btnReset = new JButton("Reset Filter");
    JButton btnKembali = new JButton("Kembali");

    btnReset.setBackground(new Color(46, 204, 113));
    btnReset.setForeground(Color.WHITE);
    btnReset.setPreferredSize(new Dimension(130, 32));
    btnReset.setFont(new Font("Segoe UI", Font.PLAIN, 13));

    btnKembali.setBackground(new Color(149, 165, 166));
    btnKembali.setForeground(Color.WHITE);
    btnKembali.setPreferredSize(new Dimension(130, 32));
    btnKembali.setFont(new Font("Segoe UI", Font.PLAIN, 13));

    bottomPanel.add(btnReset);
    bottomPanel.add(btnKembali);
    mainPanel.add(bottomPanel);

    setContentPane(mainPanel);

    // === Events ===
    btnCari.addActionListener(this::onCariClicked);
    btnReset.addActionListener(e -> {
        cbAsal.setSelectedIndex(0);
        cbTujuan.setSelectedIndex(0);
        dateChooser.setDate(null);
        loadTableData(null, null, null);
    });
    btnKembali.addActionListener(e -> {
        new UserDashboard(idUser).setVisible(true);
        dispose();
    });
    tableJadwal.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            int row = tableJadwal.getSelectedRow();
            if (e.getClickCount() == 2 && row != -1) {
                selectedId = Integer.parseInt(model.getValueAt(row, 0).toString());
                new FormReservasi(selectedId, idUser).setVisible(true);
                dispose();
            }
        }
    });
}


    private void onCariClicked(ActionEvent e) {
        String asal = cbAsal.getSelectedItem() != null && !cbAsal.getSelectedItem().toString().startsWith("--") ? cbAsal.getSelectedItem().toString() : null;
        String tujuan = cbTujuan.getSelectedItem() != null && !cbTujuan.getSelectedItem().toString().startsWith("--") ? cbTujuan.getSelectedItem().toString() : null;
        String tanggal = (dateChooser.getDate() != null) ? new SimpleDateFormat("yyyy-MM-dd").format(dateChooser.getDate()) : null;

        loadTableData(asal, tujuan, tanggal);
    }

    private void loadComboBoxData() {
        cbAsal.removeAllItems();
        cbTujuan.removeAllItems();
        cbAsal.addItem("-- Pilih Asal --");
        cbTujuan.addItem("-- Pilih Tujuan --");

        List<String> asalList = scheduleService.getAllAsal();
        List<String> tujuanList = scheduleService.getAllTujuan();

        for (String asal : asalList) cbAsal.addItem(asal);
        for (String tujuan : tujuanList) cbTujuan.addItem(tujuan);
    }

    private void loadTableData(String asal, String tujuan, String tanggal) {
        model.setRowCount(0);
        try {
            List<Map<String, Object>> list = scheduleService.getFiltered(asal, tujuan, tanggal);
            for (Map<String, Object> s : list) {
                model.addRow(new Object[]{
                        s.get("id"), s.get("nama_bus"), s.get("asal"),
                        s.get("tujuan"), s.get("tanggal"), s.get("jam"),
                        s.get("terminal"), s.get("harga")
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal load jadwal: " + ex.getMessage());
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
            .addGap(0, 442, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 490, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
        FlatLightLaf.setup();
        SwingUtilities.invokeLater(() -> new LihatJadwalForm(1).setVisible(true));
    }
}
