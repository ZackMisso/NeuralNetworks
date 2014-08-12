public class Connection{
    private Neuron giveNeuron;
    private Neuron recieveNeuron;
    private double weight;
    private int innovationNum;
    private boolean active;
    private boolean evaluated;
    
    public Connection(){
        giveNeuron=null;
        recieveNeuron=null;
        weight=0.0;
        innovationNum=-1;
        active=false;
        evaluated=false;
    }
    
    public void calculateWeight(){
        weight=giveNeuron.evaluate();
    }
    
    public Connection makeCopy(){
        Connection copy=new Connection();
        // implement
        return null;
    }
    
    public String toString(){
        String data="Connection\n";
        data+="GiveNeuron :: "+giveNeuron.getNeuronID()+"\n";
        data+="RecieveNeuron :: "+recieveNeuron.getNeuronID()+"\n";
        return data;
    }
    
    // getter methods
    public Neuron getGiveNeuron(){return giveNeuron;}
    public Neuron getRecieveNeuron(){return recieveNeuron;}
    public double getWeight(){return weight;}
    public int getInnovationNum(){return innovationNum;}
    public boolean getActive(){return active;}
    public boolean getEvaluated(){return evaluated;}
    
    // setter methods
    public void setGiveNeuron(Neuron param){giveNeuron=param;}
    public void setRecieveNeuron(Neuron param){recieveNeuron=param;}
    public void setWeight(double param){weight=param;}
    public void setInnovationNum(int param){innovationNum=param;}
    public void setActive(boolean param){active=param;}
    public void setEvaluated(boolean param){evaluated=param;}
}