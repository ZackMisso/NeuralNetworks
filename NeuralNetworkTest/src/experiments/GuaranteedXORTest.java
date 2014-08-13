/**
 *
 * @author Zackary Misso
 * 
 */
package experiments;
import networks.NeuralNetwork;
import nodes.connections.Connection;
import nodes.neurons.Neuron;
import nodes.neurons.Neuron_Add;
import nodes.neurons.InputNeuron_Add;
import nodes.neurons.OutputNeuron_Add;
import java.util.ArrayList;
public class GuaranteedXORTest {
    private NeuralNetwork net;
    
    public GuaranteedXORTest(){
        initializeNet();
        System.out.println(test());
    }
    
    private void initializeNet(){
        ArrayList<Neuron> neurons=new ArrayList<>();
        InputNeuron_Add one=new InputNeuron_Add();
        InputNeuron_Add two=new InputNeuron_Add();
        Neuron_Add three=new Neuron_Add();
        OutputNeuron_Add four=new OutputNeuron_Add();
        one.setNeuronID(0);
        two.setNeuronID(1);
        three.setNeuronID(2);
        four.setNeuronID(3);
        one.setBias(0.0);
        two.setBias(0.0);
        three.setBias(-20.0);
        four.setBias(0.0);
        Connection c1=new Connection();
        Connection c2=new Connection();
        Connection c3=new Connection();
        Connection c4=new Connection();
        Connection c5=new Connection();
        
        // implement
        neurons.add(one);
        neurons.add(two);
        neurons.add(three);
        neurons.add(four);
        net=new NeuralNetwork(neurons);
    }
    
    private String test(){
        // implement
        return "Has Not Been Iplemented Yet";
    }
    
    // getter methods
    public NeuralNetwork getNet(){return net;}
    
    // setter methods
    public void setNet(NeuralNetwork param){net=param;}
}
