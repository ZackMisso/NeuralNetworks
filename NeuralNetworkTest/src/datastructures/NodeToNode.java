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
        //System.out.println("Connection is mixing :: NodeToNode");
        Connection newConnection=new Connection();
        Connection oneC=(Connection)one;
        Connection twoC=(Connection)two;
        Random random=new Random();
        //newConnection.setInnovationNum(one.getInnovationNum());
        //if(oneC.getGiveNeuron().getInnovationNum()!=twoC.getGiveNeuron().getInnovationNum()){
        //    System.out.println("Connections dont have the same input :: NodeToNode");
        //}
        //if(twoC.getRecieveNeuron().getInnovationNum()!=oneC.getRecieveNeuron().getInnovationNum()){
        //    System.out.println("Connections dont have the same output :: NodeToNode");
        //}
        newConnection.setGiveNeuron(oneC.getGiveNeuron());
        newConnection.setRecieveNeuron(oneC.getRecieveNeuron());
        newConnection.setInnovationNum(oneC.getInnovationNum());
        // set active
        double test=random.nextDouble();
        if(test>.5)
            newConnection.setActive(oneC.getActive());
        else
            newConnection.setActive(twoC.getActive());
        // set weight
        test=random.nextDouble();
        
        
        //if(test>.5)
        //    newConnection.setWeight(oneC.getWeight());
        //else
        //    newConnection.setWeight(twoC.getWeight());
        
        newConnection.setWeight((oneC.getWeight()+twoC.getWeight())/2);
        
        // check for recurrency
        //if(newConnection.getRecieveNeuron()==newConnection.getGiveNeuron())
        //    newConnection.setRecurrent(true);
        return newConnection;
    }
    
    // returns a cross between the two neurons
    public Node neuronMix(){
        //System.out.println("Neuron is Mixing :: NodeToNode");
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
        
        
        //if(test>.5)
        //    newNeuron.setBias(oneN.getBias());
        //else
        //    newNeuron.setBias(twoN.getBias());
        
        newNeuron.setBias((oneN.getBias()+twoN.getBias())/2);
        
        // inputs
        test=random.nextDouble();
        //ArrayList<Connection> connections=new ArrayList<>();
        //if(test>.5){
        newNeuron.setInputs(oneN.getInputs());
            //for(int i=0;i<oneN.getInputs().size();i++)
            //    connections.add(oneN.getInputs().get(i).makeCopy());
        //}else{
        //    newNeuron.setOutputs(twoN.getInputs());
        //}
        // outputs
        //test=random.nextDouble();
        //if(test>.5){
        newNeuron.setOutputs(oneN.getOutputs());
        //}else{
        //    newNeuron.setOutputs(twoN.getOutputs());
        //}
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