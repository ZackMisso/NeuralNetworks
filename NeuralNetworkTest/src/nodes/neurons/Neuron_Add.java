package nodes.neurons;

import nodes.neurons.Neuron;

public class Neuron_Add extends Neuron{
    public Neuron_Add(){
        super();
    }
    
    // this constructor is used for a unit test
    public Neuron_Add(int num){
        System.out.println("THIS WAS USED :: NEURON_ADD");
        System.exit(0);
        setDepth(num);
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
        return activation;
    }
}