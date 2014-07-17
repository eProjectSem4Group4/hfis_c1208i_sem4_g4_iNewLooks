/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Phan Thanh
 */
public class CustomException extends Exception{
    public CustomException(String message){
        super(message);
    }
    public CustomException(String message,Exception childException){
        super(message);
        this.childException = childException;
    }
    private Exception childException;

    public Exception getChildException() {
        return childException;
    }
    
    public boolean isHaveChildException() {
        return childException != null;
    }
}
