import java.util.ArrayList;
public abstract class OutputNeuron extends Neuron{
    //private ArrayList<Connection> inputs;
    //private ArrayList<Double> weights;
    private double output;
    private int outputID;
    
    public OutputNeuron(){
        //inputs=new ArrayList<>();
        //weights=new ArrayList<>();
        output=0.0;
        outputID=-1;
    }
    
    public String toString(){
        String data="OutputNeuron\n";
        data+=super.toString();
        return data;
    }
    
    //public abstract double evaluate();
    //public abstract OutputNeuron makeCopy();
    
    // getter methods
    //public ArrayList<Connection> getInputs(){return inputs;}
    //public ArrayList<Double> getWeights(){return weights;}
    public double getOutput(){return output;}
    public int getOutputID(){return outputID;}
    
    // setter methods
    //public void setInputs(ArrayList<Connection> param){inputs=param;}
    //public void setWeights(ArrayList<Double> param){weights=param;}
    public void setOutput(double param){output=param;}
    public void setOutputID(int param){outputID=param;}
}