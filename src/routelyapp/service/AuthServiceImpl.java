package routelyapp.service;

import javax.swing.JOptionPane;
import routelyapp.model.User;
import routelyapp.view.auth.LoginForm;
import routelyapp.view.auth.RegisterForm;
import routelyapp.view.dashboard.AdminDashboard;
import routelyapp.view.dashboard.UserDashboard;

public class AuthServiceImpl implements AuthService {
    private final UserService userService;

    public AuthServiceImpl() {
        this.userService = new UserServiceImpl();
    }

    @Override
    public void register(String username, String password, RegisterForm form) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(form, "Semua field wajib diisi!");
            return;
        }

        boolean success = userService.registerUser(username, password);
        
        if (success) {
            JOptionPane.showMessageDialog(form, "Registrasi berhasil! Silakan login.");
            new LoginForm().setVisible(true);
            form.dispose();
        } else {
            JOptionPane.showMessageDialog(form, "Username sudah terdaftar atau terjadi kesalahan.");
        }
    }

    @Override
    public void login(String username, String password, LoginForm form) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(form, "Semua field wajib diisi!");
            return;
        }

        User user = userService.authenticateUser(username, password);

        if (user != null) {
            JOptionPane.showMessageDialog(form, "Login berhasil sebagai " + user.getRole());

            // Tampilkan dashboard dulu
            if ("admin".equals(user.getRole())) {
                new AdminDashboard().setVisible(true);
            } else {
                new UserDashboard(user.getId()).setVisible(true);
            }

            // Baru tutup form login
            form.dispose();

        } else {
            JOptionPane.showMessageDialog(form, "Username atau password salah.");
        }
    }

}