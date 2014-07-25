public class InputNeuron_Add extends InputNeuron{
    public InputNeuron_Add(){
        super();
    }
    
    public double evaluate(){
        for(int i=0;i<getOutputs().size();i++)
            getOutputs().get(i).setWeight(getWeight()*getInput());
        return getWeight()*getInput();
    }
    
    //public InputNeuron makeCopy(){
    //    // implement
    //    return null;
    //}
}
