/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Request;
import exception.CustomException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import model.RequestModel;

/**
 *
 * @author thanb_000
 */
public class RequestController implements Serializable {
    private RequestModel requestModel;

    public RequestModel getRequestModel() {
        if (requestModel == null)
            requestModel = new RequestModel();
        return requestModel;
    }
    
    public void createRequest(Request request) throws SQLException, ClassNotFoundException, CustomException{
        getRequestModel().createRequest(request);
    }
    
    public List<Request> getAllRequests() throws SQLException, ClassNotFoundException, CustomException{
        return getRequestModel().getAllRequests();
    }
    
    public void setRequestStatus(int requestId, boolean status) throws SQLException, ClassNotFoundException, CustomException{
        getRequestModel().setRequestStatus(requestId, status);
    }
    
    public void setRequestDone(int requestId, boolean status) throws SQLException, ClassNotFoundException, CustomException{
        getRequestModel().setRequestDone(requestId, status);
    }
}
