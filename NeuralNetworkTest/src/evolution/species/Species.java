/**
 *
 * @author Zackary Misso
 * 
 */
package evolution.species;
import networks.NeuralNetwork;
import evolution.SpeciationFunctions;
import java.util.ArrayList;
import java.util.Random;
public class Species{
    private ArrayList<NeuralNetwork> nets;
    private double averageFitness;
    private int maxAllowed;
    private int speciesNum;
    private int age; // in generations
    
    public Species(){
        nets=new ArrayList<>();
        averageFitness=0.0;
        maxAllowed=0;
        age=0;
    }
    
    public Species(NeuralNetwork base){
        this();
        nets.add(base);
    }
    
    public void mutate(){
        nets=NeuralNetwork.sort(nets);
        while(nets.size()>maxAllowed)
            nets.remove(nets.size()-1);
        while(nets.size()<maxAllowed)
            nets.add(nets.get(0).copyAndMutate());
        Random random=new Random();
        for(int i=0;i<nets.size();i++){
            double mORc=random.nextDouble();
            if(mORc>.5)
                crossover(nets.get(i),random);
            else
                nets.get(i).copyAndMutate();
        }
    }
    
    public void crossover(NeuralNetwork net,Random random){
        NeuralNetwork other;
        do{
            other=nets.get(random.nextInt(nets.size()));
        }while(other!=net);
        net=net.crossOver(other);
    }
    
    public void initFromStart(){
        for(int i=0;i<maxAllowed;i++)
            nets.add(new NeuralNetwork());
    }
    
    public void calculateAverageFitness(){
        double avg=0.0;
        for(int i=0;i<nets.size();i++)
            avg+=nets.get(i).getFitness();
        averageFitness=avg;
    }
    
    public boolean shouldDie(){
        return nets.size()==1;
    }
    
    public boolean belongs(NeuralNetwork net){
        if(SpeciationFunctions.findNetworkDistance(net,nets.get(0))<SpeciationFunctions.THRESHOLD){
            nets.add(net);
            return true;
        }
        return false;
    }
    
    public ArrayList<NeuralNetwork> checkDeviation(){
        ArrayList<NeuralNetwork> deviated=new ArrayList<>();
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
            if(one.get(0).getAverageFitness()<two.get(0).getAverageFitness())
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