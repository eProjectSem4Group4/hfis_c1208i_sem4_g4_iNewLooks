/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.QueryParameter;
import exception.CustomException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.CORBA.portable.ApplicationException;

/**
 *
 * @author Lab
 */
public class DatabaseManagement {

    protected Connection connect;
    PreparedStatement ps;
    String connectionString = "jdbc:sqlserver://HUNGPV\\SQLEXPRESS2013:1314;databaseName=iNewLookS";

    public DatabaseManagement() {
    }

    public void makeConnection() throws SQLException, ClassNotFoundException, CustomException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connect = DriverManager.getConnection(connectionString, "sa", "Blackadn123@");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseManagement.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManagement.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Can not connect to database");
        } catch (Exception ex) {
            Logger.getLogger(DatabaseManagement.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException("Unknown error while making new DB connection");
        }
    }

    public void closeConnection() throws CustomException {
        try {
            if (connect != null) {
                connect.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManagement.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex){
            Logger.getLogger(DatabaseManagement.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException("Unknown exception", ex);
        } finally {
            connect = null;
        }
    }

    public void setQuery(String query, QueryParameter[] params) throws SQLException, CustomException {
        try {
            ps = connect.prepareStatement(query);
            for (QueryParameter param : params) {
                ps.setString(param.getPosition(), param.getValue());
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManagement.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Can not ");
        } catch (Exception ex){
            Logger.getLogger(DatabaseManagement.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException("Unknown exception", ex);
        }
    }

    public void doQuery(String query, QueryParameter[] params) throws SQLException, CustomException {
        try {
            ps = connect.prepareStatement(query);
            for (QueryParameter param : params) {
                ps.setString(param.getPosition(), param.getValue());
            }
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManagement.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Can not process query!");
        } catch (Exception ex){
            Logger.getLogger(DatabaseManagement.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException("Unknown exception", ex);
        }
    }

    public ResultSet getResultSet(String query, QueryParameter[] params) throws SQLException, CustomException {
        try {
            ps = connect.prepareStatement(query);
            for (QueryParameter param : params) {
                ps.setString(param.getPosition(), param.getValue());
            }
            return ps.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManagement.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Can not process query!");
        } catch (Exception ex){
            Logger.getLogger(DatabaseManagement.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException("Unknown exception", ex);
        }
    }
}
