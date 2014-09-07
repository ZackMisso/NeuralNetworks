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
    private ArrayList<Node> master; // contains all made nodes put together
    //private ArrayList<Node> addedNodes;
    private int nextInnovation;
    private int nextSpecies;
    
    public HistoricalTracker(){
        master=new ArrayList<>();
        //addedNodes=new ArrayList<>();
        nextInnovation=0;
        nextSpecies=0;
        initializeMaster();
    }
    
    private void initializeMaster(){
        // implement
    }
    
    // MAYBE DONT NEED
    public void endGeneration(){
        //addedNodes.clear();
    }
    
    public void beginGeneration(){
        Node.sort(master);
    }
    
    public void defineNode(Node added){
        if(added instanceof Connection)
            defineConnection((Connection)added);
        defineNeuron((Neuron)added);
    }
    
    // TODO :: FIX THE INNEFFICIENTCIES HERE
    
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
    
    private ArrayList<Neuron> getNeuronsFromMaster(){
        ArrayList<Neuron> neurons=new ArrayList<>();
        for(int i=0;i<master.size();i++)
            if(master.get(i)instanceof Neuron)
                neurons.add((Neuron)master.get(i));
        return neurons;
    }
    
    private ArrayList<Connection> getConnectionsFromMaster(){
        ArrayList<Connection> connections=new ArrayList<>();
        for(int i=0;i<master.size();i++)
            if(master.get(i)instanceof Connection)
                connections.add((Connection)master.get(i));
        return connections;
    }
    
    // getter methods
    public ArrayList<Node> getMaster(){return master;}
    //public ArrayList<Node> getAddedNodes(){return addedNodes;}
    public int getNextInnovation(){return nextInnovation;}
    public int getNextSpecies(){return nextSpecies;}
    
    // setter methods
    public void setMaster(ArrayList<Node> param){master=param;}
    //public void setAddedNodes(ArrayList<Node> param){addedNodes=param;}
    public void setNextInnovation(int param){nextInnovation=param;}
    public void setNextSpecies(int param){nextSpecies=param;}
}