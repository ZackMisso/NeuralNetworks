public class Neuron_Add extends Neuron{
    public Neuron_Add(){
        super();
    }
    
    public double evaluate(){
        double sum=0.0;
        checkInputs();
        sum+=getWeights().get(0)*1.0;
        for(int i=0;i<getInputs().size();i++)
            sum+=getInputs().get(i).getWeight()*getWeights().get(i+1);
        for(int i=0;i<getOutputs().size();i++)
            getOutputs().get(i).setWeight(sum);
        return sum;
    }
    
    //public Neuron makeCopy(){
    //    // implement
    //    return null;
    //}
}