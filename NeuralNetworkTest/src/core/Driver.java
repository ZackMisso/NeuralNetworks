package core;
import evolution.EvolutionaryAlgorithm;
import experiments.GuaranteedXORTest;
import experiments.TestClass;
import testtools.CMDTester;
public class Driver{
    public Driver(){
        new EvolutionaryAlgorithm();
        //new TestClass();
        //new GuaranteedXORTest();
        //new CMDTester();
    }
    
    public static void main(String[] args){
        new Driver();
    }
}