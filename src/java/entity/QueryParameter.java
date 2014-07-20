/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author Lab
 */

public class QueryParameter{
    private int position;
    private Object value;

    public QueryParameter(int position, Object value) {
        this.position = position;
        this.value = String.valueOf(value);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
    
}
