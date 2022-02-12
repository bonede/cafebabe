import java.io.Serializable;
import java.util.List;
@Deprecated
@Anno
public final class Main extends Object implements Serializable{
    private static int intx = 42;
    public String hello = "hello";
    public int sum(@Anno int z, @Bar int bar){
        return Integer.parseInt(getY()) + getX() + z + bar;
    }
    public String[] listString(List<@Foo String> strings){
        return new String[]{};
    }
    public String getY(){
        try {
            int x = 1 + 1;
        }catch (RuntimeException e){
            System.out.println("Exception catched");
        }
        String y = "20";
        return y;
    }
    private int getX(){
        int x = 10;
        return x;
    }
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}