import java.util.ArrayList;
public class ANDTest {
    private ArrayList<Test> tests;
    
    public ANDTest(){
        tests=new ArrayList<>();
    }
    
    // getter methods
    public ArrayList<Test> getTests(){return tests;}
    
    // setter methods
    public void setTests(ArrayList<Test> param){tests=param;}
}