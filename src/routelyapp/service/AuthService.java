package routelyapp.service;

import routelyapp.view.auth.LoginForm;
import routelyapp.view.auth.RegisterForm;

public interface AuthService {
    void register(String username, String password, RegisterForm form);
    void login(String username, String password, LoginForm form);
}