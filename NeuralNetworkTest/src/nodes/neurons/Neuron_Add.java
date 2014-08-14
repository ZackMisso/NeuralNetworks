package nodes.neurons;

import nodes.neurons.Neuron;

public class Neuron_Add extends Neuron{
    public Neuron_Add(){
        super();
    }
    
    public double evaluate(){
        if(getEvaluated())
            return getCache();
        double sum=0.0;
        checkInputs();
        sum+=getBias();
        for(int i=0;i<getInputs().size();i++)
            sum+=getInputs().get(i).calculateValue();
        double activation=checkThreshold(sum);
        setEvaluated(true);
        setCache(activation);
        //System.out.println("HiddenNeuron :: "+getInnovationNum());
        //System.out.println("Activation :: "+activation);
        //System.out.println("Bias :: "+getBias());
        //System.out.println(activation+" "+sum);
        //for(int i=0;i<getOutputs().size();i++)
        //    getOutputs().get(i).setWeight(activation);
        return activation;
    }
}