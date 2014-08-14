package core;
import evolution.EvolutionaryAlgorithm;
import experiments.GuaranteedXORTest;
import experiments.TestClass;
public class Driver{
    public Driver(){
        //new EvolutionaryAlgorithm();
        //new TestClass();
        new GuaranteedXORTest();
    }
    
    public static void main(String[] args){
        new Driver();
    }
}