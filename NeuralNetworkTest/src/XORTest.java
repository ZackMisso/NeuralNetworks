import java.util.ArrayList;
public class XORTest {
    private ArrayList<Test> tests;
    private double solutionFitness;
    
    public XORTest(){
        tests=new ArrayList<>();
        initializeTests();
    }
    
    private void initializeTests(){
        Test one=new Test();
        one.getInputs().add(1.0);
        one.getInputs().add(1.0);
        one.getOutputs().add(0.0);
        Test two=new Test();
        two.getInputs().add(1.0);
        two.getInputs().add(0.0);
        two.getOutputs().add(1.0);
        Test three=new Test();
        three.getInputs().add(0.0);
        three.getInputs().add(1.0);
        three.getOutputs().add(1.0);
        Test four=new Test();
        four.getInputs().add(0.0);
        four.getInputs().add(0.0);
        four.getOutputs().add(0.0);
        tests.add(one);
        tests.add(two);
        tests.add(three);
        tests.add(four);
        solutionFitness=4.0;
    }
    
    public boolean checkTest(ArrayList<Double> param,int index){
        return tests.get(index).matches(param);
    }
    
    // getter methods
    public ArrayList<Test> getTests(){return tests;}
    public double getSolutionFitness(){return solutionFitness;}
    
    // setter methods
    public void setTests(ArrayList<Test> param){tests=param;}
    public void setSolutionFitness(double param){solutionFitness=param;}
}
