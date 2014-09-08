/**
 *
 * @author Zackary Misso
 * 
 */
package datastructures;
import evolution.GlobalConstants;
import java.util.Random;
public class RandomNumberGenerator{
    private Random random;
    
    public RandomNumberGenerator(){
        random=new Random();
    }

    // returns an integer whose absolute value is less than max
    public int getInt(int max,boolean canBeNeg){
        boolean neg=false;
        int num=0;
        if(canBeNeg)
            neg=isNegative();
        num=random.nextInt(max);
        if(neg)
            num*=-1;
        return num;
    }
    
    // returns an integer whose absolute value is less than max
    // the integer can not be in the list
    public int getInt(int max,int[] not,boolean canBeNeg){
        boolean neg=false;
        int num=0;
        if(canBeNeg)
            neg=isNegative();
        num=random.nextInt(max);
        if(neg)
            num*=-1;
        while(intIn(num,not)){
            num=random.nextInt(max);
            if(canBeNeg)
                neg=isNegative();
            if(neg)
                num*=-1;
        }
        return num;
    }
    
    // changes a double without going outside the globally set bounds
    public double changeDouble(double prev,boolean canBeNeg){
        return changeDouble(prev,GlobalConstants.MAX_WEIGHT_VALUE,GlobalConstants.MIN_WEIGHT_VALUE,canBeNeg);
    }
    
    // auxillary function for the changeDouble method
    private double changeDouble(double prev,double max,double min,boolean canBeNeg){
        boolean neg=false;
        double change=0.0;
        if(canBeNeg)
            neg=isNegative();
        change=random.nextDouble();
        if(neg)
            change*=-1;
        while(doubleBounds(change+prev,min,max)){
            change=random.nextDouble();
            if(canBeNeg)
                neg=isNegative();
            if(neg)
                change*=-1;
        }
        return change+prev;
    }
    
    // creates a double between max and min
    public double initDouble(double max,double min){
        return min+random.nextDouble()*(max-min);
    }
    
    // creates a double between 1.0 and 0.0
    public double simpleDouble(){
        return random.nextDouble();
    }
    
    // checks if a double is between two others
    private boolean doubleBounds(double num,double min,double max){
        return num>max||num<min;
    }
    
    // checks if a specific integer is in a list
    private boolean intIn(int num,int[] list){
        if(list==null)
            return false;
        for(int i=0;i<list.length;i++)
            if(num==list[i])
                return true;
        return false;
    }
    
    // returns if a number will be negative
    public boolean isNegative(){
        return random.nextBoolean();
    }
    
    // getter methods
    public Random getRandom(){return random;}
    
    // setter methods
    public void setRandom(Random param){random=param;}
}