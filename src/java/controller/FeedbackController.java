/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Feedback;
import exception.CustomException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import model.FeedbackModel;

/**
 *
 * @author dongnpc00702
 */
public class FeedbackController implements Serializable {

    private FeedbackModel fbModel;
    public FeedbackModel getFbModel() {
        if (this.fbModel == null)
            this.fbModel = new FeedbackModel();
        return this.fbModel;
    }
    
    public void createFeedback(Feedback feedback) throws SQLException, ClassNotFoundException, CustomException{
        getFbModel().createFeedback(feedback);
    }
    
    public void changeReadState(int iFeedbackId, boolean bRead) throws SQLException, ClassNotFoundException, CustomException{
        getFbModel().changeReadState(iFeedbackId, bRead);
    }
    
    public List<Feedback> getAll(boolean bNotReadOnly) throws SQLException, ClassNotFoundException, CustomException{
        return getFbModel().getAll(bNotReadOnly);
    }
}
