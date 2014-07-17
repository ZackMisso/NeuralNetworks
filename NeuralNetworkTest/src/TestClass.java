// This class is used to do unit tests
import java.util.ArrayList;
public class TestClass{
    public TestClass(){
        mergeSortUnitTest();
    }
    
    private void mergeSortUnitTest(){
        ArrayList<Double> list=initializeMergeSort();
        list=mergeSort(list);
        for(int i=0;i<list.size();i++)
            System.out.println(list.get(i));
    }
    
    private ArrayList<Double> initializeMergeSort(){
        ArrayList<Double> list=new ArrayList<>();
        list.add(42.9);
        list.add(6.0);
        list.add(1040.0);
        list.add(2.0);
        list.add(0.0);
        list.add(-4.0);
        list.add(92.0);
        list.add(69.0);
        list.add(13.0);
        list.add(1000000.1);
        return list;
    }
    
    private ArrayList<Double> mergeSort(ArrayList<Double> list){
        if(list.size()==1)
            return list;
        ArrayList<Double> one=new ArrayList<>();
        ArrayList<Double> two=new ArrayList<>();
        int i=0;
        for(;i<list.size()/2;i++)
            one.add(list.get(i));
        for(;i<list.size();i++)
            two.add(list.get(i));
        one=mergeSort(one);
        two=mergeSort(two);
        return merge(one,two);
    }
    
    private ArrayList<Double> merge(ArrayList<Double> one,ArrayList<Double> two){  
        ArrayList<Double> merged=new ArrayList<>();
        while(!one.isEmpty()&&!two.isEmpty()){
            if(one.get(0)>two.get(0))
                merged.add(one.remove(0));
            else
                merged.add(two.remove(0));
        }
        while(!one.isEmpty())
            merged.add(one.remove(0));
        while(!two.isEmpty())
            merged.add(two.remove(0));
        return merged;
    }
}