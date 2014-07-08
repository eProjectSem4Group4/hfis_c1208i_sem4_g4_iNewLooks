/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Elevator;
import exception.CustomException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import model.ElevatorModel;

/**
 *
 * @author thanb_000
 */
public class ElevatorController implements Serializable{
    private ElevatorModel elevatorModel;

    public ElevatorModel getElevatorModel() {
        if (elevatorModel == null)
            elevatorModel = new ElevatorModel();
        return elevatorModel;
    }
    
    public List<Elevator> getAllElevators() throws SQLException, ClassNotFoundException, CustomException{
        return getElevatorModel().getAllElevators();
    }
    
    public boolean isElevator(int id) throws ClassNotFoundException, CustomException, SQLException{
        return getElevatorModel().isElevatorExists(id);
    }
    
    public void createElevator(Elevator elevator) throws SQLException, ClassNotFoundException, CustomException{
        getElevatorModel().createElevator(elevator);
    }
}
