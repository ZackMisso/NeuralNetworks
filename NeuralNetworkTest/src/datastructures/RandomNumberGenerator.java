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

    public double changeDouble(double prev,boolean canBeNeg){
        return changeDouble(prev,GlobalConstants.MAX_WEIGHT_VALUE,GlobalConstants.MIN_WEIGHT_VALUE,canBeNeg);
    }
    
    public double changeDouble(double prev,double max,double min,boolean canBeNeg){
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
    
    public double initDouble(double max,double min){
        return min+random.nextDouble()*max;
    }

    public double simpleDouble(){
        return random.nextDouble();
    }
    
    private boolean doubleBounds(double num,double min,double max){
        return num>max||num<min;
    }
    
    private boolean intIn(int num,int[] list){
        if(list==null)
            return false;
        for(int i=0;i<list.length;i++)
            if(num==list[i])
                return true;
        return false;
    }
    
    public boolean isNegative(){
        return random.nextBoolean();
    }
    
    // getter methods
    public Random getRandom(){return random;}
    
    // setter methods
    public void setRandom(Random param){random=param;}
}