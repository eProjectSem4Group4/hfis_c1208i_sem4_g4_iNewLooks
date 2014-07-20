/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.Account;
import entity.QueryParameter;
import entity.Request;
import exception.CustomException;
import java.io.Serializable;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dongnp_c00702
 */
public class RequestModel extends DatabaseManagement implements Serializable {

    public void createRequest(Request request) throws SQLException, ClassNotFoundException, CustomException {
        try {
            makeConnection();
            System.out.println("ELEVATORID= "+request.getElevatorId());
            doQuery("INSERT INTO Request(userId,elevatorId,floorCount,systemCount,[address],totalPrice,done,processing,postDate,finishDate) VALUES (?,?,?,?,?,?,?,?,?,?)",
                    new QueryParameter[]{
                new QueryParameter(1, request.getUserId()),
                new QueryParameter(2, request.getElevatorId()),
                new QueryParameter(3, request.getFloorCount()),
                new QueryParameter(4, request.getSystemCount()),
                new QueryParameter(5, request.getAddress()),
                new QueryParameter(6, request.getTotalPrice()),
                new QueryParameter(7, request.isDone() ? "1" : "0"),
                new QueryParameter(8, request.isProcessing() ? "1" : "0"),
                new QueryParameter(9, new java.sql.Date(java.util.Calendar.getInstance().getTime().getTime())),
                new QueryParameter(10, new java.sql.Date(java.util.Calendar.getInstance().getTime().getTime()))
            });
            closeConnection();
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(RequestModel.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger(RequestModel.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException("Unknown exception", ex);
        }
    }

    public List<Request> getAllRequests() throws SQLException, ClassNotFoundException, CustomException {
        try {
            List<Request> result = new ArrayList<Request>();
            makeConnection();
            ResultSet rs = getResultSet(
                    "SELECT r.id, r.userId, r.elevatorId, r.floorCount, r.systemCount, r.[address], r.totalPrice, r.done, r.processing, r.postDate, r.finishDate, a.username, e.name AS 'elevatorName'\n"
                    + "FROM Account a INNER JOIN Request r \n"
                    + "	ON a.id = r.userId JOIN Elevator e\n"
                    + "	ON r.elevatorId = e.id", new QueryParameter[0]);
            while (rs.next()) {
                Request req = new Request();
                req.setId(rs.getInt("id"));
                req.setUserId(rs.getInt("userId"));
                req.setElevatorId(rs.getInt("elevatorId"));
                req.setFloorCount(rs.getInt("floorCount"));
                req.setSystemCount(rs.getInt("systemCount"));
                req.setAddress(rs.getString("address"));
                req.setTotalPrice(rs.getDouble("totalPrice"));
                req.setDone(rs.getBoolean("done"));
                req.setProcessing(rs.getBoolean("processing"));
                req.setSenderUsername(rs.getString("username"));
                req.setElevatorName(rs.getString("elevatorName"));
                req.setPostDate(rs.getDate("postDate"));
                req.setFinishDate(rs.getDate("finishDate"));
                result.add(req);
            }
            closeConnection();
            return result;
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(RequestModel.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger(RequestModel.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException("Unknown exception", ex);
        }
    }

    public void setRequestStatus(int requestId, boolean status) throws SQLException, ClassNotFoundException, CustomException {
        try {
            makeConnection();
            doQuery("UPDATE Request SET processing = ? WHERE id = ?",
                    new QueryParameter[]{
                new QueryParameter(1, status),
                new QueryParameter(2, requestId)
            });
            closeConnection();
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(RequestModel.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger(RequestModel.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException("Unknown exception", ex);
        }
    }

    public void setRequestDone(int requestId, boolean status) throws SQLException, ClassNotFoundException, CustomException {
        try {
            makeConnection();
            doQuery("UPDATE Request SET done = ? WHERE id = ?",
                    new QueryParameter[]{
                new QueryParameter(1, status),
                new QueryParameter(2, requestId)
            });
            closeConnection();
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(RequestModel.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger(RequestModel.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException("Unknown exception", ex);
        }
    }
}
