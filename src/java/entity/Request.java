/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author thanb_000
 */
public class Request {

    private int id;
    private int userId;
    private int floorCount;
    private int systemCount;
    private String address;
    private double totalPrice;
    private int elevatorId;
    private Boolean done;
    private Boolean processing;
    private Date postDate;
    private Date finishDate;
    private List<Task> taskList;

    public byte getCompletedPercent() {
        if (taskList == null || taskList.isEmpty()) {
            return 0;
        }
        byte c = 0;
        for (Task t : taskList) {
            c += (t.isDone() ? 1 : 0);
        }
        return (byte) Math.floor(c / taskList.size());
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public Request() {
        this.done = false;
        this.processing = false;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public boolean isProcessing() {
        return processing == null ? false : processing;
    }

    public void setProcessing(boolean processing) {
        this.processing = processing;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDone() {
        return done == null ? false : done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getElevatorId() {
        return elevatorId;
    }

    public void setElevatorId(int elevatorId) {
        this.elevatorId = elevatorId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFloorCount() {
        return floorCount;
    }

    public void setFloorCount(int floorCount) {
        if (floorCount <= 0) {
            floorCount = 1;
        }
        this.floorCount = floorCount;
    }

    public int getSystemCount() {
        return systemCount;
    }

    public void setSystemCount(int systemCount) {
        if (systemCount <= 0) {
            systemCount = 1;
        }
        this.systemCount = systemCount;
    }

    public String getAddress() {
        return address;
    }

    public String getShortAddress() {
        return this.address.length() > 40 ? this.address.substring(0, 39) : this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    private String senderUsername;
    private String elevatorName;

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getElevatorName() {
        return elevatorName;
    }

    public void setElevatorName(String elevatorName) {
        this.elevatorName = elevatorName;
    }
}
