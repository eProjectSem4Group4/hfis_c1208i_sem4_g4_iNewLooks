/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author dongnp_c00702
 */
public class Task {
    private int id;
    private int rquestId;
    private String toDo;
    private boolean done;

    public int getRquestId() {
        return rquestId;
    }

    public void setRquestId(int rquestId) {
        this.rquestId = rquestId;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToDo() {
        return toDo;
    }

    public void setToDo(String toDo) {
        this.toDo = toDo;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
