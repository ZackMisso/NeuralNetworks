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
    private int initIn;
    private int initOut;
    private int innovationNum;
    private boolean evaluated;
    
    public void reset(){
        cache=0.0;
        initIn=-1;
        initOut=-1;
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
    
    public boolean isSame(Node other){
        if(other instanceof Neuron&&this instanceof Neuron)
            return ((Neuron)other).isSameNeuron((Neuron)this);
        else if(other instanceof Connection&&this instanceof Connection)
            return ((Connection)other).isSameConnection((Connection)this);
        else
            return false;
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
    
    public static ArrayList<Connection> getConnections(ArrayList<Node> list){
        ArrayList<Connection> cons=new ArrayList<>();
        for(int i=0;i<list.size();i++)
            if(list.get(i)instanceof Connection)
                cons.add((Connection)list.get(i));
        return cons;
    }
    
    public static ArrayList<Neuron> getNeurons(ArrayList<Node> list){
        ArrayList<Neuron> ns=new ArrayList<>();
        for(int i=0;i<list.size();i++)
            if(list.get(i)instanceof Neuron)
                ns.add((Neuron)list.get(i));
        return ns;
    }
    
    // getter methods
    public double getCache(){return cache;}
    public int getInitIn(){return initIn;}
    public int getInitOut(){return initOut;}
    public int getInnovationNum(){return innovationNum;}
    public boolean getEvaluated(){return evaluated;}
    
    // setter methods
    public void setCache(double param){cache=param;}
    public void setInitIn(int param){initIn=param;}
    public void setInitOut(int param){initOut=param;}
    public void setInnovationNum(int param){innovationNum=param;}
    public void setEvaluated(boolean param){evaluated=param;}
}