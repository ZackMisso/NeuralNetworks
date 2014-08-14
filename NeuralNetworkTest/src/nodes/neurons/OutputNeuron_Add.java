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
            sum+=getInputs().get(i).calculateValue();
        double activation=checkThreshold(sum);
        setOutput(activation);
        setEvaluated(true);
        setCache(activation);
        //System.out.println("HiddenNeuron :: "+getInnovationNum());
        //System.out.println("Activation :: "+activation);
        //System.out.println("Bias :: "+getBias());
        //System.out.println("DEBUG 12");
        //System.out.println(activation);
        return activation;
    }
}
