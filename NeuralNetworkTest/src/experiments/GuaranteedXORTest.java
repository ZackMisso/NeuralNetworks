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
        ArrayList<Connection> connections=new ArrayList<>();
        InputNeuron_Add one=new InputNeuron_Add();
        InputNeuron_Add two=new InputNeuron_Add();
        Neuron_Add three=new Neuron_Add();
        OutputNeuron_Add four=new OutputNeuron_Add();
        one.setInnovationNum(0);
        two.setInnovationNum(1);
        three.setInnovationNum(2);
        four.setInnovationNum(3);
        one.setBias(0.0);
        two.setBias(0.0);
        three.setBias(-20.0);
        four.setBias(0.0);
        Connection c1=new Connection();
        Connection c2=new Connection();
        Connection c3=new Connection();
        Connection c4=new Connection();
        Connection c5=new Connection();
        c1.setWeight(5.0);
        c2.setWeight(11.0);
        c3.setWeight(11.0);
        c4.setWeight(5.0);
        c5.setWeight(-20.0);
        c1.setInnovationNum(4);
        c2.setInnovationNum(5);
        c3.setInnovationNum(6);
        c4.setInnovationNum(7);
        c5.setInnovationNum(8);
        c1.setGiveNeuron(one);
        c2.setGiveNeuron(one);
        c3.setGiveNeuron(two);
        c4.setGiveNeuron(two);
        c5.setGiveNeuron(three);
        c1.setRecieveNeuron(four);
        c2.setRecieveNeuron(three);
        c3.setRecieveNeuron(three);
        c4.setRecieveNeuron(four);
        c5.setRecieveNeuron(four);
        one.getOutputs().add(c1);
        one.getOutputs().add(c2);
        two.getOutputs().add(c3);
        two.getOutputs().add(c4);
        three.getOutputs().add(c5);
        four.getInputs().add(c1);
        three.getInputs().add(c2);
        three.getInputs().add(c3);
        four.getInputs().add(c4);
        four.getInputs().add(c5);
        neurons.add(one);
        neurons.add(two);
        neurons.add(three);
        neurons.add(four);
        connections.add(c1);
        connections.add(c2);
        connections.add(c3);
        connections.add(c4);
        connections.add(c5);
        net=new NeuralNetwork(neurons,connections);
    }
    
    private String test(){
        String data="The Fitness for the Guaranteed Test is :: ";
        TestCases xor=new TestCases();
        xor.runXORTests(net);
        return data+net.getFitness();
    }
    
    // getter methods
    public NeuralNetwork getNet(){return net;}
    
    // setter methods
    public void setNet(NeuralNetwork param){net=param;}
}