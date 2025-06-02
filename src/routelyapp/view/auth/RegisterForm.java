package routelyapp.view.auth;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import routelyapp.service.AuthService;
import routelyapp.service.AuthServiceImpl;

public class RegisterForm extends JFrame {
    private JButton btnRegister, btnToLogin;
    private JLabel jLabel1, jLabel2, jLabel3, jLabel4;
    private JPasswordField txtPassword;
    private JTextField txtUsername;
    private final AuthService authService;

    public RegisterForm() {
        authService = new AuthServiceImpl();
        setupUI();
    }

    private void setupUI() {
        setTitle("Routely - Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1024, 768));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JPanel leftPanel = new JPanel(new BorderLayout());
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/routelyapp/assets/bus.jpg"));
            JLabel imageLabel = new JLabel() {
                @Override
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    int w = this.getWidth();
                    int h = this.getHeight();
                    if (originalIcon.getImage() != null) {
                        g.drawImage(originalIcon.getImage(), 0, 0, w, h, this);
                    }
                }
            };
            imageLabel.addComponentListener(new java.awt.event.ComponentAdapter() {
                public void componentResized(java.awt.event.ComponentEvent evt) {
                    imageLabel.repaint();
                }
            });
            leftPanel.add(imageLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Image not available", JLabel.CENTER);
            errorLabel.setForeground(Color.GRAY);
            leftPanel.add(errorLabel, BorderLayout.CENTER);
        }

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(new EmptyBorder(80, 100, 80, 100));

        jLabel3 = new JLabel("Create Account");
        jLabel3.setFont(new Font("Poppins", Font.BOLD, 32));
        jLabel3.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(jLabel3);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 50)));

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
        rightPanel.add(Box.createRigidArea(new Dimension(0, 50)));

        btnRegister = new JButton("Register");
        btnRegister.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnRegister.setBackground(new Color(70, 130, 180));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setBorderPainted(false);
        btnRegister.setFocusPainted(false);
        btnRegister.setPreferredSize(new Dimension(400, 50));
        btnRegister.setMaximumSize(new Dimension(400, 50));
        btnRegister.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword());
            authService.register(username, password, this);
        });
        btnRegister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnRegister.setBackground(new Color(50, 110, 160));
                btnRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnRegister.setBackground(new Color(70, 130, 180));
            }
        });
        rightPanel.add(btnRegister);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        jLabel4 = new JLabel("Already have an account?");
        jLabel4.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loginPanel.add(jLabel4);
        btnToLogin = new JButton("Login");
        btnToLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnToLogin.setForeground(new Color(70, 130, 180));
        btnToLogin.setBackground(Color.WHITE);
        btnToLogin.setBorderPainted(false);
        btnToLogin.setFocusPainted(false);
        btnToLogin.setContentAreaFilled(false);
        btnToLogin.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });
        btnToLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnToLogin.setForeground(new Color(50, 110, 160));
                btnToLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnToLogin.setForeground(new Color(70, 130, 180));
            }
        });
        loginPanel.add(btnToLogin);
        rightPanel.add(loginPanel);

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        mainPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                int totalWidth = mainPanel.getWidth();
                int leftWidth = (int)(totalWidth * 0.5);
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
        setBackground(new java.awt.Color(153, 204, 255));
        setForeground(new java.awt.Color(153, 204, 255));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 399, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 412, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RegisterForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
