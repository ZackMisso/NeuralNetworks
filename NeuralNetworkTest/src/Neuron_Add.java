public class Neuron_Add extends Neuron{
    public Neuron_Add(){
        super();
    }
    
    public double evaluate(){
        double sum=0.0;
        checkInputs();
        sum+=getBias();
        for(int i=0;i<getInputs().size();i++)
            sum+=getInputs().get(i).getWeight()*getWeights().get(i);
        double activation=checkThreshold(sum);
        for(int i=0;i<getOutputs().size();i++)
            getOutputs().get(i).setWeight(activation);
        return activation;
    }
}