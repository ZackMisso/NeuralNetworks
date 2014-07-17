import java.util.ArrayList;
public class TestCases{
    private ArrayList<Test> tests;
    private XORTest xorTest;
    private ANDTest andTest;
    
    public TestCases(){
        tests=new ArrayList<>();
        xorTest=new XORTest();
        andTest=new ANDTest();
    }
    
    public void runXORTests(NeuralNetwork net){
        ArrayList<Double> outs=new ArrayList<>();
        for(int i=0;i<xorTest.getTests().size();i++){
            outs=net.run(xorTest.getTests().get(i));
            if(xorTest.checkTest(outs,i)){
                net.setFitness(net.getFitness()+1.0);
                System.out.println("XOR Test "+i+" Passed");
            }
            else{
                System.out.println("AND Test "+i+" Failed");
            }
        }
    }
    
    // getter methods
    public ArrayList<Test> getTests(){return tests;}
    public XORTest getXORTest(){return xorTest;}
    public ANDTest getANDTest(){return andTest;}
    
    // setter methods
    public void setTests(ArrayList<Test> param){tests=param;}
    public void setXORTest(XORTest param){xorTest=param;}
    public void setANDTest(ANDTest param){andTest=param;}
}