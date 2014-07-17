/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.Account;
import entity.QueryParameter;
import exception.CustomException;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Phan Thanh
 */
public class AccountModel extends DatabaseManagement implements Serializable {

    public Account getAccount(String username) throws SQLException, ClassNotFoundException, CustomException {
        try {
            makeConnection();
            ResultSet rs = getResultSet("SELECT * FROM Account WHERE username = ?", new QueryParameter[]{new QueryParameter(1, username)});
            if (rs.next()) {
                Account acc = new Account();
                acc.setId(rs.getInt("id"));
                acc.setUsername(rs.getString("username"));
                acc.setName(rs.getString("name"));
                acc.setEmail(rs.getString("email"));
                acc.setAddress(rs.getString("address"));
                acc.setCompany(rs.getString("company"));
                acc.setAdmin(rs.getBoolean("admin"));
                closeConnection();
                return acc;
            } else {
                closeConnection();
                throw new CustomException("Account doesn't exists!");
            }
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(AccountModel.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger(AccountModel.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException("Unknown exception", ex);
        }
    }

    public Account getAccount(int username) throws SQLException, ClassNotFoundException, CustomException {
        try {
            makeConnection();
            ResultSet rs = getResultSet("SELECT * FROM Account WHERE id = ?", new QueryParameter[]{new QueryParameter(1, username)});
            if (rs.next()) {
                Account acc = new Account();
                acc.setId(rs.getInt("id"));
                acc.setUsername(rs.getString("username"));
                acc.setName(rs.getString("name"));
                acc.setEmail(rs.getString("email"));
                acc.setAddress(rs.getString("address"));
                acc.setCompany(rs.getString("company"));
                acc.setAdmin(rs.getBoolean("admin"));
                closeConnection();
                return acc;
            } else {
                closeConnection();
                throw new CustomException("Account doesn't exists!");
            }
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(AccountModel.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger(AccountModel.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException("Unknown exception", ex);
        }
    }

    public List<Account> getAllAccounts() throws SQLException, ClassNotFoundException, CustomException {
        try {
            List<Account> result = new ArrayList<Account>();
            makeConnection();
            ResultSet rs = getResultSet("SELECT * FROM Account", new QueryParameter[0]);
            while (rs.next()) {
                Account acc = new Account();
                acc.setId(rs.getInt("id"));
                acc.setUsername(rs.getString("username"));
                acc.setName(rs.getString("name"));
                acc.setEmail(rs.getString("email"));
                acc.setAddress(rs.getString("address"));
                acc.setCompany(rs.getString("company"));
                acc.setAdmin(rs.getBoolean("admin"));
                result.add(acc);
            }
            closeConnection();
            return result;
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(AccountModel.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger(AccountModel.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException("Unknown exception", ex);
        }
    }

    public boolean isAccountValid(String username, String password) throws ClassNotFoundException, SQLException, CustomException {
        try {
            makeConnection();
            ResultSet rs = getResultSet("SELECT * FROM Account WHERE username = ? AND pwd = ?",
                    new QueryParameter[]{
                new QueryParameter(1, username),
                new QueryParameter(2, password)
            });
            boolean result = rs.next();
            closeConnection();
            return result;
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(AccountModel.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger(AccountModel.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException("Unknown exception", ex);
        }
    }

    public boolean isAccountExists(String username) throws ClassNotFoundException, CustomException, SQLException {
        try {
            makeConnection();
            ResultSet rs = getResultSet("SELECT * FROM Account WHERE username = ?",
                    new QueryParameter[]{
                new QueryParameter(1, username)
            });
            boolean result = rs.next();
            closeConnection();
            return result;
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(AccountModel.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger(AccountModel.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException("Unknown exception", ex);
        }
    }

    public void createAccount(Account acc) throws SQLException, ClassNotFoundException, CustomException {
        try {
            makeConnection();
            doQuery("INSERT INTO Account(username,pwd,name,email,[address],company,[admin]) VALUES (?,?,?,?,?,?,?)",
                    new QueryParameter[]{
                new QueryParameter(1, acc.getUsername()),
                new QueryParameter(2, acc.getPassword()),
                new QueryParameter(3, acc.getName()),
                new QueryParameter(4, acc.getEmail()),
                new QueryParameter(5, acc.getAddress()),
                new QueryParameter(6, acc.getCompany()),
                new QueryParameter(7, acc.isAdmin() ? "1" : "0")
            });
            closeConnection();
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(AccountModel.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger(AccountModel.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException("Unknown exception", ex);
        }
    }
}
