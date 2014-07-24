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
public class Project {
    private int id;
    private String title;
    private String information;
    private String shortTitle;
    private String customer;
    private Date startDate;
    private Date finishDate;
    private String avatar;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
    
    public List<PostPart> getPostPartList(){
        List<PostPart> result = new ArrayList<PostPart>();
        if (this.information == null || this.information.matches("[\\[IMG\\]]{5}.*[\\[/IMG\\]]{6}")) {
            result.add(new PostPart(false, this.information));
            return result;
        }
        String content = "";
        String imageLink = "";
        int charPosition = 0;
        char[] infoCharArray = this.information.toCharArray();
        int infoCharArrayLength = infoCharArray.length;
        int skipUntilPosition = 0;
        for (char c : infoCharArray) {
            charPosition++;
            if (charPosition < skipUntilPosition) {
                continue;
            }
            if (c == '[') {
                if ((infoCharArray[charPosition - 1] == '['
                        && (infoCharArray[charPosition + 0] == 'I' || infoCharArray[charPosition + 0] == 'i')
                        && (infoCharArray[charPosition + 1] == 'M' || infoCharArray[charPosition + 1] == 'm')
                        && (infoCharArray[charPosition + 2] == 'G' || infoCharArray[charPosition + 2] == 'g')
                        && infoCharArray[charPosition + 3] == ']')) {
                    for (int j = charPosition + 5 - 1; j < infoCharArrayLength; j++) {
                        if (infoCharArray[j] != '[') {
                            imageLink += infoCharArray[j];
                        } else {
                            result.add(new PostPart(false, content));
                            content = "";
                            if (!imageLink.matches("^[h]{1}[tt]{2,2}[p]{1}[s]{0,1}[:]{1}[//]{2}.*")){
                                imageLink = "http://" + imageLink;
                            }
                            result.add(new PostPart(true, imageLink));
                            
                            imageLink = "";
                            skipUntilPosition = j + 7;
                            break;
                        }
                    }
                } else if (infoCharArray[charPosition - 1] == '['
                        && (infoCharArray[charPosition + 0] == 'B' || infoCharArray[charPosition + 0] == 'b')
                        && (infoCharArray[charPosition + 1] == 'R' || infoCharArray[charPosition + 1] == 'r')
                        && infoCharArray[charPosition + 2] == '/'
                        && infoCharArray[charPosition + 3] == ']') {
                    result.add(new PostPart(false, content));
                    content = "";
                    result.add(new PostPart(true));
                    skipUntilPosition = charPosition + 5;
                } else {
                    content += c;
                }
            } else {
                content += c;
            }
        }
        result.add(new PostPart(false, content));
        return result;
    }
    
    
}
