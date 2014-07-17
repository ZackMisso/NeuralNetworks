import java.util.ArrayList;
public abstract class Neuron {
    private ArrayList<Connection> inputs;
    private ArrayList<Connection> outputs;
    private ArrayList<Double> weights;
    private int neuronID;
    
    public Neuron(){
        inputs=new ArrayList<>();
        outputs=new ArrayList<>();
        weights=new ArrayList<>();
        neuronID=-100;
        weights.add(0.0); // control
    }
    
    public abstract double evaluate();
    
    public void checkInputs(){
        for(int i=0;i<inputs.size();i++)
            if(!inputs.get(i).getEvaluated()){
                inputs.get(i).calculateWeight();
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
        Neuron_Add neuron=new Neuron_Add();
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
    
    // getter methods
    public ArrayList<Connection> getInputs(){return inputs;}
    public ArrayList<Connection> getOutputs(){return outputs;}
    public ArrayList<Double> getWeights(){return weights;}
    public int getNeuronID(){return neuronID;}
    
    // setter methods
    public void setInputs(ArrayList<Connection> param){inputs=param;}
    public void setOutputs(ArrayList<Connection> param){outputs=param;}
    public void setWeights(ArrayList<Double> param){weights=param;}
    public void setNeuronID(int param){neuronID=param;}
}