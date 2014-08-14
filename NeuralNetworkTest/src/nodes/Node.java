/**
 *
 * @author Zackary Misso
 * 
 */
package nodes;
public abstract class Node {
    private double cache;
    private int innovationNum;
    private boolean evaluated;
    
    public void reset(){
        cache=0.0;
        evaluated=false;
    }
    
    // getter methods
    public double getCache(){return cache;}
    public int getInnovationNum(){return innovationNum;}
    public boolean getEvaluated(){return evaluated;}
    
    // setter methods
    public void setCache(double param){cache=param;}
    public void setInnovationNum(int param){innovationNum=param;}
    public void setEvaluated(boolean param){evaluated=param;}
}
