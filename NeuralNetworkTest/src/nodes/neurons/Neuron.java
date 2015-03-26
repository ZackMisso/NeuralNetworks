package nodes.neurons;
import nodes.Node;
import nodes.connections.Connection;
import nodes.connections.RecurrentConnection;
import datastructures.RandomNumberGenerator;
import java.util.ArrayList;
import java.util.Random;
public abstract class Neuron extends Node{
    private ArrayList<Connection> inputs;
    private ArrayList<Connection> outputs;
    private double threshold;
    private double bias;
    private int connectionCreated; // ignore this
    private int depth;
    private boolean findingDepth;
    
    
    public Neuron(){
        inputs=new ArrayList<>();
        outputs=new ArrayList<>();
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
    
    public int findDepth(){
        setFindingDepth(true);
        int temp=depth;
        if(inputs.size()==0&&!(this instanceof InputNeuron))
            temp=1;
        for(int i=0;i<inputs.size();i++){
            if(!(inputs.get(i)instanceof RecurrentConnection)&&!inputs.get(i).getGiveNeuron().getFindingDepth()){
                if(inputs.get(i) instanceof RecurrentConnection)
                    System.out.println("UMMS");
                if(inputs.get(i).getGiveNeuron().findDepth()>=temp){
                    temp=inputs.get(i).getGiveNeuron().getDepth()+1;
                }
            }
        }
        setFindingDepth(false);
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
                if(inputs.get(i)instanceof RecurrentConnection)
                    ((RecurrentConnection)inputs.get(i)).calculateValue();
                else
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
        if(depth==0&&other.getDepth()==0) // the case they are both input neurons
            return true;
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
            else if(one.get(0).getDepth()>two.get(0).getDepth())
                list.add(two.remove(0));
            else{
                if(one.get(0).getInnovationNum()>two.get(0).getInnovationNum())
                    list.add(one.remove(0));
                else
                    list.add(two.remove(0));
            }
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
    public double getThreshold(){return threshold;}
    public double getBias(){return bias;}
    public int getDepth(){return depth;}
    public int getConnectionCreated(){return connectionCreated;}
    public boolean getFindingDepth(){return findingDepth;}
    
    // setter methods
    public void setInputs(ArrayList<Connection> param){inputs=param;}
    public void setOutputs(ArrayList<Connection> param){outputs=param;}
    public void setThreshold(double param){threshold=param;}
    public void setBias(double param){bias=param;}
    public void setDepth(int param){depth=param;}
    public void setConnectionCreated(int param){connectionCreated=param;}
    public void setFindingDepth(boolean param){findingDepth=param;}
}