/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.PostPart;
import entity.Project;
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
 * @author thanb_000
 */
public class ProjectModel extends DatabaseManagement implements Serializable {

    public List<Project> getAllProjects() throws SQLException, ClassNotFoundException, CustomException {
        try {
            List<Project> result = new ArrayList<Project>();
            makeConnection();
            ResultSet rs = getResultSet(
                    "SELECT * FROM Project", new QueryParameter[0]);
            while (rs.next()) {
                Project prj = new Project();
                prj.setId(rs.getInt("id"));
                prj.setTitle(rs.getString("title"));
                prj.setInformation(rs.getString("information"));
                prj.setShortTitle(rs.getString("shortTitle"));
                prj.setCustomer(rs.getString("customer"));
                prj.setStartDate(rs.getDate("startDate"));
                prj.setFinishDate(rs.getDate("finishDate"));
                prj.setAvatar(rs.getString("avatar"));
                result.add(prj);
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
    
    public void createProject(Project project) throws SQLException, ClassNotFoundException, CustomException {
        try {
            makeConnection();
            doQuery("INSERT INTO Project(shortTitle, title, information, customer, startDate, finishDate, avatar)"
                    + "\nVALUES (?,?,?,?,?,?,?)",
                    new QueryParameter[]{
                new QueryParameter(1, project.getShortTitle()),
                new QueryParameter(2, project.getTitle()),
                new QueryParameter(3, project.getInformation()),
                new QueryParameter(4, project.getCustomer()),
                new QueryParameter(5, project.getStartDate()),
                new QueryParameter(6, project.getFinishDate()),
                new QueryParameter(7, project.getAvatar())
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
}
