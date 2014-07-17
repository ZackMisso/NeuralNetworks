public class Connection{
    private Neuron giveNeuron;
    private Neuron recieveNeuron;
    private double weight;
    private boolean active;
    private boolean evaluated;
    
    public Connection(){
        giveNeuron=null;
        recieveNeuron=null;
        weight=0.0;
        active=false;
        evaluated=false;
    }
    
    public void calculateWeight(){
        weight=recieveNeuron.evaluate();
    }
    
    public Connection makeCopy(){
        Connection copy=new Connection();
        // implement
        return null;
    }
    
    // getter methods
    public Neuron getGiveNeuron(){return giveNeuron;}
    public Neuron getRecieveNeuron(){return recieveNeuron;}
    public double getWeight(){return weight;}
    public boolean getActive(){return active;}
    public boolean getEvaluated(){return evaluated;}
    
    // setter methods
    public void setGiveNeuron(Neuron param){giveNeuron=param;}
    public void setRecieveNeuron(Neuron param){recieveNeuron=param;}
    public void setWeight(double param){weight=param;}
    public void setActive(boolean param){active=param;}
    public void setEvaluated(boolean param){evaluated=param;}
}
