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
import nodes.neurons.InputNeuron;
import nodes.neurons.OutputNeuron;
import java.util.Scanner;
public class CMDTester {
    private NeuralNetwork net;
    private Scanner input;

    public CMDTester(){
    	net=new NeuralNetwork();
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
                System.out.println("New Neuron's Innovation :: "+net.getNodeCnt()-1);
            }
            if(in.equals("ac")){
                System.out.print("GiveNum :: ");
                int give=input.nextInt();
                System.out.print("RecievNum :: ");
                int recieve=input.nextInt();
                addConnection(give,recieve);
                System.out.println("New Connection's Innovation :: "+net.getNodeCnt()-1)
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
            System.out.println(output+nodes.getInnovationNum());
            index++;
        }
        while(index<nodes.size()){
            System.out.println("Connection :: "+nodes.getInnovationNum());
            index++;
        }
    }

    private void displayNeuronInfo(int num){
    	Neuron neuron=(Neuron)(net.getNode(num));
        String output="";
        output+="InnovationNum :: "+neuron.getInnovationNum()+"\n";
        output+="Bias :: "+neuron.getBias()+"\n";
        output+="Inputs :: "+Neuron.getInnovations(neuron.getInputs())+"\n";
        output+="Outputs :: "+Neuron.getInnovations(neuron.getOutputs())+"\n";
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
    	// implement
    }

    private void mutateNeuron(int num){
        // implement
    }

    private void mutateConnection(int num){
        // implement
    }

    private void addConnection(int giveNum,int recieveNum){
    	// implement
    }

    private void removeNeuron(int num){
        // implement
    }

    private void removeConnection(int num){
        // implement
    }

    private void replicateGeneration(){
    	// implement
    }

    private void testResults(){
        // implement
    }

    private void sort(ArrayList<Node> nodes){
        ArrayList<Node> connections=new ArrayList<>();
        ArrayList<Node> neurons=new ArrayList<>();
        for(int i=0;i<nodes.size();i++){
            if(nodes.get(i) instanceof Neuron)
                neurons.add(nodes.get(i));
            else
                connections.add(nodes.get(i));
        }
        neurons=sortNodes(neurons);
        connections=sortNodes(connections);
        for(int i=0;i<connections.size();i++)
            neurons.add(connections.get(i));
        return neurons;
    }

    private ArrayList<Node> sortNodes(ArrayList<Node> nodes){
        ArrayList<Node> one=new ArrayList<>();
        ArrayList<Node> two=new ArrayList<>();
        int i=0;
        for(;i<nodes.size()/2;i++)
            one.add(nodes.get(i));
        for(;i<nodes.size();i++)
            two.add(nodes.get(i));
        return mergeNodes(sortNodes(one,two));
    }

    private ArrayList<Node> mergeNodes(ArrayList<Node> one,ArrayList<Node> two){
        ArrayList<Node> sorted=new ArrayList<>();
        while(!one.isEmpty()||!two.isEmpty()){
            if(one.get(0).getInnovationNum()<two.get(0).getInnovationNum())
                sorted.add(one.remove(0));
            else
                sorted.add(two.remove(0));
        }
        while(!one.isEmpty())
            sorted.add(one.remove(0));
        while(!two.isEmpty())
            sorted.add(two.remove(0));
        return sorted;
    }
}