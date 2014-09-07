package evolution;
import networks.NeuralNetwork;
import datastructures.RandomNumberGenerator;
import experiments.XORTest;
import experiments.TestCases;
import experiments.CMDTester;
import evolution.species.Species;
import java.util.ArrayList;
import java.util.Random;
public class EvolutionaryAlgorithm {
    // TODO :: MAKE A GLOBAL RNG
    private HistoricalTracker history;
    private RandomNumberGenerator rng;
    private ArrayList<Species> species;
    private ArrayList<NeuralNetwork> networks;
    private ArrayList<NeuralNetwork> bests;
    private TestCases tests;
    private int numberOfGenerations;
    private int populationSize;
    
    public EvolutionaryAlgorithm(){
        history=new HistoricalTracker();
        rng=new RandomNumberGenerator();
        species=new ArrayList<>();
        networks=new ArrayList<>();
        bests=new ArrayList<>();
        tests=new TestCases();
        numberOfGenerations=600;
        populationSize=500;
        if(GlobalConstants.TEST){
            initializeFirstGenerationTest();
            runXORExperimentTest();
        }
        else{
            initializeFirstGeneration();
            runXORExperiment();
        }
    }
    
    private void initializeFirstGenerationTest(){
        species.add(new Species());
        species.get(0).setMaxAllowed(populationSize);
        species.get(0).initFromStart();
    }
    
    // possibly change this to allow for different set-ups
    private void initializeFirstGeneration(){
        for(int i=0;i<populationSize;i++)
            networks.add(new NeuralNetwork());
    }
    
    private void runXORExperimentTest(){
        NeuralNetwork totalBest=new NeuralNetwork();
        XORTest test=tests.getXORTest();
        for(int i=0;i<numberOfGenerations;i++){
            ArrayList<NeuralNetwork> deviated=new ArrayList<>();
            ArrayList<Species> newSpecies=new ArrayList<>();
            for(int f=0;f<species.size();f++){
                for(int g=0;g<species.get(f).getIndividuals().size();g++){ // HEAVIEST LOOP For Simulation
                    tests.runXORTests(species.get(f).getIndividuals().get(g));
                }
                ArrayList<NeuralNetwork> temp=species.get(f).checkDeviation();
                for(int g=0;g<temp.size();g++)
                    deviated.add(temp.get(g));
                species.get(f).setAge(species.get(f).getAge()+1);
            }
            // Can make this seperate function
            for(int f=0;f<deviated.size();f++){
                boolean foundFit=false;
                for(int g=0;g<species.size()&&!foundFit;g++){
                    if(species.get(g).belongs(deviated.get(f))){
                        foundFit=true;
                    }
                }
                for(int g=0;g<newSpecies.size()&&!foundFit;g++){
                    if(newSpecies.get(g).belongs(deviated.get(f))){
                        foundFit=true;
                    }
                }
                if(!foundFit){
                    Species add=new Species(deviated.get(f));
                    //add.setSpeciesNum();
                    newSpecies.add(add);
                }
            }
            for(int f=0;f<newSpecies.size();f++)
                species.add(newSpecies.get(f));
            double sum=0.0;
            int chk=0;
            for(int f=0;f<species.size();f++){
                species.get(f).calculateAverageFitness();
                sum+=species.get(f).getAverageFitness();
            }
            for(int f=0;f<species.size();f++){
                double proportion=species.get(f).getAverageFitness()/sum;
                species.get(f).setMaxAllowed((int)(proportion*populationSize));
                chk++;
            }
            if(chk>populationSize)
                System.out.println("OVERPOPULATION!!!");
            while(chk<populationSize){
                species.get(0).setMaxAllowed(species.get(0).getMaxAllowed()+1);
                chk++;
            }
            for(int f=0;f<species.size();f++){
                species.get(f).mutate();
            }
        }
    }
    
    private void runXORExperiment(){
        System.out.println("Starting XOR Test\n\n");
        NeuralNetwork totalBest=new NeuralNetwork();
        XORTest test=tests.getXORTest();
        int f;
        boolean chk=true;
        for(int i=0;i<numberOfGenerations&&chk;i++){
            ArrayList<NeuralNetwork> newList=new ArrayList<>();
            for(f=0;f<networks.size();f++){
                //System.out.println("DEBUG 3");
                tests.runXORTests(networks.get(f));
                //System.out.println("DEBUG 2");
            }
            //System.out.println("DEBUG 1");
            networks=sortNetworksByFitness(networks);
            bests.add(networks.get(0));
            if(networks.get(0).getFitness()==4.0){
                chk=false;
                totalBest=networks.get(0);
            }
            if(networks.get(0).getFitness()>totalBest.getFitness())
                totalBest=networks.get(0);
            if(networks.get(0).getFitness()==test.getSolutionFitness())
                System.out.println("Solution Found :: Generation "+i);
            for(f=0;f<networks.size()/2;f++){
                networks.get(f).mutate();
                newList.add(networks.get(f).copy());
                newList.add(networks.get(f).copy());
                //System.out.println(networks.get(f).getNeurons().size());
                // Add in crossover functionality
            }
            //for(f=0;f<networks.size();f++){
            //    networks.get(f).mutate();
            //    System.out.println(networks.get(f).getNeurons().size());
            //    newList.add(networks.get(f));
            //}
            if(networks.size()!=newList.size())
                System.out.println("Error :: Size Mismatch :: EvolutionaryAlgorithm");
            networks=newList;
            System.out.println("Generation "+i+" Over :: Best "+bests.get(bests.size()-1).getFitness());
            // continue implementation
        }
        new CMDTester(totalBest);
        //System.out.println(totalBest);
    }
    
    // this is going to be Merge Sort, possibly implement others later
    private ArrayList<NeuralNetwork> sortNetworksByFitness(ArrayList<NeuralNetwork> nets){
        if(nets.size()==1)
            return nets;
        ArrayList<NeuralNetwork> one=new ArrayList<>();
        ArrayList<NeuralNetwork> two=new ArrayList<>();
        int i=0;
        for(;i<nets.size()/2;i++)
            one.add(nets.get(i));
        for(;i<nets.size();i++)
            two.add(nets.get(i));
        one=sortNetworksByFitness(one);
        two=sortNetworksByFitness(two);
        return merge(one,two);
    }
    
    // merge for te merge sort
    private ArrayList<NeuralNetwork> merge(ArrayList<NeuralNetwork> one,ArrayList<NeuralNetwork> two){
        ArrayList<NeuralNetwork> merged=new ArrayList<>();
        while(!one.isEmpty()&&!two.isEmpty()){
            if(one.get(0).getFitness()>two.get(0).getFitness())
                merged.add(one.remove(0));
            else if(one.get(0).getFitness()<two.get(0).getFitness())
                merged.add(two.remove(0));
            else{
                double random=(new Random()).nextDouble();
                if(random>.5)
                    merged.add(one.remove(0));
                else
                    merged.add(two.remove(0));
                //if(one.get(0).size()>two.get(0).size())
                    //merged.add(one.remove(0));
                //else
                    //merged.add(two.remove(0));
            }
        }
        while(!one.isEmpty())
            merged.add(one.remove(0));
        while(!two.isEmpty())
            merged.add(two.remove(0));
        return merged;
    }
    
    // getter methods
    public ArrayList<NeuralNetwork> getNetworks(){return networks;}
    public ArrayList<NeuralNetwork> getBests(){return bests;}
    public TestCases getTests(){return tests;}
    public int getNumberOfGenerations(){return numberOfGenerations;}
    public int getPopulationSize(){return populationSize;}
    
    // setter methods
    public void setNetworks(ArrayList<NeuralNetwork> param){networks=param;}
    public void setBests(ArrayList<NeuralNetwork> param){bests=param;}
    public void setTestCases(TestCases param){tests=param;}
    public void setNumberOfGenerations(int param){numberOfGenerations=param;}
    public void setPopulationSize(int param){populationSize=param;}
}