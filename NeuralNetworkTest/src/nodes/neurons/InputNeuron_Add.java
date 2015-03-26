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
        double activation=checkThreshold(sum);
        setCache(activation);
        setEvaluated(true);
        return activation;
    }
}