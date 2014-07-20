/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import entity.Account;
import exception.CustomException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import model.AccountModel;

/**
 *
 * @author dongnp_c00702
 */
@ManagedBean(name="register")
@SessionScoped
public class Register implements Serializable{
    private String username;
    private String password;
    private String confirmPassword;
    private String name;
    private String email;
    private String address;
    private String company;
    private String message;
    private boolean showRegisterForm;
    public boolean isHaveMessage(){
        return this.message != null;
    }
    
    public void register(){
        if (!this.username.matches("^[a-zA-Z0-9]{1,16}"))
        {
            this.message = "Username must be alphanumeric from 1 to 16 characters!";
            return;
        }
        if (!this.password.matches("^[a-zA-Z0-9]{1,32}"))
        {
            this.message = "Password must be alphanumeric from 1 to 32 characters!";
            return;
        }
        if (!this.password.equals(this.confirmPassword)){
            this.message = "Confirm password not matched!";
            return;
        }
        if (!validateLength(this.name,32))
        {
            this.message = "Name's length must be between 1 to 32 characters!";
            return;
        }
        if (!this.email.matches("^[a-zA-Z0-9_@\\.]{1,64}") || !this.email.contains("@"))
        {
            this.message = "Invalid email!";
            return;
        }
        if (!validateLength(this.address,64))
        {
            this.message = "Address's length must be between 1 to 128 characters!";
            return;
        }
        if (!validateLength(this.company,64))
        {
            this.message = "Company's length must be between 1 to 64 characters!";
            return;
        }
        AccountModel am = new AccountModel();
        Account na = new Account();
        na.setUsername(username);
        na.setPassword(password);
        na.setName(name);
        na.setEmail(email);
        na.setAddress(address);
        na.setCompany(company);
        na.setAdmin(false);
        try {
            if (am.isAccountExists(username)){
                this.message = "Username has already been taken!";
                this.username = null;
                return;
            }
            am.createAccount(na);
            this.message = "Account created!";
            this.name = null;
            this.password = null;
            this.confirmPassword = null;
            this.address = null;
            this.company = null;
            this.email = null;
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
            this.message = "An error occured: " + ex;
        }
    }
    
    private boolean validateLength(String text, int length){
        return text != null && text.length() > 0 && text.length() <= length;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getMessage() {
        String copy = this.message;
        this.message = null;
        return copy;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isShowRegisterForm() {
        return showRegisterForm;
    }

    public void makeRegisterFormShow(boolean showRegisterForm) {
        this.showRegisterForm = showRegisterForm;
    }
}
