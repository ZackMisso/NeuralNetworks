package networks;
import nodes.Node;
import nodes.neurons.InputNeuron_Add;
import nodes.neurons.OutputNeuron_Add;
import nodes.neurons.InputNeuron;
import nodes.neurons.OutputNeuron;
import nodes.neurons.Neuron;
import nodes.neurons.Neuron_Add;
import nodes.connections.Connection;
import experiments.Test;
import evolution.GlobalConstants;
import evolution.SpeciationFunctions;
import testtools.CMDTester;
import datastructures.RandomNumberGenerator;
import datastructures.NodeToNode;
import java.util.ArrayList;
import java.util.Random;
public class NeuralNetwork {
    private ArrayList<Node> nodes; // reference to all the nodes
    // TODO :: replace the bottom two with the one above
    private ArrayList<Neuron> neurons;
    private ArrayList<Connection> connections;
    private ArrayList<Integer> inputs;
    private ArrayList<Integer> outputs;
    private RandomNumberGenerator rng;
    private double fitness;
    private int nodeCnt; // this will be global soon
    
    public NeuralNetwork(){
        this(2,1);
    }
    
    public NeuralNetwork(int ins,int outs){
        neurons=new ArrayList<>();
        nodes=new ArrayList<>();
        connections=new ArrayList<>();
        inputs=new ArrayList<>();
        outputs=new ArrayList<>();
        rng=new RandomNumberGenerator();
        fitness=0.0;
        nodeCnt=0;
        initializeNetwork(ins,outs);
    }
    
    public NeuralNetwork(ArrayList<Neuron> param,ArrayList<Connection> param2){
        neurons=param;
        connections=param2;
        nodes=new ArrayList<>();
        for(int i=0;i<neurons.size();i++)
            nodes.add(neurons.get(i));
        for(int i=0;i<connections.size();i++)
            nodes.add(connections.get(i));
        inputs=new ArrayList<>();
        outputs=new ArrayList<>();
        rng=new RandomNumberGenerator();
        fitness=0.0;
        nodeCnt=0;
    }
    
    private void initializeNetwork(int insnum,int outsnum){
        for(int i=0;i<insnum;i++){
            InputNeuron_Add neuron=new InputNeuron_Add();
            neuron.setInputID(i);
            neuron.setInnovationNum(nodeCnt++);
            neurons.add(neuron);
            nodes.add(neuron);
        }
        for(int i=0;i<outsnum;i++){
            OutputNeuron_Add neuron=new OutputNeuron_Add();
            neuron.setOutputID(i);
            neuron.setInnovationNum(nodeCnt++);
            neurons.add(neuron);
            nodes.add(neuron);
        }
        ArrayList<InputNeuron> ins=findInputs();
        ArrayList<OutputNeuron> outs=findOutputs();
        for(int i=0;i<ins.size();i++)
            for(int f=0;f<outs.size();f++)
                makeConnection(ins.get(i),outs.get(f));
    }
    
// Temporarily Depreciated
//    public void testMutate(){
//        Node node=nodes.get(rng.getInt(nodes.size(),false));
//        double weight=rng.simpleDouble();
//        if(node instanceof Neuron){
//            // implement Neuron mutations
//            Neuron neuron=(Neuron)node;
//            if(weight<.9){
//                neuron.mutateBias(rng);
//            }else{
//                double chk=rng.simpleDouble();
//                
//                // implement
//            }
//        }else{
//            // implement Connection mutations
//            Connection connection=(Connection)node;
//            if(weight<.9){
//                connection.mutateWeight(rng);
//            }else{
//                double chk=rng.simpleDouble();
//                // implement
//            }
//        }
//    }
    
    // controls the mutation of the neural network
    public void mutate(){
        double chance=rng.simpleDouble();
        if(chance>.9)
            mutateTopography();
        else
            mutateWeights();
    }
    
    // mutates the weights of the neural network
    public void mutateWeights(){
        //int nodeNum=random.nextInt(neurons.size()+connections.size());
        int nodeNum=rng.getInt(neurons.size(),null,false);
        if(nodeNum>=connections.size()){
            // change a random bias
            nodeNum-=connections.size();
            neurons.get(nodeNum).mutateBias(rng);
        }else{
            // change a random connection
            connections.get(nodeNum).mutateWeight(rng);
        }
    }
    
    // mutates the topography of the neural network
    public void mutateTopography(){
        // TODO :: IMPROVE THIS METHOD
        int nodeNum=rng.getInt(neurons.size(),null,false);
        Neuron neuron=neurons.get(nodeNum);
        double makeRandomConnection=rng.simpleDouble();
        double newNeuron=rng.simpleDouble();
        if(makeRandomConnection>.55)
            newRandomConnection(neuron);
        else if(neuron instanceof InputNeuron){
            ArrayList<Connection> outputs=neuron.getOutputs();
            if(newNeuron>.8&&neurons.size()<GlobalConstants.MAX_NEURONS){
                addNeuron(outputs,neuron);
            }else if(connections.size()>GlobalConstants.MIN_CONNECTIONS){
                turnOffConnection(outputs,neuron);
            }else{
                mutateTopography(); // TODO ::  need to rewrite to avoid this case
                return;
            }
        }
        else if(neuron instanceof OutputNeuron){
            ArrayList<Connection> inputs=neuron.getInputs();
            if(newNeuron>.5&&neurons.size()<GlobalConstants.MAX_NEURONS){
                addNeuron(inputs,neuron);
            }else if(connections.size()>GlobalConstants.MIN_CONNECTIONS){
                turnOffConnection(inputs,neuron);
            }else{
                mutateTopography();
                return;
            }
        }else{ // hidden neurons
            ArrayList<Connection> inputs=neuron.getInputs();
            ArrayList<Connection> outputs=neuron.getOutputs();
            double inorout=rng.simpleDouble();
            if(inorout>.5&&neurons.size()<GlobalConstants.MAX_NEURONS){
                if(newNeuron>.5){
                    addNeuron(inputs,neuron);
                }else if(connections.size()>GlobalConstants.MIN_CONNECTIONS){
                    turnOffConnection(outputs,neuron);
                }else{
                    mutateTopography();
                    return;
                }
            }else{
                if(newNeuron>.5&&neurons.size()<GlobalConstants.MAX_NEURONS){
                    addNeuron(outputs,neuron);
                }else if(connections.size()>GlobalConstants.MIN_CONNECTIONS){
                    turnOffConnection(outputs,neuron);
                }else{
                    mutateTopography();
                    return;
                }
            }
        }
    }
    
    // THIS METHOD IS VERY INEFFICIENT FIX IT LATER TODO
    public void newRandomConnection(Neuron neuron){
        ArrayList<Neuron> connected=new ArrayList<>();
        for(int i=0;i<neuron.getInputs().size();i++)
            connected.add(neuron.getInputs().get(i).getGiveNeuron());
        for(int i=0;i<neuron.getOutputs().size();i++)
            connected.add(neuron.getOutputs().get(i).getRecieveNeuron());
        connected.add(neuron);
        ArrayList<Integer> indexes=new ArrayList<>();
        for(int i=0;i<neurons.size();i++)
            if(!connected.contains(neurons.get(i)))
                indexes.add(i);
        if(indexes.size()==0){
            return;
        }
        int chosenNeuron=rng.getInt(indexes.size(),false);
        double recogiv=rng.simpleDouble();
        int otherIndex=indexes.get(chosenNeuron);
        Neuron otherNeuron=neurons.get(otherIndex);
        if(otherNeuron==null){
            return;
        }
        if(otherNeuron instanceof InputNeuron&&neuron instanceof InputNeuron){
            mutate();
        }
        else if(otherNeuron instanceof OutputNeuron&&neuron instanceof OutputNeuron){
            mutate();
        }
        else if(otherNeuron instanceof InputNeuron&&neuron instanceof OutputNeuron){
            makeConnection(otherNeuron,neuron);
        }
        else if(otherNeuron instanceof OutputNeuron&&neuron instanceof InputNeuron){
            makeConnection(neuron,otherNeuron);
        }
        else if(otherNeuron instanceof InputNeuron){
            makeConnection(otherNeuron,neuron);
        }
        else if(otherNeuron instanceof OutputNeuron){
            makeConnection(neuron,otherNeuron);
        }
        else if(neuron instanceof InputNeuron){
            makeConnection(neuron,otherNeuron);
        }
        else if(neuron instanceof OutputNeuron){
            makeConnection(otherNeuron,neuron);
        }
        else{ // connection between two hidden neurons
            if(recogiv>.5){
                makeConnection(neuron,otherNeuron);
            }else{
                makeConnection(otherNeuron,neuron);
            }
        }
    }

    // creates a new connection between two neurons
    // TODO :: EDIT THIS
    public Connection makeConnection(Neuron give,Neuron recieve){
        Connection connection=new Connection();
        connection.setEvaluated(false);
        connection.setActive(true);
        connection.setGiveNeuron(give);
        connection.setRecieveNeuron(recieve);
        give.getOutputs().add(connection);
        recieve.getInputs().add(connection);
        connection.setInnovationNum(nodeCnt++);
        connection.setWeight(rng.simpleDouble());
        connections.add(connection);
        nodes.add(connection);
        return connection;
    }
    
    // adds a neuron in the middle of a connection
    public void addNeuron(ArrayList<Connection> connects,Neuron neuron){
        // THIS SHOULD NEVER RUN
        if(connects.isEmpty()){
            mutate();
            setFitness(-10.0);
            return;
        }
        int connectionNum=rng.getInt(connects.size(),null,false);
        Connection connection=connects.get(connectionNum);
        Neuron otherNeuron=null;
        if(connection.getGiveNeuron()==neuron)
            otherNeuron=connection.getRecieveNeuron();
        else
            otherNeuron=connection.getGiveNeuron();
        Neuron newNeuron=new Neuron_Add();
        newNeuron.setInnovationNum(nodeCnt++);
        if(connection.getGiveNeuron()==neuron){
            makeConnection(neuron,newNeuron);
            makeConnection(newNeuron,otherNeuron);
        }else{
            makeConnection(otherNeuron,newNeuron);
            makeConnection(newNeuron,neuron);
        }
        neurons.add(newNeuron);
        nodes.add(newNeuron);
    }
    
    // turns off a connection
    public void turnOffConnection(ArrayList<Connection> connects,Neuron neuron){
        if(connects.isEmpty()){
            mutate();
            return;
        }
        int connectionNum=rng.getInt(connects.size(),null,false);
        Connection connection=connects.get(connectionNum);
        Neuron other=null;
        if(connection.getGiveNeuron()==neuron)
            other=connection.getRecieveNeuron();
        else
            other=connection.getGiveNeuron();
        other.removeConnectionWith(neuron);
        connects.remove(connection);
        nodes.remove(connection);
    }
    
    // This method handles the cross over part of evolution
    public NeuralNetwork crossOver(NeuralNetwork other){
        NeuralNetwork newNetwork=new NeuralNetwork();
        ArrayList<NodeToNode> similar=SpeciationFunctions.getSimilarNodes(this,other);
        ArrayList<Node> newNodes=new ArrayList<>();
        boolean hasBetterFitness=fitness>other.getFitness();
        Random random=new Random();
        for(int i=0;i<similar.size();i++){
            if(similar.get(i).getTwo()!=null){
                if(random.nextDouble()>.78){
                    newNodes.add(similar.get(i).mix());
                }else{
                    if(hasBetterFitness)
                        newNodes.add(similar.get(i).getOne());
                    else
                        newNodes.add(similar.get(i).getTwo());
                }
            }else
                newNodes.add(similar.get(i).getOne());
        }
        newNetwork.setAllNodes(newNodes);
        return newNetwork;
    }
    
    public ArrayList<Double> run(Test param){
        try{
            ArrayList<OutputNeuron> outputs=findOutputs();
            ArrayList<InputNeuron> inputs=findInputs();
            ArrayList<Double> results=new ArrayList<>();
            for(int i=0;i<param.getInputs().size();i++){
                inputs.get(i).setInput(param.getInputs().get(i));
            }
            for(int i=0;i<outputs.size();i++){
                outputs.get(i).evaluate();
                results.add(outputs.get(i).getOutput());
            }
            nextGeneration();
            return results;
        }catch(StackOverflowError e){
            System.out.println("STACK OVERFLOW");
            new CMDTester(this);
            System.exit(0);
            return null;
        }
    }

    public NeuralNetwork copy(){
        NeuralNetwork network=new NeuralNetwork();
        network.getNeurons().clear();
        network.getConnections().clear();
        for(int i=0;i<neurons.size();i++)
            network.getNeurons().add(neurons.get(i).makeCopy());
        for(int i=0;i<connections.size();i++)
            network.getConnections().add(connections.get(i).makeCopy(network.getNeurons(),network));
        network.setNodeCnt(nodeCnt);
        network.setRNG(rng);
        return network;
    }
    
    public NeuralNetwork copyAndMutate(){
        NeuralNetwork network=copy();
        network.mutate();
        return network;
    }
    
    private void nextGeneration(){
        for(int i=0;i<neurons.size();i++)
            neurons.get(i).deEvaluate();
    }
    
    private ArrayList<InputNeuron> findInputs(){
        ArrayList<InputNeuron> ins=new ArrayList<>();
        for(int i=0;i<neurons.size();i++)
            if(neurons.get(i)instanceof InputNeuron)
                ins.add((InputNeuron)neurons.get(i));
        return ins;
    }
    
    private ArrayList<OutputNeuron> findOutputs(){
        ArrayList<OutputNeuron> outs=new ArrayList<>();
        for(int i=0;i<neurons.size();i++)
            if(neurons.get(i)instanceof OutputNeuron)
                outs.add((OutputNeuron)neurons.get(i));
        return outs;
    }
    
    public Neuron getSpecific(int index){
        for(int i=0;i<neurons.size();i++)
            if(neurons.get(i).getInnovationNum()==index)
                return neurons.get(i);
        return null;
    }
    
    // a merge sort to sort a list of neural networks by their fitness
    public static ArrayList<NeuralNetwork> sort(ArrayList<NeuralNetwork> net){
        ArrayList<NeuralNetwork> one=new ArrayList<>();
        ArrayList<NeuralNetwork> two=new ArrayList<>();
        int i;
        for(i=0;i<net.size()/2;i++)
            one.add(net.get(i));
        for(;i<net.size();i++)
            two.add(net.get(i));
        one=sort(one);
        two=sort(two);
        return merge(one,two);
    }
    
    public static ArrayList<NeuralNetwork> merge(ArrayList<NeuralNetwork> one,ArrayList<NeuralNetwork> two){
        ArrayList<NeuralNetwork> merged=new ArrayList<>();
        while(!one.isEmpty()&&!two.isEmpty()){
            if(one.get(0).getFitness()>two.get(0).getFitness())
                merged.add(one.remove(0));
            else
                merged.add(two.remove(0));
        }
        while(!one.isEmpty())
            merged.add(one.remove(0));
        while(!two.isEmpty())
            merged.add(two.remove(0));
        return merged;
    }

    // THESE METHODS ARE USED FOR CMD TESTING
    public Node getNode(int nodeNum){
        for(int i=0;i<neurons.size();i++)
            if(neurons.get(i).getInnovationNum()==nodeNum)
                return neurons.get(i);
        for(int i=0;i<connections.size();i++)
            if(connections.get(i).getInnovationNum()==nodeNum)
                return connections.get(i);
        return null;
    }

    public ArrayList<Node> getAllNodes(){
        ArrayList<Node> nodes=new ArrayList<>();
        for(int i=0;i<neurons.size();i++)
            nodes.add(neurons.get(i));
        for(int i=0;i<connections.size();i++)
            nodes.add(connections.get(i));
        return nodes;
    }

    // this method sets all of the nodes in the network
    public void setAllNodes(ArrayList<Node> list){
        // first clear all current nodes
        nodes.clear();
        neurons.clear();
        connections.clear();
        // now add all of the new nodes to their respective lists
        for(int i=0;i<list.size();i++){
            if(list.get(i)instanceof Neuron)
                neurons.add((Neuron)list.get(i));
            if(list.get(i)instanceof Connection)
                connections.add((Connection)list.get(i));
            nodes.add(list.get(i));
        }
    }
    
    public void reset(){
        for(int i=0;i<neurons.size();i++)
            neurons.get(i).reset();
        for(int i=0;i<connections.size();i++)
            connections.get(i).reset();
    }
    
    public String toString(){
        String data="";
        data+="Total Neurons "+neurons.size()+"\n";
        data+="Total Connections "+connections.size()+"\n";
        for(int i=0;i<neurons.size();i++){
            data+=neurons.toString();
        }
        return data;
    }
    
    // getter methods
    public ArrayList<Node> getNodes(){return nodes;}
    public ArrayList<Neuron> getNeurons(){return neurons;}
    public ArrayList<Connection> getConnections(){return connections;}
    public ArrayList<Integer> getInputs(){return inputs;}
    public ArrayList<Integer> outputs(){return outputs;}
    public RandomNumberGenerator getRNG(){return rng;} // needed for CMDTest
    public double getFitness(){return fitness;}
    public int getNodeCnt(){return nodeCnt;}
    
    // setter methods
    public void setNeurons(ArrayList<Neuron> param){neurons=param;}
    public void setConnections(ArrayList<Connection> param){connections=param;}
    public void setInputs(ArrayList<Integer> param){inputs=param;}
    public void setOutputs(ArrayList<Integer> param){outputs=param;}
    public void setRNG(RandomNumberGenerator param){rng=param;}
    public void setFitness(double param){fitness=param;}
    public void setNodeCnt(int param){nodeCnt=param;}
}