package networks;
import nodes.Node;
import nodes.neurons.InputNeuron_Add;
import nodes.neurons.OutputNeuron_Add;
import nodes.neurons.InputNeuron;
import nodes.neurons.OutputNeuron;
import nodes.neurons.Neuron;
import nodes.neurons.Neuron_Add;
import nodes.connections.Connection;
import nodes.connections.RecurrentConnection;
import experiments.Test;
import evolution.GlobalConstants;
import evolution.SpeciationFunctions;
import evolution.HistoricalTracker;
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
    private HistoricalTracker tracker;
    private RandomNumberGenerator rng;
    private double fitness;
    private int nodeCnt; // this will be global soon
    private int currentPropogationStep;
    private boolean depthNotFound;
    private boolean usingSpeciation;
    
    public NeuralNetwork(HistoricalTracker param){
        this(param,2,1);
    }
    
    public NeuralNetwork(HistoricalTracker param,int ins,int outs){
        neurons=new ArrayList<>();
        nodes=new ArrayList<>();
        connections=new ArrayList<>();
        inputs=new ArrayList<>();
        outputs=new ArrayList<>();
        tracker=param;
        //System.out.println("Historical Tracker was set :: Neural Network");
        rng=new RandomNumberGenerator();
        fitness=0.0;
        nodeCnt=0;
        currentPropogationStep=0;
        depthNotFound=true;
        usingSpeciation=true; // set to true to test speciation
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
        int i;
        int f;
        for(i=0;i<insnum;i++){
            InputNeuron_Add neuron=new InputNeuron_Add();
            neuron.setInputID(i);
            if(tracker==null)
                System.out.println("Historical Tracker is null :: NeuralNetwork");
            if(usingSpeciation)
                neuron.setInnovationNum(i);
            else
                neuron.setInnovationNum(nodeCnt++);
            neurons.add(neuron);
            nodes.add(neuron);
        }
        for(f=0;f<outsnum;f++){
            OutputNeuron_Add neuron=new OutputNeuron_Add();
            neuron.setOutputID(f);
            if(usingSpeciation){
                neuron.setInnovationNum(f+i);
                //if(tracker.notSet())
                //    tracker.setNextInnovation(insnum+outsnum);
            }
            else
                neuron.setInnovationNum(nodeCnt++);
            neurons.add(neuron);
            nodes.add(neuron);
        }
        ArrayList<InputNeuron> ins=findInputs();
        ArrayList<OutputNeuron> outs=findOutputs();
        for(i=0;i<ins.size();i++)
            for(f=0;f<outs.size();f++)
                makeConnection(ins.get(i),outs.get(f),insnum+outsnum+f+i*outs.size());
        //if(tracker!=null){
        //    //if(tracker.getInputNums().isEmpty())
        //        //for(int i=0;i<ins.size();i++)
        //}
        if(tracker!=null){
            //tracker.endGeneration();
            if(tracker.notSet())
                tracker.setNextInnovation(insnum+outsnum+(f+1)*(i+1));
        }
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
            mutateTopology();
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
    
    // mutates the topology of the neural network
    public void mutateTopology(){
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
                mutateTopology(); // TODO ::  need to rewrite to avoid this case
                //return;
            }
        }
        else if(neuron instanceof OutputNeuron){
            ArrayList<Connection> inputs=neuron.getInputs();
            if(newNeuron>.5&&neurons.size()<GlobalConstants.MAX_NEURONS){
                addNeuron(inputs,neuron);
            }else if(connections.size()>GlobalConstants.MIN_CONNECTIONS){
                turnOffConnection(inputs,neuron);
            }else{
                mutateTopology();
                //return;
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
                    mutateTopology();
                    //return;
                }
            }else{
                if(newNeuron>.5&&neurons.size()<GlobalConstants.MAX_NEURONS){
                    addNeuron(outputs,neuron);
                }else if(connections.size()>GlobalConstants.MIN_CONNECTIONS){
                    turnOffConnection(outputs,neuron);
                }else{
                    mutateTopology();
                    //return;
                }
            }
        }
    }
    
    // new (More efficient new Random Connection method)
    public void newRandomConnection(Neuron neuron){
        //ArrayList<OutputNeuron> outputs=findOutputs();
        //    for(int i=0;i<outputs.size();i++)
        //        outputs.get(i).findDepth();
        for(int i=0;i<neurons.size();i++)
            neurons.get(i).findDepth();
        if(!findDepthSanityCheck()){
            System.out.println("Sanity check failed before add connection :: NeuralNetwork");
            System.exit(2);
        }
        if(neuron instanceof OutputNeuron){
            mutate();
            return;
        }
        // sort by depth, in theory, should be the one to find the depths
        neurons=Neuron.sortByDepth(neurons); // this should not run every time
        int index=0;
        for(;index<neurons.size()&&neurons.get(index)!=neuron;index++){}
        int maxRan=(new Random()).nextInt(neurons.size()-index);
        //outputs=neurons.get(index+maxRan).findOutputs();
        //    for(int i=0;i<outputs.size();i++)
        //        outputs.get(i).findDepth();
        //if(!findDepthSanityCheck()){
        //    System.out.println("Sanity check failed before add connection :: NeuralNetwork");
        //    System.exit(2);
        //}
        if(maxRan==0){
            // recurrent connection to self
            //mutate();
            if(neuron instanceof InputNeuron || neuron instanceof OutputNeuron){
                mutate();
                return;
            }
            System.out.println("Making a RecurrentConnection :: NeuralNetwork");
            makeRecurrentConnection(neuron);
            return;
        }
        if(neuron.existsConnection(neurons.get(index+maxRan))){
            // the connection already exists
            mutate();
            return;
        }
        //System.out.println("A connection was made :: NeuralNetwork");
        makeConnection(neuron,neurons.get(index+maxRan));
    }
    
    // THIS METHOD IS VERY INEFFICIENT FIX IT LATER TODO
    // Temporarily DEPRECIATED :: ZACK
    /*public void newRandomConnectionWorks(Neuron neuron){
        ArrayList<Neuron> connected=new ArrayList<>();
        //neurons=Neuron.sortByDepth(neurons);
        for(int i=0;i<neuron.getInputs().size();i++)
            connected.add(neuron.getInputs().get(i).getGiveNeuron());
        for(int i=0;i<neuron.getOutputs().size();i++)
            connected.add(neuron.getOutputs().get(i).getRecieveNeuron());
        connected.add(neuron);
        ArrayList<Integer> indexes=new ArrayList<>();
        for(int i=0;i<neurons.size();i++)
            if(!connected.contains(neurons.get(i)))
                indexes.add(i);
        if(indexes.isEmpty()){
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
    }*/

    // creates a new connection between two neurons
    // TODO :: EDIT THIS
    public Connection makeConnection(Neuron give,Neuron recieve){
        Connection connection;
        //if(give.getInnovationNum()==recieve.getInnovationNum()){
        //    connection=new RecurrentConnection();
        //    System.out.println("Created a Recurrent Connection");
        //}else{
            connection=new Connection();
        //}
        connection.setEvaluated(false);
        connection.setActive(true);
        connection.setGiveNeuron(give);
        connection.setRecieveNeuron(recieve);
        give.getOutputs().add(connection);
        recieve.getInputs().add(connection);
        if(usingSpeciation){
            connection.setInitIn(give.getInnovationNum());
            connection.setInitOut(recieve.getInnovationNum());
            tracker.defineConnection(connection);
        }
        else
            connection.setInnovationNum(nodeCnt++);
        connection.setWeight(rng.simpleDouble());
        connections.add(connection);
        nodes.add(connection);
        return connection;
    }
    
    public RecurrentConnection makeRecurrentConnection(Neuron neuron){
        RecurrentConnection connection=new RecurrentConnection();
        connection.setEvaluated(false);
        connection.setActive(true);
        connection.setGiveNeuron(neuron);
        connection.setRecieveNeuron(neuron);
        neuron.getOutputs().add(connection);
        neuron.getInputs().add(connection);
        if(usingSpeciation){
            connection.setInitIn(neuron.getInnovationNum());
            connection.setInitOut(neuron.getInnovationNum());
            tracker.defineConnection(connection);
        }
        else
            connection.setInnovationNum(nodeCnt++);
        connection.setWeight(rng.simpleDouble());
        connections.add(connection);
        nodes.add(connection);
        return connection;
    }
    
    public Connection makeConnection(Neuron give,Neuron recieve,int num){
        //System.out.println("DOES THIS EVEN HAVE FUNCTIONALITY? :: NeuralNetwork");
        Connection connection=new Connection();
        connection.setEvaluated(false);
        connection.setActive(true);
        connection.setGiveNeuron(give);
        connection.setRecieveNeuron(recieve);
        give.getOutputs().add(connection);
        recieve.getInputs().add(connection);
        if(usingSpeciation){
            connection.setInitIn(give.getInnovationNum());
            connection.setInitOut(recieve.getInnovationNum());
            //tracker.defineConnection(connection);
            connection.setInnovationNum(num);
        }
        else
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
            System.out.println("List of connections is empty :: NeuralNetwork");
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
        if(usingSpeciation){
            newNeuron.setInitIn(neuron.getInnovationNum());
            newNeuron.setInitOut(otherNeuron.getInnovationNum());
            //System.out.println("This was ran!!!");
            tracker.defineNeuron(newNeuron);
            if(newNeuron.getInnovationNum()<0)
                System.out.println("Start");
            if(tracker==null)
                System.out.println("Tracker is null");
        }
        else
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
        if(newNeuron.getInnovationNum()<0)
            System.out.println("Innovation number is not set");
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
    public NeuralNetwork crossOver(NeuralNetwork other){ // ERRORS
        NeuralNetwork newNetwork;
        ArrayList<NodeToNode> similar;
        if(fitness>other.getFitness())
            similar=SpeciationFunctions.getSimilarNodes(this,other);
        else
            similar=SpeciationFunctions.getSimilarNodes(other,this);
        ArrayList<Node> newNodes=new ArrayList<>();
        boolean hasBetterFitness=fitness>other.getFitness();
        if(hasBetterFitness)
            newNetwork=copy();
        else
            newNetwork=other.copy();
        // keep implementing
        Random random=new Random();
        for(int i=0;i<similar.size();i++){
            if(similar.get(i).getTwo()!=null){
                if(random.nextDouble()>.78){
                    newNodes.add(similar.get(i).mix());
                    //if(newNodes.get(newNodes.size()-1)==null)
                    //    System.out.println("Error is In MIX :: Crossover");
                }else{
                    if(hasBetterFitness)
                        newNodes.add(similar.get(i).getOne());
                    else
                        newNodes.add(similar.get(i).getTwo());
                }
            }else
                newNodes.add(similar.get(i).getOne());
        }
        for(int i=0;i<newNodes.size();i++){
            for(int f=0;f<newNetwork.getNeurons().size();f++){
                //if(newNetwork.getNeurons()==null)
                //    System.out.println("NewNetwork.Neurons are null");
                //if(newNodes.get(i)==null)
                //    System.out.println("NewNodes is null");
                if(newNetwork.getNeurons().get(f).getInnovationNum()==newNodes.get(i).getInnovationNum()){
                    newNetwork.getNeurons().get(f).setBias(((Neuron)(newNodes.get(i))).getBias());
                }
            }
            for(int f=0;f<newNetwork.getConnections().size();f++){
                if(newNetwork.getConnections().get(f).getInnovationNum()==newNodes.get(i).getInnovationNum()){
                    newNetwork.getConnections().get(f).setWeight(((Connection)(newNodes.get(i))).getWeight());
                }
            }
        }
        //newNetwork.setAllNodes(newNodes);
        return newNetwork;
    }
    
    public ArrayList<Double> run(Test param){
        //try{
            ArrayList<OutputNeuron> outputs=findOutputs();
            for(int i=0;i<outputs.size();i++)
                outputs.get(i).findDepth();
        //try{
            if(!findDepthSanityCheck()){
                System.out.println("Find Depth sanity test failed");
                new CMDTester(this);
            }
                //System.exit(2);
            //if(neurons.size()>5){
            //    for(int i=0;i<neurons.size();i++)
            //        System.out.println("Depth for Neuron "+i+" :: "+neurons.get(i).getDepth());
            //    //System.exit(0);
            //    new CMDTester(this);
            //}
            ArrayList<InputNeuron> inputs=findInputs();
            ArrayList<Double> results=new ArrayList<>();
            for(int i=0;i<param.getInputs().size();i++){
                inputs.get(i).setInput(param.getInputs().get(i));
            }
            for(int i=0;i<outputs.size();i++){
                outputs.get(i).evaluate();
                results.add(outputs.get(i).getOutput());
            }
        try{
            nextGeneration();
            return results;
        }catch(StackOverflowError e){
            System.out.println("STACK OVERFLOW");
            //ArrayList<OutputNeuron> outputs=findOutputs();
            //for(int i=0;i<outputs.size();i++) // find depth is causing the stack overflow !!!
            //    outputs.get(i).findDepth();
            new CMDTester(this);
            //System.exit(0);
            return null;
        }
    }
    
    // not done implementing yet
    public void stepPropogate(){
        if(depthNotFound){
            for(int i=0;i<neurons.size();i++){
                neurons.get(i).findDepth();
            }
            depthNotFound=false;
        }
        neurons=Neuron.sortByDepth(neurons);
        //findDepthUnitTest();
        // implement the step propogation
    }
    
    // DEPRECIATED :: ZACK
    //private void findDepthUnitTest(){
    //    for(int i=0;i<neurons.size();i++){
    //        if(neurons.get(i).getDepth()==-1)
    //            System.out.println("Neuron's depth value not set");
    //        for(int f=0;f<i;f++)
    //            if(neurons.get(f).getDepth()>neurons.get(i).getDepth())
    //                System.out.println("List not sorted correctly");
    //        // add more tests
    //    }
    //}
    
    // a sanity check for the find depth method
    private boolean findDepthSanityCheck(){
        ArrayList<InputNeuron> inputs=findInputs();
        ArrayList<OutputNeuron> outputs=findOutputs();
        for(int i=0;i<inputs.size();i++)
            if(inputs.get(i).getDepth()!=0){
                System.out.println("Input depth not right");
                return false;
            }
        for(int i=0;i<outputs.size();i++)
            for(int f=0;f<neurons.size();f++){
                if(neurons.get(f).getDepth()>outputs.get(i).getDepth()&&!outputs.contains(neurons.get(f))){
                    System.out.println("Output Neurons are not the max");
                    return false;
                }
            }
        for(int i=0;i<neurons.size();i++)
            if(neurons.get(i).getDepth()==0&&!(neurons.get(i)instanceof InputNeuron)){
                System.out.println("A Neuron cannot have depth 0");
                return false;
            }
        return true;
    }

    public NeuralNetwork copy(){
        NeuralNetwork network=new NeuralNetwork(tracker);
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
    
    // returns all of the output neurons in this network
    public ArrayList<OutputNeuron> findOutputs(){
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
        if(net.size()==0)
            return new ArrayList<NeuralNetwork>();
        if(net.size()==1)
            return net;
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
        for(int i=0;i<connections.size();i++){
            //if(connections.get(i)instanceof RecurrentConnection)
            //    System.out.println("There is a recurrent connection among us :: NeuralNetwork");
            nodes.add(connections.get(i));
        }
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
    public int getCurrentPropogationStep(){return currentPropogationStep;}
    
    // setter methods
    public void setNeurons(ArrayList<Neuron> param){neurons=param;}
    public void setConnections(ArrayList<Connection> param){connections=param;}
    public void setInputs(ArrayList<Integer> param){inputs=param;}
    public void setOutputs(ArrayList<Integer> param){outputs=param;}
    public void setRNG(RandomNumberGenerator param){rng=param;}
    public void setFitness(double param){fitness=param;}
    public void setNodeCnt(int param){nodeCnt=param;}
}