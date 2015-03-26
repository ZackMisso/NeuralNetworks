package nodes.connections;
import nodes.neurons.Neuron;
import networks.NeuralNetwork;
import java.util.ArrayList;

public class RecurrentConnection extends Connection{
    private double newCache;
    
    public RecurrentConnection(){
        super();
        newCache=0.0;
        setRecurrent(true);
    }
    
    public void updateCache(){
        setCache(newCache);
    }
    
    public double calculateValue(){
        setEvaluated(true);
        return getCache();
    }
    
    public void resetCache(){
        newCache=0.0;
        updateCache();
    }
    
    public Connection makeCopy(ArrayList<Neuron> neurons,NeuralNetwork net){
        RecurrentConnection copy=new RecurrentConnection();
        copy.setInnovationNum(getInnovationNum());
        copy.setWeight(getWeight());
        copy.setActive(getActive());
        int giveNeuronNum=getGiveNeuron().getInnovationNum();
        int recieveNeuronNum=getRecieveNeuron().getInnovationNum();
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
    
    // getter methods
    public double getNewCache(){return newCache;}
    
    // setter methods
    public void setNewCache(double param){newCache=param;}
}