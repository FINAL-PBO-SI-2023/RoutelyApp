package routelyapp.view.admin;

import com.formdev.flatlaf.FlatLightLaf;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import routelyapp.model.Schedule;
import routelyapp.service.ScheduleService;
import routelyapp.service.ScheduleServiceImpl;
import routelyapp.repository.ScheduleRepositoryImpl;
import routelyapp.view.dashboard.AdminDashboard;

public class ListJadwalForm extends JFrame {
    
    private final ScheduleService scheduleService = new ScheduleServiceImpl(new ScheduleRepositoryImpl());
    private int selectedId = -1;
    
    // Komponen UI (nama variabel sama persis dengan aslinya)
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKembali;
    private javax.swing.JButton btnTambahBaru;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane scrollTable;
    private javax.swing.JTable tableJadwal;

    public ListJadwalForm() {
        // Setup FlatLaf
        FlatLightLaf.setup();
        
        initUI();
        setupEventHandlers();
        loadJadwal();
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Daftar Jadwal Bus");
    }

    private void initUI() {
        // Inisialisasi komponen
        jLabel1 = new JLabel("Daftar Jadwal Bus", SwingConstants.CENTER);
        
        tableJadwal = new JTable();
        scrollTable = new JScrollPane(tableJadwal);
        
        btnTambahBaru = new JButton("➕ Tambah Baru");
        btnEdit = new JButton("✏️ Edit");
        btnHapus = new JButton("🗑️ Hapus");
        btnKembali = new JButton("🔙 Kembali");
        
        // Styling dengan FlatLaf
        jLabel1.setFont(new Font("Segoe UI Emoji", Font.BOLD, 24));
        
        // Table styling
        tableJadwal.setRowHeight(35);
        tableJadwal.setShowGrid(true);
        JTableHeader header = tableJadwal.getTableHeader();
        header.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        
        // Button styling
        styleButton(btnTambahBaru, new Color(59, 180, 74)); // Hijau
        styleButton(btnEdit, new Color(0, 122, 204)); // Biru
        styleButton(btnHapus, new Color(204, 0, 0)); // Merah
        styleButton(btnKembali, new Color(100, 100, 100)); // Abu-abu
        
        // Layout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.add(btnTambahBaru);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnKembali);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(jLabel1, BorderLayout.NORTH);
        mainPanel.add(scrollTable, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
    }

    private void setupEventHandlers() {
        // Table selection listener
        tableJadwal.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableJadwal.getSelectedRow() != -1) {
                selectedId = (int) tableJadwal.getModel().getValueAt(tableJadwal.getSelectedRow(), 0);
            }
        });
        
        // Button handlers (langsung di sini tanpa method terpisah)
        btnTambahBaru.addActionListener(e -> {
            new FormInputJadwal(null).setVisible(true);
            dispose();
        });
        
        btnEdit.addActionListener(e -> {
            if (selectedId == -1) {
                JOptionPane.showMessageDialog(this, "Pilih data yang ingin diedit.");
                return;
            }
            new FormInputJadwal(selectedId).setVisible(true);
            dispose();
        });
        
        btnHapus.addActionListener(e -> {
            if (selectedId == -1) {
                JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu.");
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(
                this, 
                "Yakin ingin menghapus jadwal ini?", 
                "Konfirmasi", 
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION && scheduleService.delete(selectedId)) {
                loadJadwal();
                selectedId = -1;
            }
        });
        
        btnKembali.addActionListener(e -> {
            new AdminDashboard().setVisible(true);
            dispose();
        });
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 459, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 511, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
    private void loadJadwal() {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        model.addColumn("ID");
        model.addColumn("Bus");
        model.addColumn("Asal");
        model.addColumn("Tujuan");
        model.addColumn("Tanggal");
        model.addColumn("Jam");
        model.addColumn("Harga");
        model.addColumn("Terminal");

        List<Schedule> jadwals = scheduleService.getAll();
        for (Schedule s : jadwals) {
            model.addRow(new Object[]{
                s.getId(),
                s.getBusName(),
                s.getAsal(),
                s.getTujuan(),
                s.getTanggal(),
                s.getJam(),
                "Rp " + s.getHarga(),
                s.getTerminal()
            });
        }

        tableJadwal.setModel(model);
        tableJadwal.removeColumn(tableJadwal.getColumnModel().getColumn(0));
        
        // Distribusikan lebar kolom (dalam pixel)
        int tableWidth = scrollTable.getWidth() - 30; // Beri margin 30px
        tableJadwal.getColumnModel().getColumn(0).setPreferredWidth((int)(tableWidth * 0.15)); // Bus 15%
        tableJadwal.getColumnModel().getColumn(1).setPreferredWidth((int)(tableWidth * 0.25)); // Rute 25%
        tableJadwal.getColumnModel().getColumn(2).setPreferredWidth((int)(tableWidth * 0.15)); // Tanggal 15%
        tableJadwal.getColumnModel().getColumn(3).setPreferredWidth((int)(tableWidth * 0.10)); // Jam 10%
        tableJadwal.getColumnModel().getColumn(4).setPreferredWidth((int)(tableWidth * 0.15)); // Harga 15%
        tableJadwal.getColumnModel().getColumn(5).setPreferredWidth((int)(tableWidth * 0.20)); // Terminal 20%
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        EventQueue.invokeLater(() -> {
            new ListJadwalForm().setVisible(true);
        });
    }
}