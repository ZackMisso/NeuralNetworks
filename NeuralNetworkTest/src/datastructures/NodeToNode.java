/**
 *
 * @author Zackary Misso
 * 
 */
package datastructures;
import nodes.Node;
import nodes.neurons.Neuron;
import nodes.neurons.InputNeuron;
import nodes.neurons.OutputNeuron;
import nodes.neurons.InputNeuron_Add;
import nodes.neurons.OutputNeuron_Add;
import nodes.neurons.Neuron_Add;
import nodes.connections.Connection;
import java.util.ArrayList;
import java.util.Random;
public class NodeToNode {
    private Node one;
    private Node two;
    
    public NodeToNode(Node a,Node b){
        one=a;
        two=b;
    }
    
    // returns a cross between the two nodes
    public Node mix(){
        if(one instanceof Connection && two instanceof Connection)
            return connectionMix();
        else if(one instanceof Neuron && two instanceof Neuron)
            return neuronMix();
        else{
            System.out.println("Conflicting Types :: NodeToNode");
            System.exit(0);
            return null;
        }
    }
    
    // returns a cross between the two connections
    public Node connectionMix(){
        Connection newConnection=new Connection();
        Connection oneC=(Connection)one;
        Connection twoC=(Connection)two;
        Random random=new Random();
        newConnection.setGiveNeuron(oneC.getGiveNeuron());
        newConnection.setRecieveNeuron(oneC.getRecieveNeuron());
        newConnection.setInnovationNum(oneC.getInnovationNum());
        double test=random.nextDouble();
        if(test>.5)
            newConnection.setActive(oneC.getActive());
        else
            newConnection.setActive(twoC.getActive());
        test=random.nextDouble();    
        newConnection.setWeight((oneC.getWeight()+twoC.getWeight())/2);
        return newConnection;
    }
    
    // returns a cross between the two neurons
    public Node neuronMix(){
        Neuron oneN=(Neuron)one;
        Neuron twoN=(Neuron)two;
        Neuron newNeuron;
        if(oneN instanceof InputNeuron){
            newNeuron=new InputNeuron_Add();
            // implement
        }
        else if(oneN instanceof OutputNeuron){
            newNeuron=new OutputNeuron_Add();
            // implement
        }else{
            newNeuron=new Neuron_Add();
            // implement
        }
        // set up bias
        Random random=new Random();
        double test=random.nextDouble();
        newNeuron.setBias((oneN.getBias()+twoN.getBias())/2);
        test=random.nextDouble();
        newNeuron.setInputs(oneN.getInputs());
        newNeuron.setOutputs(oneN.getOutputs());
        return newNeuron;
    }

    // this is used for the debugger
    public static ArrayList<Integer> getSharedInnoNums(ArrayList<NodeToNode> nodes){
        ArrayList<Integer> list=new ArrayList<>();
        for(int i=0;i<nodes.size();i++)
            if(nodes.get(i).getOne()!=null&&nodes.get(i).getTwo()!=null)
                list.add(nodes.get(i).getOne().getInnovationNum());
        return list;
    }
    
    // getter methods
    public Node getOne(){return one;}
    public Node getTwo(){return two;}
    
    // setter methods
    public void setOne(Node param){one=param;}
    public void setTwo(Node param){two=param;}
}