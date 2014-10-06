/**
 *
 * @author Zackary Misso
 *
 */
package testtools;
import networks.NeuralNetwork;
import evolution.HistoricalTracker;
import evolution.SpeciationFunctions;
import evolution.species.Species;
import experiments.TestCases;
import nodes.Node;
import nodes.connections.Connection;
import nodes.neurons.Neuron;
import nodes.neurons.Neuron_Add;
import nodes.neurons.InputNeuron;
import nodes.neurons.OutputNeuron;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
public class CMDTester {
    private HistoricalTracker history;
    private Species species;
    private Species species2;
    private Species currentSpecies;
    private NeuralNetwork net;
    private NeuralNetwork net2; // to test speciation distance
    private NeuralNetwork currentNetwork;
    private Scanner input;
    private String fileName;
    private boolean batchInput; // input from a file
    private boolean speciesMode;
    private boolean networkMode;
    // TODO :: ADD SPECIES FUNCTIONALITY
    // TODO :: POSSIBLY ADD HISTORICALTRACKER FUNCTIONALITY
    public CMDTester(){
    	//net=new NeuralNetwork();
        fileName="instructions.txt";
        batchInput=true;
        try{
            input=new Scanner(new File(fileName));
        }catch(IOException e) {}
        net=null;
        net2=null;
        species=null;
        species2=null;
        speciesMode=false;
        networkMode=false;
        displayNetworkInfo();
    	run();
    }
    
    public CMDTester(NeuralNetwork network){
        currentNetwork=network;
        net=network;
        net2=null;
        input=new Scanner(System.in);
        displayNetworkInfo();
        fileName="";
        batchInput=false;
        speciesMode=false;
        networkMode=true;
        run();
    }
    
    public CMDTester(Species spec){
        currentSpecies=spec;
        species=spec;
        input=new Scanner(System.in);
        displaySpeciesInfo();
        fileName="";
        batchInput=false;
        networkMode=false;
        speciesMode=true;
        run();
    }

    public void run(){
    	while(true){
            System.out.print("Enter Instruction :: ");
    		String in=input.nextLine();
            if(in.equals("dn"))
                displayNetworkInfo();
            if(in.equals("dni")){
                System.out.print("Enter NeuronNum :: ");
                displayNeuronInfo(input.nextInt());
            }
            if(in.equals("dci")){
                System.out.print("Enter ConnectionNum :: ");
                displayConnectionInfo(input.nextInt());
            }
            if(in.equals("an")){
                System.out.print("What Connection would you like to split :: ");
                addNeuron(input.nextInt());
                System.out.println("New Neuron's Innovation :: "+(net.getNodeCnt()-1));
            }
            if(in.equals("ac")){
                System.out.print("GiveNum :: ");
                int give=input.nextInt();
                System.out.print("RecievNum :: ");
                int recieve=input.nextInt();
                addConnection(give,recieve);
                System.out.println("New Connection's Innovation :: "+(net.getNodeCnt()-1));
            }
            if(in.equals("mn")){
                System.out.print("Enter Neuron num :: ");
                mutateNeuron(input.nextInt());
            }
            if(in.equals("mc")){
                System.out.print("Enter Connection num :: ");
                mutateConnection(input.nextInt());
            }
            if(in.equals("rn")){
                System.out.print("Enter Neuron to Remove :: ");
                removeNeuron(input.nextInt());
            }
            if(in.equals("rc")){
                System.out.print("Enter Connection to Remove :: ");
                removeConnection(input.nextInt());
            }
            if(in.equals("rg")){
                replicateGeneration();
            }
            if(in.equals("test")){
                testResults();
            }
            if(in.equals("rwc")){
                replaceWithCopy();
            }
            if(in.equals("rnn")){
                System.out.println("Recreating NeuralNetwork");
                recreateNeuralNetwork();
            }
            // implement more as needed
    	}
    }
    
    // displays a brief summary of the individuals of a species
    private void displaySpeciesInfo(){
        ArrayList<NeuralNetwork> nets=currentSpecies.getIndividuals();
        String output="";
        for(int i=0;i<nets.size();i++){
            output+="Individual Number :: "+i+"\n";
            output+="Nodes :: "+nets.get(i).getNodes().size()+"\n";
            output+="Neurons :: "+nets.get(i).getNeurons().size()+"\n";
            output+="Connections :: "+nets.get(i).getConnections().size()+"\n";
            output+="\n";
        }
        System.out.println(output);
    }

    private void displayNetworkInfo(){
    	ArrayList<Node> nodes=currentNetwork.getAllNodes();
        nodes=Node.sort(nodes);
        int index=0;
        while(nodes.get(index)instanceof Neuron){
            String output="";
            if(nodes.get(index)instanceof InputNeuron)
                output="InputNeuron :: ";
            else if(nodes.get(index)instanceof OutputNeuron)
                output="OutputNeuron :: ";
            else
                output="Neuron :: ";
            System.out.println(output+nodes.get(index).getInnovationNum());
            index++;
        }
        while(index<nodes.size()){
            System.out.println("Connection :: "+nodes.get(index).getInnovationNum());
            index++;
        }
    }

    private void displayNeuronInfo(int num){
    	Neuron neuron=(Neuron)(currentNetwork.getNode(num));
        String output="";
        output+="InnovationNum :: "+neuron.getInnovationNum()+"\n";
        output+="Bias :: "+neuron.getBias()+"\n";
        output+="Inputs :: "+Connection.getInnovations(neuron.getInputs())+"\n";
        output+="Outputs :: "+Connection.getInnovations(neuron.getOutputs())+"\n";
        output+="Threshold :: "+neuron.getThreshold()+"\n";
        System.out.println(output);
    }

    private void displayConnectionInfo(int num){
    	Connection connection=(Connection)(currentNetwork.getNode(num));
        String output="";
        output+="InnovationNum :: "+connection.getInnovationNum()+"\n";
        output+="Weight :: "+connection.getWeight()+"\n";
        output+="GiveNeuron :: "+connection.getGiveNeuron().getInnovationNum()+"\n";
        output+="RecieveNeuron :: "+connection.getRecieveNeuron().getInnovationNum()+"\n";
        System.out.println(output);
    }

    private void addNeuron(int num){
    	Connection oldConnection=(Connection)(currentNetwork.getNode(num));
        Neuron bot=oldConnection.getGiveNeuron();
        Neuron top=oldConnection.getRecieveNeuron();
        Neuron_Add newNeuron=new Neuron_Add();
        newNeuron.setInnovationNum(currentNetwork.getNodeCnt());
        currentNetwork.setNodeCnt(currentNetwork.getNodeCnt()+1);
        currentNetwork.getNeurons().add(newNeuron);
        currentNetwork.makeConnection(newNeuron,top);
        currentNetwork.makeConnection(bot,newNeuron);
        currentNetwork.getConnections().remove(oldConnection);
        bot.getOutputs().remove(oldConnection);
        top.getInputs().remove(oldConnection);
    }

    private void mutateNeuron(int num){
        Neuron neuron=(Neuron)(currentNetwork.getNode(num));
        System.out.println("Old Bias :: "+neuron.getBias());
        neuron.mutateBias(currentNetwork.getRNG());
        System.out.println("New Bias :: "+neuron.getBias());
    }

    private void mutateConnection(int num){
        Connection connection=(Connection)(currentNetwork.getNode(num));
        System.out.println("Old Weight :: "+connection.getWeight());
        connection.mutateWeight(currentNetwork.getRNG());
        System.out.println("New Weight :: "+connection.getWeight());
    }

    private void addConnection(int giveNum,int recieveNum){
    	Neuron give=(Neuron)(currentNetwork.getNode(giveNum));
        Neuron recieve=(Neuron)(currentNetwork.getNode(recieveNum));
        currentNetwork.makeConnection(give,recieve);
        System.out.println("New Connection Was Made :: "+(currentNetwork.getNodeCnt()-1));
    }

    private void removeNeuron(int num){
        Neuron neuron=(Neuron)(currentNetwork.getNode(num));
        currentNetwork.getNeurons().remove(neuron);
        for(int i=0;i<neuron.getInputs().size();i++){
            Connection connection=neuron.getInputs().get(i);
            connection.getGiveNeuron().getOutputs().remove(connection);
            currentNetwork.getConnections().remove(connection);
        }
        for(int i=0;i<neuron.getOutputs().size();i++){
            Connection connection=neuron.getOutputs().get(i);
            connection.getRecieveNeuron().getInputs().remove(connection);
            currentNetwork.getConnections().remove(connection);
        }
        System.out.println("Neuron :: "+num+" :: was removed");
    }

    private void removeConnection(int num){
        Connection connection=(Connection)(currentNetwork.getNode(num));
        currentNetwork.getConnections().remove(connection);
        connection.getGiveNeuron().getOutputs().remove(connection);
        connection.getRecieveNeuron().getInputs().remove(connection);
        System.out.println("Connection :: "+num+" :: was removed");
    }

    private void replicateGeneration(){
        System.out.println("This has yet to be implemented :: replicateGeneration");
    	// implement later
    }

    private void testResults(){
        TestCases test=new TestCases();
        test.runXORTests(currentNetwork);
        System.out.println("Achieved Fitness :: "+currentNetwork.getFitness());
    }

    private void replaceWithCopy(){
        System.out.println("Replacing current network with a copy");
        currentNetwork=currentNetwork.copy();
    }

    private void recreateNeuralNetwork(){
        // implement
    }

    // sets the fitness of the current network
    private void setFitness(double fit){
        currentNetwork.setFitness(fit);
    }

    // switches to the specified network
    private void switchNetwork(int network){
        if(net==null||net2==null)
            System.out.println("One of the networks is null");
        else if(currentSpecies==null){
            // switch between net and net2
            if(currentNetwork==net)
                currentNetwork=net2;
            else
                currentNetwork=net;
        }else{
            // switch between the individuals in currentSpecies
            int max=currentSpecies.getIndividuals().size();
            if(network>=max)
                System.out.println("Index out of bounds! :: switchNetwork");
            else
                currentNetwork=currentSpecies.getIndividuals().get(network);
        }
    }

    // creates a new network with the inputs and outputs
    private void createNetwork(int inputs,int outputs){
        if(currentSpecies==null){
            if(net==null){
                System.out.println("Neural Network created in net");
                net=new NeuralNetwork(inputs,outputs);
                if(net2==null)
                    currentNetwork=net;
            }else if(net2==null){
                System.out.println("Neural Network created in net2");
                net2=new NeuralNetwork(inputs,outputs);
                if(net==null)
                    currentNetwork=net2;
            }else if(net==currentNetwork){
                System.out.println("Neural Network created in net2");
                net2=null;
                net2=new NeuralNetwork(inputs,outputs);
            }else{ // net2==currentNetwork
                System.out.println("Neural Network created in net");
                net=null;
                net=new NeuralNetwork(inputs,outputs);
            }
        }else{
            System.out.println("Neural Network created in the current species");
            currentSpecies.getIndividuals().add(new NeuralNetwork(inputs,outputs));
        }
    }

    ///////////// SPECIES RELATED COMMANDS //////////////////////

    // prints out the distance between two networks
    private void distanceBetween(NeuralNetwork one,NeuralNetwork two){
        System.out.println(SpeciationFunctions.findNetworkDistance(one,two));
    }

    // switches to the next species (there should only be two)
    private void switchSpecies(){
        if(species==null||species2==null)
            System.out.println("There is not two species");
        else{
            if(currentSpecies==species)
                currentSpecies=species2;
            else
                currentSpecies=species;
            switchNetwork(0);
        }
        
    }

    // creates a new species with the specified stats
    private void createSpecies(int num,int inputs,int outputs){
        if(species==null){
            species=new Species();
            for(int i=0;i<num;i++)
                species.getIndividuals().add(new NeuralNetwork(inputs,outputs));
            if(species2==null)
                currentSpecies=species;
        }else if(species2==null){
            species2=new Species();
            for(int i=0;i<num;i++)
                species2.getIndividuals().add(new NeuralNetwork(inputs,outputs));
            //currentSpecies=species2;
        }else if(species==currentSpecies){
            species2=null;
            species2=new Species();
            for(int i=0;i<num;i++)
                species2.getIndividuals().add(new NeuralNetwork(inputs,outputs));
        }else{ // species2==currentSpecies
            species=null;
            species=new Species();
            for(int i=0;i<num;i++)
                species.getIndividuals().add(new NeuralNetwork(inputs,outputs));
        }
    }
    
    // copies and mutates the current network
    private void forkAndMutate(int network){
        // implement
    }
    
    // crossover two individuals
    private void crossoverIndividuals(int one,int two){
        // implement
    }
    
    // adds a recurrent connection to the specified neuron to itself
    private void addRecurrentConnection(int num){
        // implement
    }
    
    // adds a recurrent connection between two seperate neurons
    private void addRecurrentLargeConnection(int give,int recieve){
        // implement
    }
    
    // switches to Species mode
    private void switchToSpeciesMode(){
        // implement
    }
    
    // swiches to network mode
    private void switchToNetworkMode(){
        // implement
    }

    // Deprecated :: Zack
    //private ArrayList<Node> sort(ArrayList<Node> nodes){
    //    ArrayList<Node> connections=new ArrayList<>();
    //    ArrayList<Node> neurons=new ArrayList<>();
    //    for(int i=0;i<nodes.size();i++){
    //        if(nodes.get(i) instanceof Neuron)
    //            neurons.add(nodes.get(i));
    //        else
    //            connections.add(nodes.get(i));
    //    }
    //    neurons=Node.sort(neurons);
    //    connections=Node.sort(connections);
    //    for(int i=0;i<connections.size();i++)
    //        neurons.add(connections.get(i));
    //    return neurons;
    //}
}