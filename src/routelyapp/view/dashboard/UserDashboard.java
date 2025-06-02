package routelyapp.view.dashboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import routelyapp.assets.BackgroundPanel;
import routelyapp.model.User;
import routelyapp.service.UserServiceImpl;
import routelyapp.view.auth.LoginForm;
import routelyapp.view.user.LihatJadwalForm;
import routelyapp.view.user.RiwayatReservasiForm;

public class UserDashboard extends JFrame {
    private int idUser;
    private UserServiceImpl userService;
    private User user;

    private JLabel lblWelcomeUser = new JLabel(); 
    private JButton btnLihatJadwal = new JButton("📅 Lihat Jadwal");
    private JButton btnRiwayatSaya = new JButton("📖 Riwayat Saya");
    private JButton btnLogout = new JButton("🚪 Logout");

    public UserDashboard(int idUser) {
        this.idUser = idUser;
        this.userService = new UserServiceImpl();
        
        initUI();
        loadUserData();
    }

    private void initUI() {
        try {
            com.formdev.flatlaf.FlatIntelliJLaf.install();
        } catch (Exception ex) {
            System.err.println("Failed to set FlatLaf");
        }

        setTitle("Routely - User Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1024, 768));
        setLocationRelativeTo(null);

        setupUIComponents();
        setupEventHandlers();
    }

    private void setupUIComponents() {
        BackgroundPanel mainPanel = new BackgroundPanel("/routelyapp/assets/bususer.jpg");
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        // Title Section
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = new JLabel("✨ Selamat Datang");
        lblTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 32));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSubtitle = new JLabel("Lihat jadwal dan riwayat reservasi kamu");
        lblSubtitle.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        lblSubtitle.setForeground(Color.WHITE);

        titlePanel.add(lblTitle);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(lblSubtitle);

        // User info
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);
        lblWelcomeUser.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        lblWelcomeUser.setForeground(Color.WHITE);
        userPanel.add(lblWelcomeUser);
        btnLogout.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        btnLogout.setBackground(new Color(220, 53, 69));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btnLogout.setFocusPainted(false);
        userPanel.add(btnLogout);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);

        // Center Menu
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        styleButton(btnLihatJadwal);
        styleButton(btnRiwayatSaya);

        centerPanel.add(btnLihatJadwal, gbc);
        centerPanel.add(btnRiwayatSaya, gbc);

        // Overlay semi-transparent
        JPanel overlayPanel = new JPanel(new BorderLayout());
        overlayPanel.setBackground(new Color(0, 0, 0, 130));
        overlayPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        overlayPanel.add(headerPanel, BorderLayout.NORTH);
        overlayPanel.add(centerPanel, BorderLayout.CENTER);

        mainPanel.add(overlayPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
        button.setBackground(new Color(100, 150, 200)); // More vibrant color
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        button.setFocusPainted(false);
        button.setMargin(new Insets(10, 20, 10, 20));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(80, 130, 180));
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(100, 150, 200));
            }
        });
    }

    private void setupEventHandlers() {
        btnLihatJadwal.addActionListener(e -> {
            new LihatJadwalForm(idUser).setVisible(true);
            dispose();
        });

        btnRiwayatSaya.addActionListener(e -> {
            new RiwayatReservasiForm(idUser).setVisible(true);
            dispose();
        });

        btnLogout.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });
    }

    private void loadUserData() {
        user = userService.getUserById(idUser);
        if (user != null) {
            lblWelcomeUser.setText("👋 Hai, " + user.getUsername() + "!");
        } else {
            JOptionPane.showMessageDialog(this, "Gagal memuat data user.");
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
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to set FlatLaf");
        }

        SwingUtilities.invokeLater(() -> {
            // For testing purposes, use a dummy ID
            new UserDashboard(1).setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}
