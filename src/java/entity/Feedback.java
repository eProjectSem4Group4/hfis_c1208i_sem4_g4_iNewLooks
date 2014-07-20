/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author dongnpc00702
 */
public class Feedback {
    private int id;
    private int userId;
    private String senderName;
    private String senderEmail;
    private String description;
    private Byte rate;
    private String problem;
    private String comment;
    private Boolean read;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        if (senderName == null || senderName.isEmpty()){
            return;
        }
        this.senderName = senderName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        if (senderEmail == null || senderEmail.isEmpty()){
            return;
        }
        this.senderEmail = senderEmail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte getRate() {
        if (this.rate == null)
            this.rate = 1;
        return this.rate;
    }

    public void setRate(byte rate) {
        this.rate = rate;
    }
    
    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getRead() {
        if (read == null)
            read = false;
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }
    
    
}
