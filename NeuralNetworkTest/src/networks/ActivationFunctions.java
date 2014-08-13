package networks;


import nodes.neurons.Neuron;

public class ActivationFunctions {
    public static int stepFunction(Neuron neuron){
        return Math.max(-1,Math.min((int)neuron.evaluate(),1));
    }
}