public class InputNeuron_Add extends InputNeuron{
    public InputNeuron_Add(){
        super();
    }
    
    public double evaluate(){
        double sum=getWeight()*getInput()+getBias();
        double activation=checkThreshold(sum);
        for(int i=0;i<getOutputs().size();i++)
            getOutputs().get(i).setWeight(activation);
        return activation;
    }
}
