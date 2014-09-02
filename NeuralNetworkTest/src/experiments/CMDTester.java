/**
 *
 * @author Zackary Misso
 *
 */
package experiments;
import networks.NeuralNetwork;
import nodes.Node;
import nodes.connections.Connection;
import nodes.neurons.Neuron;
import nodes.neurons.Neuron_Add;
import nodes.neurons.InputNeuron;
import nodes.neurons.OutputNeuron;
import java.util.Scanner;
import java.util.ArrayList;
public class CMDTester {
    private NeuralNetwork net;
    private Scanner input;

    public CMDTester(){
    	net=new NeuralNetwork();
    	input=new Scanner(System.in);
    	displayNetworkInfo();
    	run();
    }
    
    public CMDTester(NeuralNetwork network){
        net=network;
        input=new Scanner(System.in);
        displayNetworkInfo();
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

    private void displayNetworkInfo(){
    	ArrayList<Node> nodes=net.getAllNodes();
        nodes=sort(nodes);
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
    	Neuron neuron=(Neuron)(net.getNode(num));
        String output="";
        output+="InnovationNum :: "+neuron.getInnovationNum()+"\n";
        output+="Bias :: "+neuron.getBias()+"\n";
        output+="Inputs :: "+Connection.getInnovations(neuron.getInputs())+"\n";
        output+="Outputs :: "+Connection.getInnovations(neuron.getOutputs())+"\n";
        output+="Threshold :: "+neuron.getThreshold()+"\n";
        System.out.println(output);
    }

    private void displayConnectionInfo(int num){
    	Connection connection=(Connection)(net.getNode(num));
        String output="";
        output+="InnovationNum :: "+connection.getInnovationNum()+"\n";
        output+="Weight :: "+connection.getWeight()+"\n";
        output+="GiveNeuron :: "+connection.getGiveNeuron().getInnovationNum()+"\n";
        output+="RecieveNeuron :: "+connection.getRecieveNeuron().getInnovationNum()+"\n";
        System.out.println(output);
    }

    private void addNeuron(int num){
    	Connection oldConnection=(Connection)(net.getNode(num));
        Neuron bot=oldConnection.getGiveNeuron();
        Neuron top=oldConnection.getRecieveNeuron();
        Neuron_Add newNeuron=new Neuron_Add();
        newNeuron.setInnovationNum(net.getNodeCnt());
        net.setNodeCnt(net.getNodeCnt()+1);
        net.getNeurons().add(newNeuron);
        net.makeConnection(newNeuron,top);
        net.makeConnection(bot,newNeuron);
        net.getConnections().remove(oldConnection);
        bot.getOutputs().remove(oldConnection);
        top.getInputs().remove(oldConnection);
    }

    private void mutateNeuron(int num){
        Neuron neuron=(Neuron)(net.getNode(num));
        System.out.println("Old Bias :: "+neuron.getBias());
        neuron.mutateBias(net.getRNG());
        System.out.println("New Bias :: "+neuron.getBias());
    }

    private void mutateConnection(int num){
        Connection connection=(Connection)(net.getNode(num));
        System.out.println("Old Weight :: "+connection.getWeight());
        connection.mutateWeight(net.getRNG());
        System.out.println("New Weight :: "+connection.getWeight());
    }

    private void addConnection(int giveNum,int recieveNum){
    	Neuron give=(Neuron)(net.getNode(giveNum));
        Neuron recieve=(Neuron)(net.getNode(recieveNum));
        net.makeConnection(give,recieve);
        System.out.println("New Connection Was Made :: "+(net.getNodeCnt()-1));
    }

    private void removeNeuron(int num){
        Neuron neuron=(Neuron)(net.getNode(num));
        net.getNeurons().remove(neuron);
        for(int i=0;i<neuron.getInputs().size();i++){
            Connection connection=neuron.getInputs().get(i);
            connection.getGiveNeuron().getOutputs().remove(connection);
            net.getConnections().remove(connection);
        }
        for(int i=0;i<neuron.getOutputs().size();i++){
            Connection connection=neuron.getOutputs().get(i);
            connection.getRecieveNeuron().getInputs().remove(connection);
            net.getConnections().remove(connection);
        }
        System.out.println("Neuron :: "+num+" :: was removed");
    }

    private void removeConnection(int num){
        Connection connection=(Connection)(net.getNode(num));
        net.getConnections().remove(connection);
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
        test.runXORTests(net);
        System.out.println("Achieved Fitness :: "+net.getFitness());
    }

    private void replaceWithCopy(){
        System.out.println("Replacing current network with a copy");
        net=net.copy();
    }

    private void recreateNeuralNetwork(){
        // implement
    }

    private ArrayList<Node> sort(ArrayList<Node> nodes){
        ArrayList<Node> connections=new ArrayList<>();
        ArrayList<Node> neurons=new ArrayList<>();
        for(int i=0;i<nodes.size();i++){
            if(nodes.get(i) instanceof Neuron)
                neurons.add(nodes.get(i));
            else
                connections.add(nodes.get(i));
        }
        neurons=Node.sort(neurons);
        connections=Node.sort(connections);
        for(int i=0;i<connections.size();i++)
            neurons.add(connections.get(i));
        return neurons;
    }
}