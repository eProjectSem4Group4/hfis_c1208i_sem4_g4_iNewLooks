/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Account;
import exception.CustomException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import model.AccountModel;

/**
 *
 * @author thanb_000
 */
public class AccountController implements Serializable{
    private AccountModel accountModel;

    public AccountModel getAccountModel() {
        if (accountModel == null)
            accountModel = new AccountModel();
        return accountModel;
    }
    
    public boolean isAccountValid(String username, String password) throws ClassNotFoundException, SQLException, CustomException{
        return getAccountModel().isAccountValid(username, password);
    }
    
    public Account getAccount(String username) throws SQLException, ClassNotFoundException, CustomException{
        return getAccountModel().getAccount(username);
    }
    
    public Account getAccount(int id) throws SQLException, ClassNotFoundException, CustomException{
        return getAccountModel().getAccount(id);
    }
    
    public List<Account> getAllAccounts() throws SQLException, ClassNotFoundException, CustomException{
        return getAccountModel().getAllAccounts();
    }
}
