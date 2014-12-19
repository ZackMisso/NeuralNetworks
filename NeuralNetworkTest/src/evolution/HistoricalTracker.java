/**
 *
 * @author Zackary Misso
 * 
 */
package evolution;
import nodes.Node;
import nodes.connections.Connection;
import nodes.neurons.Neuron;
import java.util.ArrayList;
public class HistoricalTracker {
    //private ArrayList<Node> master; // contains all developed genes
    private ArrayList<Node> addedNodes; // contains new genes added this generation (need to implement this)
    private ArrayList<Integer> connectionNums;
    private ArrayList<Integer> neuronNums;
    private ArrayList<Integer> inputNums; // innovation numbers of the inputs
    private ArrayList<Integer> outputNums; // innovation numbers of the outputs
    private int nextInnovation; // the next node identifier
    private int nextSpecies; // the next species identifier
    
    public HistoricalTracker(){ // default constructor
        //master=new ArrayList<>();
        addedNodes=new ArrayList<>();
        connectionNums=new ArrayList<>();
        neuronNums=new ArrayList<>();
        nextInnovation=0;
        nextSpecies=0;
        initializeMaster();
    }
    
    // initializes the master node list
    private void initializeMaster(){
        // implement
    }
    
    // returns the current nest species identifier and increments it
    public int nextSpecies(){
        return nextSpecies++;
    }
    
    // returns the current nest node identifier and increments it
    public int nextInnovationNum(){
        return nextInnovation++;
    }
    
    // MAYBE DONT NEED
    public void endGeneration(){
        //System.out.println("Resetting historical tracker");
        addedNodes.clear();
    }
    
    public boolean notSet(){
        return nextInnovation==0;
    }
    
    // possibly need?
    //public void beginGeneration(){
    //    Node.sort(master);
    //}
    
    // defines a new node based on what was evolved in the past
    //public void defineNode(Node added){
    //    if(added instanceof Connection)
    //        defineConnection((Connection)added);
    //    else
    //        defineNeuron((Neuron)added);
    //}
    
    // TODO :: FIX THE INNEFFICIENTCIES HERE
    
    public void defineNeuron(Neuron added){
        for(int i=0;i<addedNodes.size();i++){
            //System.out.println("THIS WAS RAN");
            if(addedNodes.get(i).getInitIn()==added.getInitIn()){
                //System.out.println("The input is the same");
                //System.out.println("Other Out :: "+addedNodes.get(i).getInitOut());
                //System.out.println("This Out :: "+added.getInitOut());
                //System.out.println("THis IN :: "+added.getInitIn());
                if(addedNodes.get(i).getInitOut()==added.getInitOut()){
                    if(addedNodes.get(i)instanceof Neuron){
                        //addedNodes.get(i).setInnovationNum(added.getInnovationNum());
                        added.setInnovationNum(addedNodes.get(i).getInnovationNum());
                        return;
                    }
                }
            }
        }
        //System.out.println("Neuron not defined :: HistoricalTracker");
        //System.out.print(addedNodes.size());
        addedNodes.add(added);
        //System.out.println(" "+addedNodes.size());
        neuronNums.add(nextInnovation);
        added.setInnovationNum(nextInnovationNum());
        if(added.getInnovationNum()<0)
            System.out.println("Error with the innovation number in tracker :: HistoricalTracker");
    }
    
    public void defineConnection(Connection added){
        for(int i=0;i<addedNodes.size();i++){
            if(addedNodes.get(i).getInitIn()==added.getInitIn()){
                //System.out.println("The input is the same");
                //System.out.println("Other Out :: "+addedNodes.get(i).getInitOut());
                //System.out.println("This Out :: "+added.getInitOut());
                //System.out.println("THis IN :: "+added.getInitIn());
                if(addedNodes.get(i).getInitOut()==added.getInitOut()){
                    if(addedNodes.get(i)instanceof Connection){
                        //addedNodes.get(i).setInnovationNum(added.getInnovationNum());
                        added.setInnovationNum(addedNodes.get(i).getInnovationNum());
                        //System.out.println("Found a copy");
                        return;
                    }
                }
            }
        }
        //System.out.print(addedNodes.size());
        addedNodes.add(added);
        //System.out.println(" "+addedNodes.size());
        connectionNums.add(nextInnovation);
        added.setInnovationNum(nextInnovationNum());
    }
    
    // defines a connection based on what was evolved in the past
    //private void defineConnection(Connection added){
    //    boolean chk=true;
    //   ArrayList<Connection> connections=getConnectionsFromMaster();
    //    for(int i=0;i<connections.size()&&chk;i++)
    //        if(connections.get(i).isSameConnection(added))
    //            chk=false;
    //    if(chk){
    //        added.setInnovationNum(nextInnovation++);
    //        //master.add(added);
    //        // possibly add more
    //    }
    //}
    
    // defines a neuron based on what was evolved in the past
    //private void defineNeuron(Neuron added){
    //    boolean chk=true;
    //    ArrayList<Neuron> neurons=getNeuronsFromMaster();
    //    for(int i=0;i<neurons.size()&&chk;i++)
    //        if(neurons.get(i).isSameNeuron(added))
    //            chk=false;
    //    if(chk){
    //        added.setInnovationNum(nextInnovation++);
    //        //master.add(added);
    //    }
    //}
    
    // gets all the neurons from the master list
    //private ArrayList<Neuron> getNeuronsFromMaster(){
    //    ArrayList<Neuron> neurons=new ArrayList<>();
    //    for(int i=0;i<master.size();i++)
    //       if(master.get(i)instanceof Neuron)
    //            neurons.add((Neuron)master.get(i));
    //    return neurons;
    //}
    
    // gets all the connections from the master list
    //private ArrayList<Connection> getConnectionsFromMaster(){
    //   ArrayList<Connection> connections=new ArrayList<>();
    //    for(int i=0;i<master.size();i++)
    //        if(master.get(i)instanceof Connection)
    //            connections.add((Connection)master.get(i));
    //    return connections;
    //}
    
    // getter methods
    //public ArrayList<Node> getMaster(){return master;}
    public ArrayList<Node> getAddedNodes(){return addedNodes;}
    public ArrayList<Integer> getConnectionNums(){return connectionNums;}
    public ArrayList<Integer> getNeuronNums(){return neuronNums;}
    public ArrayList<Integer> getInputNums(){return inputNums;}
    public ArrayList<Integer> getOutputNums(){return outputNums;}
    
    // setter methods
    //public void setMaster(ArrayList<Node> param){master=param;}
    public void setNextInnovation(int param){nextInnovation=param;}
    public void setAddedNodes(ArrayList<Node> param){addedNodes=param;}
    public void setConnectionNums(ArrayList<Integer> param){connectionNums=param;}
    public void setNeuronNums(ArrayList<Integer> param){neuronNums=param;}
    public void setInputNums(ArrayList<Integer> param){inputNums=param;}
    public void setOutputNums(ArrayList<Integer> param){outputNums=param;}
}