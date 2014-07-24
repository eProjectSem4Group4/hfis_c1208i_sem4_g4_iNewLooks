/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.Account;
import entity.Elevator;
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
 * @author thanb_000
 */
public class ElevatorModel extends DatabaseManagement implements Serializable {
    public List<Elevator> getAllElevators() throws SQLException, ClassNotFoundException, CustomException {
        try {
            List<Elevator> result = new ArrayList<Elevator>();
            makeConnection();
            ResultSet rs = getResultSet("SELECT * FROM Elevator", new QueryParameter[0]);
            while (rs.next()) {
                Elevator ele = new Elevator();
                ele.setId(rs.getInt("id"));
                ele.setName(rs.getString("name"));
                ele.setDescription(rs.getString("description"));
                ele.setBasePrice(rs.getDouble("baseprice"));
                ele.setFloorPrice(rs.getDouble("floorprice"));
                ele.setMaxWeight(rs.getInt("maxWeight"));
                ele.setMaxHuman(rs.getInt("maxHuman"));
                ele.setCountry(rs.getString("country"));
                ele.setProducer(rs.getString("producer"));
                result.add(ele);
            }
            closeConnection();
            return result;
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(ElevatorModel.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger(ElevatorModel.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException("Unknown exception", ex);
        }
    }
    
    public boolean isElevatorExists(int id) throws ClassNotFoundException, CustomException, SQLException {
        try {
            makeConnection();
            ResultSet rs = getResultSet("SELECT * FROM Elevator WHERE id = ?",
                    new QueryParameter[]{
                new QueryParameter(1, id)
            });
            boolean result = rs.next();
            closeConnection();
            return result;
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(ElevatorModel.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger(ElevatorModel.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException("Unknown exception", ex);
        }
    }
    
    public void createElevator(Elevator ele) throws SQLException, ClassNotFoundException, CustomException{
        try {
            makeConnection();
            doQuery("INSERT INTO Elevator(name,baseprice,floorprice,[description],country,producer) VALUES (?,?,?,?,?,?)",
                    new QueryParameter[]{
                new QueryParameter(1, ele.getName()),
                new QueryParameter(2, ele.getBasePrice()),
                new QueryParameter(3, ele.getFloorPrice()),
                new QueryParameter(4, ele.getDescription()),
                new QueryParameter(5, ele.getCountry()),
                new QueryParameter(6, ele.getProducer())
            });
            closeConnection();
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(ElevatorModel.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger(ElevatorModel.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException("Unknown exception", ex);
        }
    }
}
