/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.QueryParameter;
import entity.Task;
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
public class TaskModel extends DatabaseManagement implements Serializable {

    public List<Task> getAllTask(int requestId) throws SQLException, ClassNotFoundException, CustomException {
        try {
            List<Task> result = new ArrayList<Task>();
            makeConnection();
            ResultSet rs = getResultSet(
                    "SELECT * FROM Task WHERE requestId = ? ", new QueryParameter[]{new QueryParameter(1, requestId)});
            while (rs.next()) {
                Task taskList = new Task();
                taskList.setId(rs.getInt("id"));
                taskList.setToDo(rs.getString("toDo"));
                taskList.setDone(rs.getBoolean("done"));
            }
            closeConnection();
            return result;
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(TaskModel.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger(TaskModel.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException("Unknown exception", ex);
        }
    }

    public void deleteTask(Task task) throws SQLException, ClassNotFoundException, CustomException {
        try {
            makeConnection();
            doQuery("DELETE FROM Task WHERE id = ? ", new QueryParameter[]{new QueryParameter(1, task.getId())});
            closeConnection();
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(TaskModel.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger(TaskModel.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException("Unknown exception", ex);
        }
    }

    public void updateTask(Task task) throws SQLException, ClassNotFoundException, CustomException {
        try {
            makeConnection();
            doQuery("UPDATE Task SET toDo = ? AND done = ? WHERE id = ? ", new QueryParameter[]{
                new QueryParameter(1, task.getToDo()),
                new QueryParameter(2, task.isDone() ? "1" : "0"),
                new QueryParameter(3, task.getId())});
            closeConnection();
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(TaskModel.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger(TaskModel.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException("Unknown exception", ex);
        }
    }
    
    public void createTask(Task task) throws SQLException, ClassNotFoundException, CustomException {
        try {
            makeConnection();
            doQuery("INSERT INTO Task(requestId,toDo,done) VALUES (?,?,?)", new QueryParameter[]{
                new QueryParameter(1, task.getRquestId()),
                new QueryParameter(2, task.getToDo()),
                new QueryParameter(3, task.isDone() ? "1" : "0")});
            closeConnection();
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(TaskModel.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger(TaskModel.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException("Unknown exception", ex);
        }
    }
}
