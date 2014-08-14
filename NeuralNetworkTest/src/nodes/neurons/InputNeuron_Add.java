package nodes.neurons;


import nodes.neurons.InputNeuron;

public class InputNeuron_Add extends InputNeuron{
    public InputNeuron_Add(){
        super();
    }
    
    public double evaluate(){
        if(getEvaluated())
            return getCache();
        double sum=getWeight()*getInput()+getBias();
        //if(getInput()==1.0)
        //    System.out.println("SUM :: "+sum);
        double activation=checkThreshold(sum);
        //System.out.println(activation);
        setCache(activation);
        setEvaluated(true);
        //System.out.println("InputNeuron :: "+getInnovationNum());
        //System.out.println("Activation :: "+activation);
        //System.out.println("Bias :: "+getBias());
        //System.out.println("Input :: "+getInput());
        //System.out.println("Weight :: "+getWeight());
        //for(int i=0;i<getOutputs().size();i++)
        //    //getOutputs().get(i).setWeight(activation);
        return activation;
    }
}
