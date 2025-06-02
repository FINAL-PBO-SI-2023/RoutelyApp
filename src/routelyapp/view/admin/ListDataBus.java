package routelyapp.view.admin;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import routelyapp.model.Bus;
import routelyapp.service.BusService;
import routelyapp.service.BusServiceImpl;
import routelyapp.view.dashboard.AdminDashboard;

public class ListDataBus extends JFrame {
    private final BusService busService = new BusServiceImpl();
    private int selectedId = -1;

    private JTable tableBus;
    private JScrollPane scrollTable;
    private JButton btnTambah, btnEdit, btnHapus, btnKembali;
    private JLabel titleLabel;

    public ListDataBus() {
        FlatLightLaf.setup();
        initUI();
        setupEvents();
        loadData();

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Daftar Data Bus");
    }

    private void initUI() {
        titleLabel = new JLabel("Daftar Data Bus", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 24));

        tableBus = new JTable();
        scrollTable = new JScrollPane(tableBus);

        btnTambah = new JButton("➕ Tambah Baru");
        btnEdit = new JButton("✏️ Edit");
        btnHapus = new JButton("🗑️ Hapus");
        btnKembali = new JButton("🔙 Kembali");

        // Table styling
        tableBus.setRowHeight(35);
        tableBus.setShowGrid(true);
        tableBus.setGridColor(Color.LIGHT_GRAY);
        tableBus.setIntercellSpacing(new Dimension(1, 1));
        JTableHeader header = tableBus.getTableHeader();
        header.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));

        // Button styling
        styleButton(btnTambah, new Color(59, 180, 74));
        styleButton(btnEdit, new Color(0, 122, 204));
        styleButton(btnHapus, new Color(204, 0, 0));
        styleButton(btnKembali, new Color(100, 100, 100));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.add(btnTambah);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnKembali);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
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

    private void setupEvents() {
        tableBus.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableBus.getSelectedRow() != -1) {
                selectedId = (int) tableBus.getModel().getValueAt(tableBus.getSelectedRow(), 0);
            }
        });

        btnTambah.addActionListener(e -> {
            new FormInputBus(null).setVisible(true);
            dispose();
        });

        btnEdit.addActionListener(e -> {
            if (selectedId == -1) {
                JOptionPane.showMessageDialog(this, "Pilih data yang ingin diedit.");
                return;
            }
            new FormInputBus(selectedId).setVisible(true);
            dispose();
        });

        btnHapus.addActionListener(e -> {
            if (selectedId == -1) {
                JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                this, "Yakin ingin menghapus data bus ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION && busService.hapus(selectedId)) {
                loadData();
                selectedId = -1;
            }
        });

        btnKembali.addActionListener(e -> {
            new AdminDashboard().setVisible(true);
            dispose();
        });
    }

//    @SuppressWarnings("unchecked")
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
            .addGap(0, 529, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

     private void loadData() {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("ID");
        model.addColumn("Nama Bus");
        model.addColumn("Nomor Polisi");
        model.addColumn("Kapasitas");

        List<Bus> data = busService.getAll();
        for (Bus bus : data) {
            model.addRow(new Object[]{
                bus.getId(),
                bus.getNamaBus(),
                bus.getNomorPolisi(),
                bus.getKapasitas()
            });
        }

        tableBus.setModel(model);
        tableBus.removeColumn(tableBus.getColumnModel().getColumn(0));
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        EventQueue.invokeLater(() -> {
            new ListDataBus().setVisible(true);
        });
    }
}

