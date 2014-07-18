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
        sum+=getWeights().get(0)*1.0;
        for(int i=0;i<getInputs().size();i++)
            sum+=getInputs().get(i).getWeight()*getWeights().get(i+1);
        setOutput(sum);
        //System.out.println("DEBUG 12");
        return sum;
    }
}
