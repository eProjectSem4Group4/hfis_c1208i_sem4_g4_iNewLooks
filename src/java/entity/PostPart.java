/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author thanb_000
 */
public class PostPart {
    private boolean isImage;
    private String content;
    private boolean isNewLine;

    public boolean isIsNewLine() {
        return isNewLine;
    }

    public void setIsNewLine(boolean isNewLine) {
        this.isNewLine = isNewLine;
    }

    public PostPart(boolean isNewLine) {
        this.isNewLine = isNewLine;
    }
    
    public PostPart(boolean isImage, String content) {
        this.isImage = isImage;
        this.content = content;
        this.isNewLine = false;
    }

    public boolean isIsImage() {
        return isImage;
    }

    public void setIsImage(boolean isImage) {
        this.isImage = isImage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
