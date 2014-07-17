import java.util.ArrayList;
public class XORTest {
    private ArrayList<Test> tests;
    private double solutionFitness;
    
    public XORTest(){
        tests=new ArrayList<>();
    }
    
    private void initializeTests(){
        // implement
    }
    
    public boolean checkTest(ArrayList<Double> param,int index){
        // implement
        return false;
    }
    
    // getter methods
    public ArrayList<Test> getTests(){return tests;}
    public double getSolutionFitness(){return solutionFitness;}
    
    // setter methods
    public void setTests(ArrayList<Test> param){tests=param;}
    public void setSolutionFitness(double param){solutionFitness=param;}
}
