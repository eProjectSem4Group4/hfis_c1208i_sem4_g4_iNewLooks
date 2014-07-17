/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.Feedback;
import entity.QueryParameter;
import entity.Request;
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
public class FeedbackModel extends DatabaseManagement implements Serializable {

    public void createFeedback(Feedback feedback) throws SQLException, ClassNotFoundException, CustomException {
        try {
            makeConnection();
            doQuery("INSERT INTO Feedback(userId,senderName,senderEmail,[description],rate,problem,comment,[read])"
                    + "\nVALUES (?,?,?,?,?,?,?,?)",
                    new QueryParameter[]{
                new QueryParameter(1, feedback.getUserId()),
                new QueryParameter(2, feedback.getSenderName()),
                new QueryParameter(3, feedback.getSenderEmail()),
                new QueryParameter(4, feedback.getDescription()),
                new QueryParameter(5, feedback.getRate()),
                new QueryParameter(6, feedback.getProblem()),
                new QueryParameter(7, feedback.getComment()),
                new QueryParameter(8, feedback.getRead() ? "1" : "0")
            });
            closeConnection();
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(FeedbackModel.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger(FeedbackModel.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException("Unknown exception", ex);
        }
    }

    public void changeReadState(int iFeedbackId, boolean bRead) throws SQLException, ClassNotFoundException, CustomException {
        try {
            makeConnection();
            doQuery("UPDATE Feedback SET [read] = ? WHERE id = ?",
                    new QueryParameter[]{
                new QueryParameter(1, bRead ? "1" : "0"),
                new QueryParameter(2, iFeedbackId),});
            closeConnection();
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(FeedbackModel.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger(FeedbackModel.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException("Unknown exception", ex);
        }
    }

    public List<Feedback> getAll(boolean bNotReadOnly) throws SQLException, ClassNotFoundException, CustomException {
        try {
            makeConnection();
            ResultSet rs = getResultSet("SELECT * FROM Feedback", new QueryParameter[0]);
            List<Feedback> result = new ArrayList<Feedback>();
            while (rs.next()) {
                Feedback fb = new Feedback();
                fb.setId(rs.getInt("id"));
                fb.setUserId(rs.getInt("userId"));
                fb.setSenderName(rs.getString("senderName"));
                fb.setSenderEmail(rs.getString("senderEmail"));
                fb.setDescription(rs.getString("description"));
                fb.setRate(rs.getByte("rate"));
                fb.setProblem(rs.getString("problem"));
                fb.setComment(rs.getString("comment"));
                fb.setRead(rs.getBoolean("read"));
                if (!bNotReadOnly || (bNotReadOnly && fb.getRead() == false)) {
                    result.add(fb);
                }
            }
            closeConnection();
            return result;
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(FeedbackModel.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger(FeedbackModel.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException("Unknown exception", ex);
        }
    }
}
