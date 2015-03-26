package evolution;
import networks.NeuralNetwork;
import datastructures.RandomNumberGenerator;
import experiments.XORTest;
import experiments.TestCases;
import testtools.CMDTester;
import evolution.species.Species;
import java.util.ArrayList;
import java.util.Random;
public class EvolutionaryAlgorithm {
    private HistoricalTracker history; // controls the origins of genes
    private RandomNumberGenerator rng; // creates forms of random numbers
    private ArrayList<Species> species; // the current species
    private ArrayList<NeuralNetwork> networks; // the current networks (will be removed soon)
    private ArrayList<NeuralNetwork> bests; // the list of best networks
    private TestCases tests; // the tests
    private int numberOfGenerations; // number of generations (should be global)
    private int populationSize; // population size (should be global)
    
    public EvolutionaryAlgorithm(){ // default constructor
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
            System.out.println("HUMINAHUMINA");
            runXORExperiment();
        }
    }
    
    // a unit test for the initializeFirstGeneration method
    private void initializeFirstGenerationTest(){
        species.add(new Species(history));
        species.get(0).setMaxAllowed(populationSize);
        species.get(0).initFromStart();
    }
    
    // initializes the first generation of individuals
    private void initializeFirstGeneration(){
        for(int i=0;i<populationSize;i++)
            networks.add(new NeuralNetwork(null));
    }
    
    // unit test for run XORExperiment method
    // this is currently an attempt to implement speciation
    private void runXORExperimentTest(){
        NeuralNetwork totalBest=new NeuralNetwork(history); // best neural network
        XORTest test=tests.getXORTest(); // contains the test cases
        for(int i=0;i<numberOfGenerations;i++){ // for each generation
            System.out.println("Starting generation :: "+i);
            // Create temporary lists to hold any new species and deviated nets
            ArrayList<NeuralNetwork> deviated=new ArrayList<>();
            ArrayList<Species> newSpecies=new ArrayList<>();
            // for each species
            for(int f=0;f<species.size();f++){
                // run the tests to get each individuals fitness
                for(int g=0;g<species.get(f).getIndividuals().size();g++){
                    tests.runXORTests(species.get(f).getIndividuals().get(g));
                    if(species.get(f).getIndividuals().get(g).getFitness()==4.0){
                        System.out.println("A solution was found at Generation :: "+i+" with a total number of species :: "+species.size());
                        new CMDTester(species.get(f).getIndividuals().get(g));
                    }
                }
                // gets the list of individuals who no longer belong to this species
                ArrayList<NeuralNetwork> temp=species.get(f).checkDeviation();
                // add those individuals to the deviated list
                for(int g=0;g<temp.size();g++)
                    deviated.add(temp.get(g));
                // increments the species age
                species.get(f).setAge(species.get(f).getAge()+1);
            }
            // for all of the deviated individuals
            for(int f=0;f<deviated.size();f++){
                boolean foundFit=false;
                // check if the individual belongs in a current species
                for(int g=0;g<species.size()&&!foundFit;g++)
                    if(species.get(g).belongs(deviated.get(f)))
                        foundFit=true;
                // check if the individual belongs in a newly added species
                for(int g=0;g<newSpecies.size()&&!foundFit;g++)
                    if(newSpecies.get(g).belongs(deviated.get(f)))
                        foundFit=true;
                // if not make a new species
                if(!foundFit){
                    Species add=new Species(history,deviated.get(f));
                    add.setSpeciesNum(history.nextSpecies());
                    newSpecies.add(add);
                }
            }
            // add all the new species to the list of all species
            for(int f=0;f<newSpecies.size();f++)
                species.add(newSpecies.get(f));
            double sum=0.0;
            int chk=0;
            // gets the sum of the average fitness of all species
            for(int f=0;f<species.size();f++){
                species.get(f).calculateAverageFitness();
                sum+=species.get(f).getAverageFitness();
            }
            // sets the allowed population size to a proportion of its average fitness to the sum
            for(int f=0;f<species.size();f++){
                if(species.size()==0){
                    species.get(f).setMaxAllowed(500);
                }else{
                    double proportion=species.get(f).getAverageFitness()/sum;
                    species.get(f).setMaxAllowed((int)(proportion*populationSize)); // species
                    if(species.get(f).getMaxAllowed()==0)
                        species.get(f).setMaxAllowed(1);
                }
                chk+=species.get(f).getIndividuals().size(); // why did I put this here?
            }
            // WHAT WAS I THINKING?
            while(chk<populationSize){
                species.get(0).setMaxAllowed(species.get(0).getMaxAllowed()+1);
                chk++;
            }
            // mutates each species
            if(i!=numberOfGenerations-1)
                for(int f=0;f<species.size();f++){
                    species.get(f).mutate();
                }
            // resets the changes in the history tracker
            history.endGeneration();
            if(i!=numberOfGenerations-1)
                for(int f=0;f<species.size();f++)
                    species.get(f).reset();
            else
                System.out.println("THIS WAS RAN");
            int totalInd=0;
            for(int g=0;g<species.size();g++){
                totalInd+=species.get(g).getIndividuals().size();
            }
            // Final test (need to implement functionality)
            System.out.println("Total Individuals :: "+totalInd);
        }
        // Debugging code
        species=Species.sort(species);
        System.out.println("Total Species :: "+species.size());
        System.out.println("Best Species Average Fitness :: "+species.get(0).getAverageFitness());
        System.out.println("Worst Species Average Fitness :: "+species.get(species.size()-1).getAverageFitness());
        int totalInd=0;
        for(int i=0;i<species.size();i++){
            totalInd+=species.get(i).getIndividuals().size();
        }
        // Final test (need to implement functionality)
        System.out.println("Total Individuals :: "+totalInd);
        new CMDTester(species.get(0));
    }
    
    // the "main" loop.
    private void runXORExperiment(){
        System.out.println("Starting XOR Test\n\n");
        NeuralNetwork totalBest=new NeuralNetwork(null);
        XORTest test=tests.getXORTest();
        int f;
        boolean chk=true;
        for(int i=0;i<numberOfGenerations&&chk;i++){
            ArrayList<NeuralNetwork> newList=new ArrayList<>();
            for(f=0;f<networks.size();f++)
                tests.runXORTests(networks.get(f));
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
            }
            if(networks.size()!=newList.size())
                System.out.println("Error :: Size Mismatch :: EvolutionaryAlgorithm");
            networks=newList;
            System.out.println("Generation "+i+" Over :: Best "+bests.get(bests.size()-1).getFitness());
        }
        new CMDTester(totalBest); // command line debugger
    }
    
    // MergeSort
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
    
    // merge for the merge sort
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