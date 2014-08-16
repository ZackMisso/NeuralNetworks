/**
 *
 * @author Zackary Misso
 * 
 */
package datastructures;
public class IntToObject {
    private int value;
    private Object mapping;
    
    // TODO :: DECIDE IF THIS CLASS SHOULD BE USED OR NOT
    
    public IntToObject(int val,Object obj){
        value=val;
        mapping=obj;
    }
    
    // getter methods
    public int getValue(){return value;}
    public Object getMapping(){return mapping;}
    
    // setter methods
    public void setValue(int param){value=param;}
    public void setMapping(Object param){mapping=param;}
}