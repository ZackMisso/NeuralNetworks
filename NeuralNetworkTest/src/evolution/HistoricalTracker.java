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
    private ArrayList<Node> master; // contains all developed genes
    private ArrayList<Node> addedNodes; // contains new genes added this generation (need to implement this)
    private int nextInnovation; // the next node identifier
    private int nextSpecies; // the next species identifier
    
    public HistoricalTracker(){ // default constructor
        master=new ArrayList<>();
        addedNodes=new ArrayList<>();
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
        //addedNodes.clear();
    }
    
    // possibly need?
    public void beginGeneration(){
        Node.sort(master);
    }
    
    // defines a new node based on what was evolved in the past
    public void defineNode(Node added){
        if(added instanceof Connection)
            defineConnection((Connection)added);
        else
            defineNeuron((Neuron)added);
    }
    
    // TODO :: FIX THE INNEFFICIENTCIES HERE
    
    // defines a connection based on what was evolved in the past
    private void defineConnection(Connection added){
        boolean chk=true;
        ArrayList<Connection> connections=getConnectionsFromMaster();
        for(int i=0;i<connections.size()&&chk;i++)
            if(connections.get(i).isSameConnection(added))
                chk=false;
        if(chk){
            added.setInnovationNum(nextInnovation++);
            master.add(added);
            // possibly add more
        }
    }
    
    // defines a neuron based on what was evolved in the past
    private void defineNeuron(Neuron added){
        boolean chk=true;
        ArrayList<Neuron> neurons=getNeuronsFromMaster();
        for(int i=0;i<neurons.size()&&chk;i++)
            if(neurons.get(i).isSameNeuron(added))
                chk=false;
        if(chk){
            added.setInnovationNum(nextInnovation++);
            master.add(added);
        }
    }
    
    // gets all the neurons from the master list
    private ArrayList<Neuron> getNeuronsFromMaster(){
        ArrayList<Neuron> neurons=new ArrayList<>();
        for(int i=0;i<master.size();i++)
            if(master.get(i)instanceof Neuron)
                neurons.add((Neuron)master.get(i));
        return neurons;
    }
    
    // gets all the connections from the master list
    private ArrayList<Connection> getConnectionsFromMaster(){
        ArrayList<Connection> connections=new ArrayList<>();
        for(int i=0;i<master.size();i++)
            if(master.get(i)instanceof Connection)
                connections.add((Connection)master.get(i));
        return connections;
    }
    
    // getter methods
    public ArrayList<Node> getMaster(){return master;}
    
    // setter methods
    public void setMaster(ArrayList<Node> param){master=param;}
}