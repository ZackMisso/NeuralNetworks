package nodes.connections;
import nodes.Node;
import nodes.neurons.Neuron;
public class Connection extends Node{
    private Neuron giveNeuron;
    private Neuron recieveNeuron;
    private double weight;
    private double cache;
    private boolean active;
    private boolean evaluated;
    
    public Connection(){
        giveNeuron=null;
        recieveNeuron=null;
        weight=0.0;
        cache=0.0;
        setInnovationNum(-1);
        active=false;
        evaluated=false;
    }
    
    public double calculateValue(){
        if(evaluated)
            return cache;
        cache=giveNeuron.evaluate()*weight;
        evaluated=true;
        return cache;
    }
    
    public void reset(){
        cache=0.0;
        evaluated=false;
    }
    
    public Connection makeCopy(){
        Connection copy=new Connection();
        // implement
        return null;
    }
    
    public String toString(){
        String data="Connection\n";
        data+="GiveNeuron :: "+giveNeuron.getInnovationNum()+"\n";
        data+="RecieveNeuron :: "+recieveNeuron.getInnovationNum()+"\n";
        return data;
    }
    
    // getter methods
    public Neuron getGiveNeuron(){return giveNeuron;}
    public Neuron getRecieveNeuron(){return recieveNeuron;}
    public double getWeight(){return weight;}
    public double getCache(){return cache;}
    public boolean getActive(){return active;}
    public boolean getEvaluated(){return evaluated;}
    
    // setter methods
    public void setGiveNeuron(Neuron param){giveNeuron=param;}
    public void setRecieveNeuron(Neuron param){recieveNeuron=param;}
    public void setWeight(double param){weight=param;}
    public void setCache(double param){cache=param;}
    public void setActive(boolean param){active=param;}
    public void setEvaluated(boolean param){evaluated=param;}
}