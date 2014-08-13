package nodes.neurons;
public abstract class OutputNeuron extends Neuron{
    private double output;
    private int outputID;
    
    public OutputNeuron(){
        output=0.0;
        outputID=-1;
    }
    
    public String toString(){
        String data="OutputNeuron\n";
        data+=super.toString();
        return data;
    }
    
    // getter methods
    public double getOutput(){return output;}
    public int getOutputID(){return outputID;}
    
    // setter methods
    public void setOutput(double param){output=param;}
    public void setOutputID(int param){outputID=param;}
}