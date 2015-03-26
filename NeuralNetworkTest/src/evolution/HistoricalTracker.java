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
    private ArrayList<Node> addedNodes; // contains new genes added this generation (need to implement this)
    private ArrayList<Integer> connectionNums;
    private ArrayList<Integer> neuronNums;
    private ArrayList<Integer> inputNums; // innovation numbers of the inputs
    private ArrayList<Integer> outputNums; // innovation numbers of the outputs
    private int nextInnovation; // the next node identifier
    private int nextSpecies; // the next species identifier
    
    public HistoricalTracker(){ // default constructor
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
        addedNodes.clear();
    }
    
    public boolean notSet(){
        return nextInnovation==0;
    }
    
    // TODO :: FIX THE INNEFFICIENTCIES HERE
    
    public void defineNeuron(Neuron added){
        for(int i=0;i<addedNodes.size();i++){
            if(addedNodes.get(i).getInitIn()==added.getInitIn()){
                if(addedNodes.get(i).getInitOut()==added.getInitOut()){
                    if(addedNodes.get(i)instanceof Neuron){
                        added.setInnovationNum(addedNodes.get(i).getInnovationNum());
                        return;
                    }
                }
            }
        }
        addedNodes.add(added);
        neuronNums.add(nextInnovation);
        added.setInnovationNum(nextInnovationNum());
        if(added.getInnovationNum()<0)
            System.out.println("Error with the innovation number in tracker :: HistoricalTracker");
    }
    
    public void defineConnection(Connection added){
        for(int i=0;i<addedNodes.size();i++){
            if(addedNodes.get(i).getInitIn()==added.getInitIn()){
                if(addedNodes.get(i).getInitOut()==added.getInitOut()){
                    if(addedNodes.get(i)instanceof Connection){
                        added.setInnovationNum(addedNodes.get(i).getInnovationNum());
                        return;
                    }
                }
            }
        }
        addedNodes.add(added);
        connectionNums.add(nextInnovation);
        added.setInnovationNum(nextInnovationNum());
    }

    // getter methods
    public ArrayList<Node> getAddedNodes(){return addedNodes;}
    public ArrayList<Integer> getConnectionNums(){return connectionNums;}
    public ArrayList<Integer> getNeuronNums(){return neuronNums;}
    public ArrayList<Integer> getInputNums(){return inputNums;}
    public ArrayList<Integer> getOutputNums(){return outputNums;}
    
    // setter methods
    public void setNextInnovation(int param){nextInnovation=param;}
    public void setAddedNodes(ArrayList<Node> param){addedNodes=param;}
    public void setConnectionNums(ArrayList<Integer> param){connectionNums=param;}
    public void setNeuronNums(ArrayList<Integer> param){neuronNums=param;}
    public void setInputNums(ArrayList<Integer> param){inputNums=param;}
    public void setOutputNums(ArrayList<Integer> param){outputNums=param;}
}