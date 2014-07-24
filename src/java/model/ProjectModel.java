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
}
