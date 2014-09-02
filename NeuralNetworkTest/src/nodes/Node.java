/**
 *
 * @author Zackary Misso
 * 
 */
package nodes;
import nodes.connections.Connection;
import nodes.neurons.Neuron;
import java.util.ArrayList;
public abstract class Node {
    private double cache;
    private int innovationNum;
    private boolean evaluated;
    
    public void reset(){
        cache=0.0;
        evaluated=false;
    }
    
    // this is using a merge Sort
    public static ArrayList<Node> sort(ArrayList<Node> list){
        if(list.size()==1)
            return list;
        ArrayList<Node> one=new ArrayList<>();
        ArrayList<Node> two=new ArrayList<>();
        int i=0;
        for(;i<list.size()/2;i++)
            one.add(list.get(i));
        for(;i<list.size();i++)
            two.add(list.get(i));
        one=sort(one);
        two=sort(two);
        return merge(one,two);
    }
    
    public static ArrayList<Node> merge(ArrayList<Node> one,ArrayList<Node> two){
        ArrayList<Node> merged=new ArrayList<>();
        while(!one.isEmpty()&&!two.isEmpty()){
            if(one.get(0).getInnovationNum()<two.get(0).getInnovationNum())
                merged.add(one.remove(0));
            else
                merged.add(two.remove(0));
        }
        while(!one.isEmpty())
            merged.add(one.remove(0));
        while(!two.isEmpty())
            merged.add(two.remove(0));
        return merged;
    }
    
    public static ArrayList<Node> makeList(ArrayList<Connection> cons,ArrayList<Neuron> ns){
        ArrayList<Node> nodes=new ArrayList<>();
        for(int i=0;i<cons.size();i++)
            nodes.add(cons.get(i));
        for(int i=0;i<ns.size();i++)
            nodes.add(ns.get(i));
        return nodes;
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