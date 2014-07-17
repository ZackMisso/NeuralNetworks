import java.util.ArrayList;
import java.util.Random;
public class NeuralNetwork {
    private ArrayList<Neuron> neurons;
    private ArrayList<Integer> inputs; // maybe use this
    private ArrayList<Integer> outputs; // maybe use this
    private double fitness;
    
    public NeuralNetwork(){
        this(3,1);
    }
    
    public NeuralNetwork(int ins,int outs){
        neurons=new ArrayList<>();
        inputs=new ArrayList<>();
        outputs=new ArrayList<>();
        fitness=0.0;
        initializeNetwork(ins,outs);
    }
    
    private void initializeNetwork(int insnum,int outsnum){
        for(int i=0;i<insnum;i++){
            InputNeuron_Add neuron=new InputNeuron_Add();
            neuron.setInputID(i);
           neurons.add(neuron);
        }
        for(int i=0;i<outsnum;i++){
            OutputNeuron_Add neuron=new OutputNeuron_Add();
            neuron.setOutputID(i);
            neurons.add(neuron);
        }
        ArrayList<InputNeuron> ins=findInputs();
        ArrayList<OutputNeuron> outs=findOutputs();
        for(int i=0;i<ins.size();i++)
            for(int f=0;f<outs.size();f++)
                makeConnection(ins.get(i),outs.get(f));
        // possibly add logic for initializing hidden layers in the future
    }
    
    public int size(){
        int totalConnections=0;
        for(int i=0;i<neurons.size();i++)
            totalConnections+=neurons.get(i).getNumberOfConnections();
        totalConnections/=2;
        return totalConnections+neurons.size()*2;
    }
    
    // controls the mutation of the neural network
    public void mutate(){
        Random random=new Random();
        double chance=random.nextDouble();
        if(chance>.7){
            mutateTopography(random);
        }else{
            mutateWeights(random);
        }
    }
    
    // mutates the weights of the neural network
    private void mutateWeights(Random random){
        int neuronNum=random.nextInt(neurons.size());
        Neuron neuron=neurons.get(neuronNum);
        if(neuron instanceof InputNeuron){
            // idk if input neurons need weights
        }else{
            ArrayList<Double> weights=neuron.getWeights();
            double isNeg=random.nextDouble();
            double change=random.nextDouble();
            int weightsNum=random.nextInt(weights.size());
            if(isNeg>.5)
                change*=-1;
            weights.set(weightsNum,weights.get(weightsNum)+change);
            // implement more if needed
        }
    }
    
    // mutates the topography of the neural network
    private void mutateTopography(Random random){
        int neuronNum=random.nextInt(neurons.size());
        Neuron neuron=neurons.get(neuronNum);
        double makeRandomConnection=random.nextDouble();
        double newNeuron=random.nextDouble();
        if(makeRandomConnection>.8){
            newRandomConnection(neuron);
        }
        else if(neuron instanceof InputNeuron){
            ArrayList<Connection> outputs=neuron.getOutputs();
            if(newNeuron>.5){
                addNeuron(outputs,neuron,random);
            }else{
                turnOffConnection(outputs,neuron,random);
            }
        }
        else if(neuron instanceof OutputNeuron){
            ArrayList<Connection> inputs=neuron.getInputs();
            if(newNeuron>.5){
                addNeuron(inputs,neuron,random);
            }else{
                turnOffConnection(inputs,neuron,random);
            }
        }else{ // hidden neurons
            ArrayList<Connection> inputs=neuron.getInputs();
            ArrayList<Connection> outputs=neuron.getOutputs();
            double inorout=random.nextDouble();
            if(inorout>.5){
                if(newNeuron>.5){
                    addNeuron(inputs,neuron,random);
                }else{
                    turnOffConnection(inputs,neuron,random);
                }
            }else{
                if(newNeuron>.5){
                    addNeuron(outputs,neuron,random);
                }else{
                    turnOffConnection(outputs,neuron,random);
                }
            }
        }
    }
    
    // THIS METHOD IS VERY INEFFICIENT FIX IT LATER
    private void newRandomConnection(Neuron neuron){
        ArrayList<Neuron> connected=new ArrayList<>();
        for(int i=0;i<neuron.getInputs().size();i++)
            connected.add(neuron.getInputs().get(i).getGiveNeuron());
        for(int i=0;i<neuron.getOutputs().size();i++)
            connected.add(neuron.getOutputs().get(i).getRecieveNeuron());
        connected.add(neuron);
        ArrayList<Integer> indexes=new ArrayList<>();
        for(int i=0;i<neurons.size();i++)
            if(!connected.contains(neurons.get(i)))
                indexes.add(i);
        if(connected.size()==neurons.size()-1)
            return;
        Random random=new Random();
        int chosenNeuron=random.nextInt(indexes.size());
        double recogiv=random.nextDouble();
        int otherIndex=indexes.get(chosenNeuron);
        Neuron otherNeuron=neurons.get(otherIndex);
        if(otherNeuron==null)
            return;
        if(otherNeuron instanceof InputNeuron&&neuron instanceof InputNeuron){
            mutate();
        }
        else if(otherNeuron instanceof OutputNeuron&&neuron instanceof OutputNeuron){
            mutate();
        }
        else if(otherNeuron instanceof InputNeuron&&neuron instanceof OutputNeuron){
            makeConnection(otherNeuron,neuron);
        }
        else if(otherNeuron instanceof OutputNeuron&&neuron instanceof InputNeuron){
            makeConnection(neuron,otherNeuron);
        }
        else if(otherNeuron instanceof InputNeuron){
            makeConnection(otherNeuron,neuron);
        }
        else if(otherNeuron instanceof OutputNeuron){
            makeConnection(neuron,otherNeuron);
        }
        else if(neuron instanceof InputNeuron){
            makeConnection(neuron,otherNeuron);
        }
        else if(neuron instanceof OutputNeuron){
            makeConnection(otherNeuron,neuron);
        }
        else{ // connection between two hidden neurons
            if(recogiv>.5){
                makeConnection(neuron,otherNeuron);
            }else{
                makeConnection(otherNeuron,neuron);
            }
        }
    }

    // creates a new connection between two neurons
    private void makeConnection(Neuron give,Neuron recieve){
        Connection connection=new Connection();
        connection.setEvaluated(false);
        connection.setActive(true);
        connection.setGiveNeuron(give);
        connection.setRecieveNeuron(recieve);
        give.getOutputs().add(connection);
        recieve.getInputs().add(connection);
    }
    
    // adds a neuron in the middle of a connection
    private void addNeuron(ArrayList<Connection> connects,Neuron neuron,Random random){
        if(connects.isEmpty())
            mutate();
        int connectionNum=random.nextInt(connects.size());
        Connection connection=connects.get(connectionNum);
        Neuron otherNeuron=null;
        if(connection.getGiveNeuron()==neuron)
            otherNeuron=connection.getRecieveNeuron();
        else
            otherNeuron=connection.getGiveNeuron();
        Neuron newNeuron=new Neuron_Add();
        if(connection.getGiveNeuron()==neuron){
            makeConnection(neuron,newNeuron);
            makeConnection(newNeuron,otherNeuron);
        }else{
            makeConnection(otherNeuron,newNeuron);
            makeConnection(newNeuron,neuron);
        }
        neurons.add(newNeuron);
    }
    
    // turns off a connection
    private void turnOffConnection(ArrayList<Connection> connects,Neuron neuron,Random random){
        if(connects.isEmpty())
            mutate();
        int connectionNum=random.nextInt(connects.size());
        Connection connection=connects.get(connectionNum);
        Neuron other=null;
        if(connection.getGiveNeuron()==neuron)
            other=connection.getRecieveNeuron();
        else
            other=connection.getGiveNeuron();
        other.removeConnectionWith(neuron);
        connects.remove(connection);
    }
    
    public void crossOver(NeuralNetwork other){
        // implement
    }
    
    public ArrayList<Double> run(Test param){
        ArrayList<OutputNeuron> outputs=findOutputs();
        ArrayList<Double> results=new ArrayList<>();
        outputs=sortOutputs(outputs);
        for(int i=0;i<outputs.size();i++){
            outputs.get(i).evaluate();
            results.add(outputs.get(i).getOutput());
        }
        nextGeneration();
        return results;
    }
    
    public NeuralNetwork copyAndMutate(){
        // implement
        return null;
    }
    
    private void nextGeneration(){
        for(int i=0;i<neurons.size();i++)
            neurons.get(i).deEvaluate();
        fitness=0.0;
    }
    
    private ArrayList<InputNeuron> findInputs(){
        ArrayList<InputNeuron> ins=new ArrayList<>();
        for(int i=0;i<neurons.size();i++)
            if(neurons.get(i)instanceof InputNeuron)
                ins.add((InputNeuron)neurons.get(i));
        return ins;
    }
    
    private ArrayList<OutputNeuron> findOutputs(){
        ArrayList<OutputNeuron> outs=new ArrayList<>();
        for(int i=0;i<neurons.size();i++)
            if(neurons.get(i)instanceof OutputNeuron)
                outs.add((OutputNeuron)neurons.get(i));
        return outs;
    }
    
    // there will not be many outputs so this sort will be ineffitient
    private ArrayList<OutputNeuron> sortOutputs(ArrayList<OutputNeuron> outputs){
        ArrayList<OutputNeuron> list=new ArrayList<>();
        int cnt=0;
        while(!outputs.isEmpty()){
            for(int i=0;i<outputs.size();i++)
                if(outputs.get(i).getOutputID()==cnt)
                    list.add(outputs.remove(i--));
            cnt++;
        }
        return list;
    }
    
    public String toString(){
        // implement to write to file
        return "";
    }
    
    // getter methods
    public ArrayList<Neuron> getNeurons(){return neurons;}
    public ArrayList<Integer> getInputs(){return inputs;}
    public ArrayList<Integer> outputs(){return outputs;}
    public double getFitness(){return fitness;}
    
    // setter methods
    public void setNeurons(ArrayList<Neuron> param){neurons=param;}
    public void setInputs(ArrayList<Integer> param){inputs=param;}
    public void setOutputs(ArrayList<Integer> param){outputs=param;}
    public void setFitness(double param){fitness=param;}
}