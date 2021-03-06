package nodes.connections;
import datastructures.RandomNumberGenerator;
import networks.NeuralNetwork;
import nodes.Node;
import nodes.neurons.Neuron;
import java.util.ArrayList;
public class Connection extends Node{
    private Neuron giveNeuron;
    private Neuron recieveNeuron;
    private double weight;
    private double cache;
    private boolean active;
    private boolean recurrent; // implement recurrent functionality
    private boolean calculating;
    
    public Connection(){
        giveNeuron=null;
        recieveNeuron=null;
        cache=0.0;
        weight=0.0;
        setInnovationNum(-1);
        active=true;
        recurrent=false;
        calculating=false;
    }
    
    public double calculateValue(){
        if(recurrent){
            System.out.println("WHAT");
            System.exit(0);
        }
        if(!active)
            return 0.0;
        if(getEvaluated())
            return getCache();
        if(giveNeuron.getInnovationNum()==recieveNeuron.getInnovationNum()){
            System.out.println("YEA BAMARAM");
            System.exit(0);
            return getCache();
        }
        if(!calculating){
            calculating=true;
            setCache(giveNeuron.evaluate()*weight);
            calculating=false;
        }
        setEvaluated(true);
        return getCache();
    }

    public void mutateWeight(RandomNumberGenerator rng){
        weight=rng.changeDouble(weight,true);
    }
    
    public Connection makeCopy(ArrayList<Neuron> neurons,NeuralNetwork net){
        Connection copy=new Connection();
        copy.setInnovationNum(getInnovationNum());
        copy.setWeight(weight);
        copy.setActive(active);
        int giveNeuronNum=giveNeuron.getInnovationNum();
        int recieveNeuronNum=recieveNeuron.getInnovationNum();
        for(int i=0;i<neurons.size();i++){
            if(neurons.get(i).getInnovationNum()==giveNeuronNum)
                copy.setGiveNeuron(neurons.get(i));
            if(neurons.get(i).getInnovationNum()==recieveNeuronNum)
                copy.setRecieveNeuron(neurons.get(i));
        }
        copy.getGiveNeuron().getOutputs().add(copy);
        copy.getRecieveNeuron().getInputs().add(copy);
        return copy;
    }
    
    public boolean isSameConnection(Connection other){
        if(giveNeuron.getInnovationNum()==other.getGiveNeuron().getInnovationNum())
            if(recieveNeuron.getInnovationNum()==other.getRecieveNeuron().getInnovationNum())
                return true;
        return false;
    }

    // This method is used by CMDTester
    public static ArrayList<Integer> getInnovations(ArrayList<Connection> list){
        ArrayList<Integer> nums=new ArrayList<>();
        for(int i=0;i<list.size();i++)
            nums.add(list.get(i).getInnovationNum());
        return nums;
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
    public boolean getRecurrent(){return recurrent;}
    public boolean getCalculating(){return calculating;}
    
    // setter methods
    public void setGiveNeuron(Neuron param){giveNeuron=param;}
    public void setRecieveNeuron(Neuron param){recieveNeuron=param;}
    public void setWeight(double param){weight=param;}
    public void setActive(boolean param){active=param;}
    public void setRecurrent(boolean param){recurrent=param;}
    public void setCalculating(boolean param){calculating=param;}
}