/**
 *
 * @author Zackary Misso
 * 
 */
package evolution.species;
import networks.NeuralNetwork;
import evolution.SpeciationFunctions;
import evolution.HistoricalTracker;
import java.util.ArrayList;
import java.util.Random;
import nodes.neurons.OutputNeuron;
public class Species{
    private ArrayList<NeuralNetwork> nets;
    private HistoricalTracker tracker;
    private double averageFitness;
    private int maxAllowed;
    private int speciesNum;
    private int age;
    
    public Species(HistoricalTracker param){
        nets=new ArrayList<>();
        tracker=param;
        averageFitness=0.0;
        maxAllowed=0;
        age=0;
    }
    
    public Species(HistoricalTracker param,NeuralNetwork base){
        this(param);
        nets.add(base);
    }
    
    public void mutate(){ // TODO :: Pass in RNG
        // sort the individuals in the species based on fitness
        nets=NeuralNetwork.sort(nets);
        // remove an individual if there are too many
        if(maxAllowed<=0)
            System.out.println(maxAllowed+" :: is zero or less. Size :: "+nets.size());
        while(nets.size()<maxAllowed)
            nets.add(nets.get(0).copyAndMutate());
        while(nets.size()>maxAllowed&&nets.size()>0){
            if(nets.size()<maxAllowed)
                System.out.println("WHYYYYYY :: Species");
            nets.remove(nets.size()-1);
        }        
        // mutate every one
        Random random=new Random();
        for(int i=0;i<nets.size();i++){
            ArrayList<OutputNeuron> outputs=nets.get(i).findOutputs();
            for(int f=0;f<outputs.size();f++)
                outputs.get(f).findDepth();
            double mORc=random.nextDouble();
            if(mORc>.8) // possibly make this global
                crossover(nets.get(i),random);
            else
                nets.set(i,nets.get(i).copyAndMutate());
        }
    }
    
    public void crossover(NeuralNetwork net,Random random){
        NeuralNetwork other;
        // find the other individual
        do{
            other=nets.get(random.nextInt(nets.size()));
            if(nets.size()==1){
                net=net.copyAndMutate();
                return;
            }
        }while(other!=net);
        ArrayList<OutputNeuron> outputs=other.findOutputs();
            for(int i=0;i<outputs.size();i++)
                outputs.get(i).findDepth();
        // calls the neural network's crossover function
        if(other!=null)
            net=net.crossOver(other);
    }
    
    public void initFromStart(){
        System.out.println("MAX ALLOWED :: "+maxAllowed);
        for(int i=0;i<maxAllowed;i++)
            nets.add(new NeuralNetwork(tracker));
        System.out.println("Begin Size "+nets.size());
    }
    
    public void calculateAverageFitness(){
        double avg=0.0;
        for(int i=0;i<nets.size();i++)
            avg+=nets.get(i).getFitness();
        averageFitness=avg/nets.size();
    }
    
    public void calculateAverageFitnessDescriptive(){
        double avg=0.0;
        for(int i=0;i<nets.size();i++)
            avg+=nets.get(i).getFitness();
        System.out.println(avg+" :: "+nets.size());
    }
    
    public boolean shouldDie(){
        return age>10&&nets.size()<=5;
    }
    
    public boolean belongs(NeuralNetwork net){
        if(nets.size()==0)
            return false;
        if(SpeciationFunctions.sameSpecies(net,nets.get(0))){
            nets.add(net);
            return true;
        }
        return false;
    }
    
    public void reset(){
        for(int i=0;i<nets.size();i++)
            nets.get(i).setFitness(0);
    }
    
    public ArrayList<NeuralNetwork> checkDeviation(){
        ArrayList<NeuralNetwork> deviated=new ArrayList<>();
        if(nets.size()==0){
            System.out.println("ERROR THERE WAS AN ERROR :: The individual list is empty");
            return deviated;
        }
        NeuralNetwork base=nets.get(0);
        for(int i=1;i<nets.size();i++)
            if(SpeciationFunctions.findNetworkDistance(base,nets.get(i))>SpeciationFunctions.THRESHOLD)
                deviated.add(nets.remove(i));
        return deviated;
    }
    
    public static ArrayList<Species> sort(ArrayList<Species> list){
        if(list.size()==1)
            return list;
        ArrayList<Species> one=new ArrayList<>();
        ArrayList<Species> two=new ArrayList<>();
        int i=0;
        for(;i<list.size()/2;i++)
            one.add(list.get(i));
        for(;i<list.size();i++)
            two.add(list.get(i));
        one=sort(one);
        two=sort(two);
        return merge(one,two);
    }
    
    public static ArrayList<Species> merge(ArrayList<Species> one,ArrayList<Species> two){
        ArrayList<Species> merged=new ArrayList<>();
        while(!one.isEmpty()&&!two.isEmpty()){
            if(one.get(0).getAverageFitness()>two.get(0).getAverageFitness())
                merged.add(one.remove(0));
            else
                merged.add(two.remove(0));
        }
        while(!one.isEmpty())
            merged.add(one.remove(0));
        while(!two.isEmpty())
            merged.add(two.remove(0));
        return merged;
    }
    
    // getter methods
    public ArrayList<NeuralNetwork> getIndividuals(){return nets;}
    public double getAverageFitness(){return averageFitness;}
    public int getSpeciesNum(){return speciesNum;}
    public int getMaxAllowed(){return maxAllowed;}
    public int getAge(){return age;}
    
    // setter methods
    public void setMaxAllowed(int param){maxAllowed=param;}
    public void setSpeciesNum(int param){speciesNum=param;}
    public void setAge(int param){age=param;}
}