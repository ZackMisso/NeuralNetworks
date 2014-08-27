/**
 *
 * @author Zackary Misso
 *
 */
package evolution;
import networks.NeuralNetwork;
import nodes.Node;
import nodes.neurons.Neuron;
import nodes.connections.Connection;
import java.util.ArrayList;
public class SpeciationFunctions {
	public static final double THRESHOLD=10.0; // change as needed
	public static final double weightConstant=.4; // change as needed

	public static boolean sameSpecies(NeuralNetwork one,NeuralNetwork two){
		return findNetworkDistance(one,two)<THRESHOLD;
	}

    public static double findNetworkDistance(NeuralNetwork one,NeuralNetwork two){
    	ArrayList<Node> oneNodes=Node.sort(one.getAllNodes());
    	ArrayList<Node> twoNodes=Node.sort(two.getAllNodes());
    	double distance=0.0;
    	int i=0;
    	int f=0;
    	int oneIn=oneNodes.get(i).getInnovationNum();
    	int twoIn=twoNodes.get(i).getInnovationNum();
    	while(i!=oneNodes.size()&&f!=twoNodes.size()){
    		if(oneIn==twoIn){
    			distance+=findNodeDistance(oneNodes.get(i),twoNodes.get(f));
    			oneIn=oneNodes.get(++i).getInnovationNum();
    			twoIn=twoNodes.get(++f).getInnovationNum();
    		}else if(oneIn>twoIn){
    			distance+=1.0;
    			twoIn=twoNodes.get(++f).getInnovationNum();
    		}else{
    			distance+=1.0;
    			oneIn=oneNodes.get(++i).getInnovationNum();
    		}
    	}
    	while(i!=oneNodes.size()){
    		distance+=1.0;
    		i++;
    	}
    	while(f!=twoNodes.size()){
    		distance+=1.0;
    		f++;
    	}
    	return distance;
    }

    // this is always going to be less than .4 (to be changed)
    public static double findNodeDistance(Node one,Node two){
    	if(one instanceof Neuron)
    		return findNeuronDistance((Neuron)one,(Neuron)two);
    	else
    		return findConnectionDistance((Connection)one,(Connection)two);
    }

    public static double findNeuronDistance(Neuron one,Neuron two){
    	double biasDistance=one.getBias()-two.getBias();
    	double maxDistance=GlobalConstants.MAX_WEIGHT_VALUE-GlobalConstants.MIN_WEIGHT_VALUE;
    	biasDistance=Math.abs(biasDistance);
    	maxDistance=Math.abs(maxDistance);
    	double proportion=biasDistance/maxDistance;
    	return proportion/weightConstant;
    }

    public static double findConnectionDistance(Connection one,Connection two){
    	double weightDistance=one.getWeight()-two.getWeight();
    	double maxDistance=GlobalConstants.MAX_WEIGHT_VALUE-GlobalConstants.MIN_WEIGHT_VALUE;
    	weightDistance=Math.abs(weightDistance);
    	maxDistance=Math.abs(maxDistance);
    	double proportion=weightDistance/maxDistance;
    	return proportion/weightConstant;
    }
}