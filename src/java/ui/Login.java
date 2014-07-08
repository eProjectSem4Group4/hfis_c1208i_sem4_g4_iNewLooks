/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import controller.AccountController;
import entity.Account;
import exception.CustomException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author thanb_000
 */
@ManagedBean(name="login")
@SessionScoped
public class Login implements Serializable {
   
    private String username;
    private String password;
    private Boolean role;
    private Account user;
    private String message;

    public Account getUser() {
        return user;
    }
    
    private AccountController accountController;

    public AccountController getAccountController() {
        if (accountController == null)
            accountController = new AccountController();
        return accountController;
    }
    
    /**
     * Creates a new instance of Login
     */
    public Login() {
    }

    public void login() {
        if (this.username != null && this.password != null) {
            try {
                if (getAccountController().isAccountValid(this.username, this.password)) {
                    this.user = getAccountController().getAccount(this.username);
                    this.role = true;
                } else {
                    this.message = "Invalid username or password";
                }
            } catch (    ClassNotFoundException | SQLException | CustomException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                this.message = "Error: " + ex.getMessage();
            }
        }
    }
    
    public void logout() {
        role = null;
    }
    
    public boolean isLoggedIn() {
        return role != null && role == true;
    }
    
    public boolean isAdmin() {
        return isLoggedIn() && user != null && user.isAdmin();
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
