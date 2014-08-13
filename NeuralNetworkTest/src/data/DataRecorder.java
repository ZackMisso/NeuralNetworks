package data;

import networks.NeuralNetwork;
import evolution.EvolutionaryAlgorithm;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
public class DataRecorder{
    public DataRecorder(){
        initializeRecorder();
    }
    
    private void initializeRecorder(){
        // to be implemented possibly later
    }
    
    public void writeBestsToFile(ArrayList<NeuralNetwork> nets){
        // implement
    }
    
    public void writeSolutionToFile(NeuralNetwork net){
        // implement
    }
    
    public void writeCurrentExperimentToFile(EvolutionaryAlgorithm algorithm){
        // implement
    }
    
    public void readCurrentExperimentFromFile(EvolutionaryAlgorithm algorithm){
        // implement
    }
}
