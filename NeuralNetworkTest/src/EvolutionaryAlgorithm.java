import java.util.ArrayList;
public class EvolutionaryAlgorithm {
    private ArrayList<NeuralNetwork> networks;
    private ArrayList<NeuralNetwork> bests;
    private TestCases tests;
    private DataRecorder recorder;
    private int numberOfGenerations;
    private int populationSize;
    
    public EvolutionaryAlgorithm(){
        networks=new ArrayList<>();
        bests=new ArrayList<>();
        tests=new TestCases();
        recorder=new DataRecorder();
        numberOfGenerations=20;
        populationSize=20;
        initializeFirstGeneration();
        runXORExperiment();
    }
    
    // possibly change this to allow for different set-ups
    private void initializeFirstGeneration(){
        for(int i=0;i<populationSize;i++)
            networks.add(new NeuralNetwork());
    }
    
    private void runXORExperiment(){
        NeuralNetwork totalBest=new NeuralNetwork();
        XORTest test=tests.getXORTest();
        int f;
        for(int i=0;i<numberOfGenerations;i++){
            ArrayList<NeuralNetwork> newList=new ArrayList<>();
            for(f=0;f<networks.size();f++){
                tests.runXORTests(networks.get(f));
            }
            networks=sortNetworksByFitness(networks);
            bests.add(networks.get(i));
            if(networks.get(i).getFitness()>totalBest.getFitness())
                totalBest=networks.get(i);
            if(networks.get(i).getFitness()==test.getSolutionFitness())
                System.out.println("Solution Found :: Generation "+i);
            for(f=0;f<networks.size()/2;f++){
                networks.get(f).mutate();
                newList.add(networks.get(f).copyAndMutate());
                newList.add(networks.get(f).copyAndMutate());
                // Add in crossover functionality
            }
            if(networks.size()!=newList.size())
                System.out.println("Error :: Size Mismatch :: EvolutionaryAlgorithm");
            networks=newList;
            // continue implementation
        }
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
                if(one.get(0).size()>two.get(0).size())
                    merged.add(two.remove(0));
                else
                    merged.add(one.remove(0));
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
    public DataRecorder getRecorder(){return recorder;}
    public int getNumberOfGenerations(){return numberOfGenerations;}
    public int getPopulationSize(){return populationSize;}
    
    // setter methods
    public void setNetworks(ArrayList<NeuralNetwork> param){networks=param;}
    public void setBests(ArrayList<NeuralNetwork> param){bests=param;}
    public void setTestCases(TestCases param){tests=param;}
    public void setRecorder(DataRecorder param){recorder=param;}
    public void setNumberOfGenerations(int param){numberOfGenerations=param;}
    public void setPopulationSize(int param){populationSize=param;}
}