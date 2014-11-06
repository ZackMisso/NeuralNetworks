package nodes.neurons;
import nodes.Node;
import nodes.connections.Connection;
import datastructures.RandomNumberGenerator;
import java.util.ArrayList;
import java.util.Random;
public abstract class Neuron extends Node{
    private ArrayList<Connection> inputs;
    private ArrayList<Connection> outputs;
    //private Neuron initInput; // what
    //private Neuron initOutput; // what
    //private int initIn; // should be used for initialization
    //private int initOut; // should be used for initialization
    private double threshold;
    private double bias;
    private int connectionCreated; // ignore this
    private int depth;
    
    public Neuron(){
        inputs=new ArrayList<>();
        outputs=new ArrayList<>();
        //initInput=null;
        //initOutput=null;
        setInnovationNum(-100);
        threshold=1.0;
        connectionCreated=-1;
        if(this instanceof InputNeuron)
            depth=0;
        else
            depth=1;
        Random random=new Random();
        bias=random.nextDouble();
    }
    
    public int findDepth(){ // NEED TO TEST
        //if(depth!=-1)
        //    return depth;
        //System.out.println("THIS HAS BEEN RAN");
        //System.exit(0);
        //if(depth==-1)
        //    depth=0;
        int temp=depth;
        if(inputs.size()==0&&!(this instanceof InputNeuron))
            temp=1;
        for(int i=0;i<inputs.size();i++)
            if(inputs.get(i).getGiveNeuron().findDepth()>=temp)
                temp=inputs.get(i).getGiveNeuron().findDepth()+1;
        //System.out.println("Temp :: "+temp);
        //System.exit(0);
        depth=temp;
        return depth;
    }
    
    public abstract double evaluate();
    
    public double checkThreshold(double value){
        if(value>=threshold)
            return 1.0;
        return 0.0;
    }
    
    public void checkInputs(){
        for(int i=0;i<inputs.size();i++)
            if(!inputs.get(i).getEvaluated()){
                inputs.get(i).calculateValue();
                inputs.get(i).setEvaluated(true);
            }
    }
    
    public void deEvaluate(){
        for(int i=0;i<inputs.size();i++)
            inputs.get(i).setEvaluated(false);
        for(int i=0;i<outputs.size();i++)
            outputs.get(i).setEvaluated(false);
    }
    
    public int getNumberOfConnections(){
        return inputs.size()+outputs.size();
    }
    
    // returns if there is already a connection between two neurons
    public boolean existsConnection(Neuron other){
        if(depth<0&&other.getDepth()<0) // The depths are not being set
            System.out.println("Depths not set :: Neuron");
        else if(depth<0)
            System.out.println("This depth not set :: Neuron");
        else if(other.getDepth()<0)
            System.out.println("Other depth not set :: Neuron");
        if(depth==0&&other.getDepth()==0){ // the case they are both input neurons
            //System.out.println("THIS WAS RAN :: NEURON");
            return true;
        }
        for(int i=0;i<outputs.size();i++)
            if(outputs.get(i).getGiveNeuron()==other||outputs.get(i).getRecieveNeuron()==other)
                return true;
        for(int i=0;i<inputs.size();i++)
            if(inputs.get(i).getGiveNeuron()==other||inputs.get(i).getRecieveNeuron()==other)
                return true;
        return false;
    }

    public void mutateBias(RandomNumberGenerator rng){
        bias=rng.changeDouble(bias,true);
    }
    
    public void crossover(Neuron other){
        // implement
    }
    
    public Connection getConnectionWith(Neuron other){
        for(int i=0;i<outputs.size();i++)
            if(outputs.get(i).getGiveNeuron()==other||outputs.get(i).getRecieveNeuron()==other)
                return outputs.get(i);
        for(int i=0;i<inputs.size();i++)
            if(inputs.get(i).getGiveNeuron()==other||inputs.get(i).getRecieveNeuron()==other)
                return inputs.get(i);
        return null;
    }
    
    public void removeConnectionWith(Neuron other){
        Connection connection=getConnectionWith(other);
        if(outputs.contains(connection))
            outputs.remove(connection);
        else
            inputs.remove(connection);
    }
    
    public Neuron makeCopy(){
        Neuron neuron;
        if(this instanceof InputNeuron){
            InputNeuron_Add newNeuron=new InputNeuron_Add();
            InputNeuron temp=(InputNeuron)this;
            newNeuron.setInputID(temp.getInputID());
            neuron=newNeuron;
        }else if(this instanceof OutputNeuron){
            OutputNeuron_Add newNeuron=new OutputNeuron_Add();
            OutputNeuron temp=(OutputNeuron)this;
            newNeuron.setOutputID(temp.getOutputID());
            neuron=newNeuron;
        }else{
            neuron=new Neuron_Add();
        }
        neuron.setBias(bias);
        neuron.setInnovationNum(getInnovationNum());
        return neuron;
    }
    
    // IGNORE
    public boolean isSameNeuron(Neuron other){
        //boolean outputs=false;
        //boolean inputs=false;
        /*if(initInput==null&&other.getInitInput()!=null)
            return false;
        if(initInput!=null&&other.getInitInput()==null)
            return false;
        if(initOutput==null&&other.getInitOutput()!=null)
            return false;
        if(initOutput!=null&&other.getInitOutput()==null)
            return false;
        if(initInput==null&&other.getInitInput()==null)
            inputs=true;
        if(initOutput==null&&other.getInitOutput()==null)
            outputs=true;
        if(!inputs&&initInput.getInnovationNum()==other.getInitInput().getInnovationNum())
            inputs=true;
        if(!outputs&&initOutput.getInnovationNum()==other.getInitOutput().getInnovationNum())
            outputs=true;*/
        return other.getConnectionCreated()==connectionCreated;
    }
    
    // sorts a list of neurons by their depths
    public static ArrayList<Neuron> sortByDepth(ArrayList<Neuron> list){
        if(list.size()==1)
            return list;
        ArrayList<Neuron> one=new ArrayList<>();
        ArrayList<Neuron> two=new ArrayList<>();
        for(int i=0;i<list.size()/2;i++)
            one.add(list.get(i));
        for(int i=list.size()/2;i<list.size();i++)
            two.add(list.get(i));
        return mergeByDepth(sortByDepth(one),sortByDepth(two));
    }
    
    // merges two lists of neurons based on their depths
    public static ArrayList<Neuron> mergeByDepth(ArrayList<Neuron> one,ArrayList<Neuron> two){
        ArrayList<Neuron> list=new ArrayList<>();
        while(!one.isEmpty()&&!two.isEmpty()){
            if(one.get(0).getDepth()<two.get(0).getDepth())
                list.add(one.remove(0));
            else
                list.add(two.remove(0));
        }
        while(!one.isEmpty())
            list.add(one.remove(0));
        while(!two.isEmpty())
            list.add(two.remove(0));
        return list;
    }

    // This method is used by CMDTester
    public static ArrayList<Integer> getInnovations(ArrayList<Neuron> list){
        ArrayList<Integer> nums=new ArrayList<>();
        for(int i=0;i<list.size();i++)
            nums.add(list.get(i).getInnovationNum());
        return nums;
    }
    
    // getter methods
    public ArrayList<Connection> getInputs(){return inputs;}
    public ArrayList<Connection> getOutputs(){return outputs;}
    //public Neuron getInitInput(){return initInput;}
    //public Neuron getInitOutput(){return initOutput;}
    public double getThreshold(){return threshold;}
    public double getBias(){return bias;}
    public int getDepth(){return depth;}
    public int getConnectionCreated(){return connectionCreated;}
    
    // setter methods
    public void setInputs(ArrayList<Connection> param){inputs=param;}
    public void setOutputs(ArrayList<Connection> param){outputs=param;}
    //public void setInitInput(Neuron param){initInput=param;}
    //public void getSetInitOutput(Neuron param){initOutput=param;}
    public void setThreshold(double param){threshold=param;}
    public void setBias(double param){bias=param;}
    public void setDepth(int param){depth=param;}
    public void setConnectionCreated(int param){connectionCreated=param;}
}