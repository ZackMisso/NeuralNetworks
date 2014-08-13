package nodes.neurons;


import nodes.neurons.OutputNeuron;

public class OutputNeuron_Add extends OutputNeuron{
    public OutputNeuron_Add(){
        super();
    }
    
    public double evaluate(){
        double sum=0.0;
        //System.out.println("DEBUG 10 "+getOutputID());
        checkInputs();
        // TODO :: ALSO ADD THE EXTRA WEIGHTS PER INPUT
        //System.out.println("DEBUG 11 :: OutputNeuron_Add");
        sum+=getBias();
        for(int i=0;i<getInputs().size();i++)
            sum+=getInputs().get(i).getWeight()*getWeights().get(i);
        double activation=checkThreshold(sum);
        setOutput(activation);
        //System.out.println("DEBUG 12");
        return activation;
    }
}
