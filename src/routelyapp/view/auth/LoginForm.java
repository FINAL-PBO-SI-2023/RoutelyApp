package routelyapp.view.auth;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import routelyapp.service.AuthService;
import routelyapp.service.AuthServiceImpl;

public class LoginForm extends JFrame {
    private JButton btnLogin, btnToRegister;
    private JLabel jLabel1, jLabel2, jLabel3, jLabel4;
    private JPasswordField txtPassword;
    private JTextField txtUsername;
    private final AuthService authService;

    public LoginForm() {
        authService = new AuthServiceImpl();
        setupUI();
    }

    private void setupUI() {
        setTitle("Routely - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen by default
        setMinimumSize(new Dimension(1024, 768)); // Larger minimum size

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Left panel for image - with no background painting
        JPanel leftPanel = new JPanel(new BorderLayout());

        // Directly load image from correct path
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/routelyapp/assets/bus.jpg"));
            
            // Create a method to fit the image to the panel size
            JLabel imageLabel = new JLabel() {
                @Override
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    // Get current panel size for fullscreen effect
                    int w = this.getWidth();
                    int h = this.getHeight();
                    
                    // Draw the image scaled to fill the panel completely
                    if (originalIcon.getImage() != null) {
                        g.drawImage(originalIcon.getImage(), 0, 0, w, h, this);
                    }
                }
            };
            
            // Add resize listener to ensure image redraws when panel size changes
            imageLabel.addComponentListener(new java.awt.event.ComponentAdapter() {
                public void componentResized(java.awt.event.ComponentEvent evt) {
                    imageLabel.repaint();
                }
            });
            
            leftPanel.add(imageLabel, BorderLayout.CENTER);
            System.out.println("Image loaded successfully from: /routelyapp/assets/bus.jpg");
            
        } catch (Exception e) {
            System.out.println("Failed to load image: " + e.getMessage());
            // No gradient background or fallback in this simplified version
            JLabel errorLabel = new JLabel("Image not available", JLabel.CENTER);
            errorLabel.setForeground(Color.GRAY);
            leftPanel.add(errorLabel, BorderLayout.CENTER);
        }

        // Right panel (60% width) for form
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(new EmptyBorder(80, 100, 80, 100)); // More padding

        // Welcome title
        jLabel3 = new JLabel("Welcome Back!");
        jLabel3.setFont(new Font("Poppins", Font.BOLD, 32));
        jLabel3.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(jLabel3);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 50))); // More space

        // Username field with better spacing
        JPanel usernamePanel = new JPanel(new BorderLayout());
        usernamePanel.setBackground(Color.WHITE);
        usernamePanel.setMaximumSize(new Dimension(400, 80));

        jLabel1 = new JLabel("Username", JLabel.LEFT);
        jLabel1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        usernamePanel.add(jLabel1, BorderLayout.NORTH);
        usernamePanel.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.CENTER);

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(70, 130, 180)),
            BorderFactory.createEmptyBorder(10, 5, 10, 5)
        ));
        usernamePanel.add(txtUsername, BorderLayout.SOUTH);
        rightPanel.add(usernamePanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Password field with better spacing
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setBackground(Color.WHITE);
        passwordPanel.setMaximumSize(new Dimension(400, 80));

        jLabel2 = new JLabel("Password", JLabel.LEFT);
        jLabel2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordPanel.add(jLabel2, BorderLayout.NORTH);
        passwordPanel.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.CENTER);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(70, 130, 180)),
            BorderFactory.createEmptyBorder(10, 5, 10, 5)
        ));
        passwordPanel.add(txtPassword, BorderLayout.SOUTH);
        rightPanel.add(passwordPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 50))); // More space before button

        // Login button with better styling
        btnLogin = new JButton("Login");
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setBackground(new Color(70, 130, 180));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBorderPainted(false);
        btnLogin.setFocusPainted(false);
        btnLogin.setPreferredSize(new Dimension(400, 50));
        btnLogin.setMaximumSize(new Dimension(400, 50));
        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword());
            authService.login(username, password, this);
        });

        // Hover effects
        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(new Color(50, 110, 160));
                btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(new Color(70, 130, 180));
            }
        });
        rightPanel.add(btnLogin);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Register link with better styling
        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        registerPanel.setBackground(Color.WHITE);
        registerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        jLabel4 = new JLabel("Don't have an account?");
        jLabel4.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        registerPanel.add(jLabel4);

        btnToRegister = new JButton("Register");
        btnToRegister.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnToRegister.setForeground(new Color(70, 130, 180));
        btnToRegister.setBackground(Color.WHITE);
        btnToRegister.setBorderPainted(false);
        btnToRegister.setFocusPainted(false);
        btnToRegister.setContentAreaFilled(false);
        btnToRegister.addActionListener(e -> {
            new RegisterForm().setVisible(true);
            dispose();
        });

        // Hover effects for register button
        btnToRegister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnToRegister.setForeground(new Color(50, 110, 160));
                btnToRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnToRegister.setForeground(new Color(70, 130, 180));
            }
        });
        registerPanel.add(btnToRegister);
        rightPanel.add(registerPanel);

        // Add panels to main frame - give left panel 40% width
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        
        // Set weights for the left and right panels
        // This ensures the left panel takes up 40% of the width
        mainPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                int totalWidth = mainPanel.getWidth();
                int leftWidth = (int)(totalWidth * 0.5); // 40% for left panel
                leftPanel.setPreferredSize(new Dimension(leftWidth, mainPanel.getHeight()));
                mainPanel.revalidate();
            }
        });

        add(mainPanel);
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
            .addGap(0, 446, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

//    public static void main(String args[]) {
//        try {
//            // Gunakan FlatLaf untuk tampilan modern
//            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
//        } catch (Exception ex) {
//            System.err.println("Gagal mengatur FlatLaf");
//        }
//
//        SwingUtilities.invokeLater(() -> {
//            LoginForm loginForm = new LoginForm();
//            loginForm.setVisible(true);
//        });
//    }
//    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
