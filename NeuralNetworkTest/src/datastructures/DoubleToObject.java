/**
 *
 * @author Zackary Misso
 * 
 */
// THIS CLASS IS DEPRECIATED
package datastructures;
import java.util.ArrayList;
public class DoubleToObject {
    private double value;
    private Object mapping;
    
    // TODO :: DECIDE IF THIS CLASS SHOULD BE USED OR NOT
    
    public DoubleToObject(double val,Object obj){
        value=val;
        mapping=obj;
    }
    
    public static ArrayList<DoubleToObject> sort(ArrayList<DoubleToObject> list){
        // implement
        return null;
    }
    
    // getter methods
    public double getValue(){return value;}
    public Object getMapping(){return mapping;}
    
    // setter method
    public void setValue(double param){value=param;}
    public void setMapping(Object param){mapping=param;}
}
