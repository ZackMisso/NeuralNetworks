package nodes.neurons;
import nodes.Node;
import nodes.connections.Connection;
import java.util.ArrayList;
import java.util.Random;
public abstract class Neuron extends Node{
    private ArrayList<Connection> inputs;
    private ArrayList<Connection> outputs;
    private ArrayList<Double> weights;
    private double threshold;
    private double bias;
    //private int neuronID;
    
    public Neuron(){
        inputs=new ArrayList<>();
        outputs=new ArrayList<>();
        weights=new ArrayList<>();
        //neuronID=-100;
        setInnovationNum(-100);
        threshold=1.0;
        Random random=new Random();
        //weights.add(random.nextDouble());
        bias=random.nextDouble();
    }
    
    public abstract double evaluate();
    
    public double checkThreshold(double value){
        if(value>=threshold)
            return 1.0;
        return 0.0;
    }
    
    public void checkInputs(){
        for(int i=0;i<inputs.size();i++)
            if(!inputs.get(i).getEvaluated()){
                inputs.get(i).calculateValue();
                inputs.get(i).setEvaluated(true);
            }
    }
    
    public void deEvaluate(){
        for(int i=0;i<inputs.size();i++)
            inputs.get(i).setEvaluated(false);
        for(int i=0;i<outputs.size();i++)
            outputs.get(i).setEvaluated(false);
    }
    
    public int getNumberOfConnections(){
        return inputs.size()+outputs.size();
    }
    
    public boolean existsConnection(Neuron other){
        for(int i=0;i<outputs.size();i++)
            if(outputs.get(i).getGiveNeuron()==other||outputs.get(i).getRecieveNeuron()==other)
                return true;
        for(int i=0;i<inputs.size();i++)
            if(inputs.get(i).getGiveNeuron()==other||inputs.get(i).getRecieveNeuron()==other)
                return true;
        return false;
    }
    
    public Connection getConnectionWith(Neuron other){
        for(int i=0;i<outputs.size();i++)
            if(outputs.get(i).getGiveNeuron()==other||outputs.get(i).getRecieveNeuron()==other)
                return outputs.get(i);
        for(int i=0;i<inputs.size();i++)
            if(inputs.get(i).getGiveNeuron()==other||inputs.get(i).getRecieveNeuron()==other)
                return inputs.get(i);
        return null;
    }
    
    public void removeConnectionWith(Neuron other){
        Connection connection=getConnectionWith(other);
        if(outputs.contains(connection))
            outputs.remove(connection);
        else
            inputs.remove(connection);
    }
    
    public Neuron makeCopy(){
        Neuron neuron;
        if(this instanceof InputNeuron){
            InputNeuron_Add newNeuron=new InputNeuron_Add();
            InputNeuron temp=(InputNeuron)this;
            newNeuron.setInputID(temp.getInputID());
            neuron=newNeuron;
        }else if(this instanceof OutputNeuron){
            OutputNeuron_Add newNeuron=new OutputNeuron_Add();
            OutputNeuron temp=(OutputNeuron)this;
            newNeuron.setOutputID(temp.getOutputID());
            neuron=newNeuron;
        }else{
            neuron=new Neuron_Add();
        }
        neuron.setInnovationNum(getInnovationNum());
        ArrayList<Connection> ins=new ArrayList<>();
        ArrayList<Connection> outs=new ArrayList<>();
        ArrayList<Double> doubs=new ArrayList<>();
        for(int i=0;i<weights.size();i++)
            doubs.add(weights.get(i).doubleValue());
        for(int i=0;i<inputs.size();i++){
            Connection connection=new Connection();
            connection.setGiveNeuron(inputs.get(i).getGiveNeuron());
            connection.setRecieveNeuron(this);
            connection.setActive(inputs.get(i).getActive());
            connection.setWeight(inputs.get(i).getWeight());
            connection.setEvaluated(inputs.get(i).getEvaluated());
            ins.add(connection);
        }
        for(int i=0;i<outputs.size();i++){
            Connection connection=new Connection();
            connection.setGiveNeuron(this);
            connection.setRecieveNeuron(outputs.get(i).getRecieveNeuron());
            connection.setActive(outputs.get(i).getActive());
            connection.setWeight(outputs.get(i).getWeight());
            connection.setEvaluated(outputs.get(i).getEvaluated());
            outs.add(connection);
        }
        neuron.setInputs(ins);
        neuron.setOutputs(outs);
        neuron.setWeights(doubs);
        return neuron;
    }
    
    public String toString(){
        String data="";
        if(this instanceof OutputNeuron)
            System.out.println("OutputNeuron\n");
        if(this instanceof InputNeuron)
            System.out.println("InputNeuron\n");
        data+="NeuronID :: "+getInnovationNum()+"\n";
        data+="Control Weight :: "+weights.get(0)+"\n";
        data+="Inputs\n\n";
        for(int i=0;i<inputs.size();i++){
            data+=inputs.get(i).toString();
            data+="Input Weight :: "+weights.get(i+1)+"\n";
        }
        data+="Outputs\n\n";
        for(int i=0;i<outputs.size();i++){
            data+=outputs.get(i).toString();
        }
        data+="\n";
        return data;
    }
    
    // getter methods
    public ArrayList<Connection> getInputs(){return inputs;}
    public ArrayList<Connection> getOutputs(){return outputs;}
    public ArrayList<Double> getWeights(){return weights;}
    public double getThreshold(){return threshold;}
    public double getBias(){return bias;}
    //public int getNeuronID(){return neuronID;}
    
    // setter methods
    public void setInputs(ArrayList<Connection> param){inputs=param;}
    public void setOutputs(ArrayList<Connection> param){outputs=param;}
    public void setWeights(ArrayList<Double> param){weights=param;}
    public void setThreshold(double param){threshold=param;}
    public void setBias(double param){bias=param;}
    //public void setNeuronID(int param){neuronID=param;}
}