package experiments;

import java.util.ArrayList;
public class Test {
    private ArrayList<Double> inputs;
    private ArrayList<Double> outputs;
    private boolean posneg; // if pos or negative is accepted
    private boolean one; // if only 1,0,-1 are accepted
    private boolean exact; // if the exact values are needed
    private boolean posnegdiff;
    
    public Test(){
        inputs=new ArrayList<>();
        outputs=new ArrayList<>();
        posneg=false;
        one=false;
        exact=true;
        posnegdiff=false;
    }
    
    public boolean matches(ArrayList<Double> test){
        if(posneg)
            return posNegTest(test);
        if(one)
            return oneTest(test);
        if(exact)
            return exactTest(test);
        if(posnegdiff)
            return posNegDiffTest(test);
        return false;
    }
    
    private boolean posNegTest(ArrayList<Double> test){
        if(test.isEmpty()||outputs.isEmpty())
            return false;
        for(int i=0;i<outputs.size();i++){
            if(outputs.get(i)>0.0&&test.get(i)<=0.0)
                return false;
            if(outputs.get(i)<=0.0&&test.get(i)>0.0)
                return false;
        }
        return true;
    }
    
    private boolean posNegDiffTest(ArrayList<Double> test){
        if(test.isEmpty()||outputs.isEmpty())
            return false;
        for(int i=0;i<outputs.size();i++){
            if(outputs.get(i)>=0.0&&test.get(i)>=0.0)
                return false;
            if(outputs.get(i)<0.0&&test.get(i)<0.0)
                return false;
        }
        return true;
    }
    
    private boolean oneTest(ArrayList<Double> test){
        ArrayList<Double> chk=new ArrayList<>();
        for(int i=0;i<test.size();i++){
            if(test.get(i)<=-1.0)
                chk.add(-1.0);
            else if(test.get(i)>=1.0)
                chk.add(1.0);
            else
                chk.add(0.0);
        }
        for(int i=0;i<chk.size();i++)
            if(chk.get(i)!=outputs.get(i))
                return false;
        return true;
    }
    
    private boolean exactTest(ArrayList<Double> test){
        for(int i=0;i<test.size();i++)
            if(!test.get(i).equals(outputs.get(i)))
                return false;
        return true;
    }
    
    // getter methods
    public ArrayList<Double> getInputs(){return inputs;}
    public ArrayList<Double> getOutputs(){return outputs;}
    public boolean getPosNeg(){return posneg;}
    public boolean getOne(){return one;}
    public boolean getExact(){return exact;}
    public boolean getPosNegDiff(){return posnegdiff;}
    
    // setter methods
    public void setInputs(ArrayList<Double> param){inputs=param;}
    public void setOutputs(ArrayList<Double> param){outputs=param;}
    public void setPosNeg(boolean param){posneg=param;}
    public void setOne(boolean param){one=param;}
    public void setExact(boolean param){exact=param;}
    public void setPosNegDiff(boolean param){posnegdiff=param;}
}