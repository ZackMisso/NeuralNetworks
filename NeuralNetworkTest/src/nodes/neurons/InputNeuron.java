package nodes.neurons;

//import java.util.ArrayList;
import java.util.Random;
public abstract class InputNeuron extends Neuron{
    private double input;
    private double weight;
    private int inputID;
    
    public InputNeuron(){
        input=0.0;
        weight=1.0;
        inputID=-1;
    }
    
    public String toString(){
        String data="InputNeuron\n";
        data+=super.toString();
        return data;
    }
    
    public void inputWeightsMutate(){
        Random random=new Random();
        double chance=random.nextDouble();
        double neg=random.nextDouble();
        double change=random.nextDouble();
        if(neg>.5)
            change*=-1;
        if(chance>.5)
            setBias(getBias()+change);
        else
            weight+=change;
    }
    
    //public abstract double evaluate();
    //public abstract InputNeuron makeCopy();
    
    // getter methods
    public double getInput(){return input;}
    public double getWeight(){return weight;}
    public int getInputID(){return inputID;}
    
    // setter methods
    public void setInput(double param){input=param;}
    public void setWeight(double param){weight=param;}
    public void setInputID(int param){inputID=param;}
}