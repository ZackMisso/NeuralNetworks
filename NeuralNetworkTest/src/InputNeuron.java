//import java.util.ArrayList;
public abstract class InputNeuron extends Neuron{
    //private ArrayList<Connection> outputs;
    private double weight;
    private int inputID;
    
    public InputNeuron(){
        //outputs=new ArrayList<Connection>();
        weight=1.0;
        inputID=-1;
    }
    
    //public abstract double evaluate();
    //public abstract InputNeuron makeCopy();
    
    // getter methods
    //public ArrayList<Connection> getOutputs(){return outputs;}
    public double getWeight(){return weight;}
    public int getInputID(){return inputID;}
    
    // setter methods
    //public void setOutputs(ArrayList<Connection> param){outputs=param;}
    public void setWeight(double param){weight=param;}
    public void setInputID(int param){inputID=param;}
}