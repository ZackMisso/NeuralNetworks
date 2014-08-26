package nodes.connections;
import datastructures.RandomNumberGenerator;
import nodes.Node;
import nodes.neurons.Neuron;
public class Connection extends Node{
    private Neuron giveNeuron;
    private Neuron recieveNeuron;
    private double weight;
    private boolean active;
    
    public Connection(){
        giveNeuron=null;
        recieveNeuron=null;
        weight=0.0;
        setInnovationNum(-1);
        active=false;
    }
    
    public double calculateValue(){
        if(getEvaluated())
            return getCache();
        setCache(giveNeuron.evaluate()*weight);
        //System.out.println("Connection :: "+getInnovationNum());
        //System.out.println("Weight :: "+weight);
        //System.out.println("Value To Push :: "+getCache());
        setEvaluated(true);
        return getCache();
    }

    public void mutateWeight(RandomNumberGenerator rng){
        weight=rng.changeDouble(weight,true);
    }
    
    public Connection makeCopy(){
        Connection copy=new Connection();
        // implement
        return null;
    }
    
    public boolean isSameConnection(Connection other){
        if(giveNeuron.getInnovationNum()==other.getGiveNeuron().getInnovationNum())
            if(recieveNeuron.getInnovationNum()==other.getRecieveNeuron().getInnovationNum())
                return true;
        return false;
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
    public boolean getActive(){return active;}
    
    // setter methods
    public void setGiveNeuron(Neuron param){giveNeuron=param;}
    public void setRecieveNeuron(Neuron param){recieveNeuron=param;}
    public void setWeight(double param){weight=param;}
    public void setActive(boolean param){active=param;}
}