package experiments;
import networks.NeuralNetwork;
import java.util.ArrayList;
public class TestCases{
    private ArrayList<Test> tests;
    private XORTest xorTest;
    private ANDTest andTest;
    //private boolean hack;
    
    public TestCases(){
        tests=new ArrayList<>();
        xorTest=new XORTest();
        andTest=new ANDTest();
        //hack=false;
    }
    
    public void runXORTests(NeuralNetwork net){
        ArrayList<Double> outs=new ArrayList<>();
        //System.out.println("DEBUG 4");
        for(int i=0;i<xorTest.getTests().size();i++){
            //System.out.println("DEBUG 6");
            //System.out.println("START");
            outs=net.run(xorTest.getTests().get(i));
            //System.out.println("STOP");
            //System.out.println("DEBUG 5");
            //System.out.println(outs);
            if(xorTest.checkTest(outs,i)){
                net.setFitness(net.getFitness()+1.0);
                //System.out.println("THIS IS RAN");
                //if(hack){
                //    System.out.println(net);
                //    hack=false;
                //} 
                //System.out.println("XOR Test "+i+" Passed");
            }
            else{
                //System.out.println("XOR Test "+i+" Failed");
            }
            net.reset();
        }
    }
    
    // getter methods
    public ArrayList<Test> getTests(){return tests;}
    public XORTest getXORTest(){return xorTest;}
    public ANDTest getANDTest(){return andTest;}
    //public boolean getHack(){return hack;}
    
    // setter methods
    public void setTests(ArrayList<Test> param){tests=param;}
    public void setXORTest(XORTest param){xorTest=param;}
    public void setANDTest(ANDTest param){andTest=param;}
    //public void setHack(boolean param){hack=param;}
}