//import java.util.ArrayList;
public abstract class InputNeuron extends Neuron{
    //private ArrayList<Connection> outputs;
    private double input;
    private double weight;
    private int inputID;
    
    public InputNeuron(){
        //outputs=new ArrayList<Connection>();
        input=0.0;
        weight=1.0;
        inputID=-1;
    }
    
    public String toString(){
        String data="InputNeuron\n";
        data+=super.toString();
        return data;
    }
    
    //public abstract double evaluate();
    //public abstract InputNeuron makeCopy();
    
    // getter methods
    //public ArrayList<Connection> getOutputs(){return outputs;}
    public double getInput(){return input;}
    public double getWeight(){return weight;}
    public int getInputID(){return inputID;}
    
    // setter methods
    //public void setOutputs(ArrayList<Connection> param){outputs=param;}
    public void setInput(double param){input=param;}
    public void setWeight(double param){weight=param;}
    public void setInputID(int param){inputID=param;}
}