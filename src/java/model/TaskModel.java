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
 * @author dongnp_c00702
 */
public class TaskModel extends DatabaseManagement implements Serializable {

    public List<Task> getAllTask(int requestId) throws SQLException, ClassNotFoundException, CustomException {
        try {
            List<Task> result = new ArrayList<Task>();
            makeConnection();
            ResultSet rs = getResultSet(
                    "SELECT * FROM Task WHERE requestId = ? ", new QueryParameter[]{new QueryParameter(1, requestId)});
            while (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("id"));
                task.setToDo(rs.getString("toDo"));
                task.setDone(rs.getBoolean("done"));
                result.add(task);
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
            doQuery("IF NOT EXISTS (SELECT * FROM Task WHERE requestId = ? AND done = 0) UPDATE Request SET finishDate = ? WHERE id = ?", new QueryParameter[]{
                            new QueryParameter(1, task.getRquestId()),
                            new QueryParameter(2, new java.sql.Date(java.util.Calendar.getInstance().getTime().getTime())),
                            new QueryParameter(3, task.getRquestId())});
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
            doQuery("UPDATE Task SET toDo = ?, done = ? WHERE id = ? ", new QueryParameter[]{
                new QueryParameter(1, task.getToDo()),
                new QueryParameter(2, task.isDone() ? "1" : "0"),
                new QueryParameter(3, task.getId())});
            doQuery("IF NOT EXISTS (SELECT * FROM Task WHERE requestId = ? AND done = 0) UPDATE Request SET finishDate = ? WHERE id = ?", new QueryParameter[]{
                            new QueryParameter(1, task.getRquestId()),
                            new QueryParameter(2, new java.sql.Date(java.util.Calendar.getInstance().getTime().getTime())),
                            new QueryParameter(3, task.getRquestId())});
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
            doQuery("IF NOT EXISTS (SELECT * FROM Task WHERE requestId = ? AND done = 0) UPDATE Request SET finishDate = ? WHERE id = ?", new QueryParameter[]{
                            new QueryParameter(1, task.getRquestId()),
                            new QueryParameter(2, new java.sql.Date(java.util.Calendar.getInstance().getTime().getTime())),
                            new QueryParameter(3, task.getRquestId())});
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
