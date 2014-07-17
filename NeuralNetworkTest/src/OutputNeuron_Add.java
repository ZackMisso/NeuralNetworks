public class OutputNeuron_Add extends OutputNeuron{
    public OutputNeuron_Add(){
        super();
    }
    
    public double evaluate(){
        double sum=0.0;
        checkInputs();
        sum+=getWeights().get(0)*1.0;
        for(int i=0;i<getInputs().size();i++)
            sum+=getInputs().get(i).getWeight()*getWeights().get(i+1);
        setOutput(sum);
        return sum;
    }
    
    //public OutputNeuron makeCopy(){
    //    // implement
    //    return null;
    //}
}
