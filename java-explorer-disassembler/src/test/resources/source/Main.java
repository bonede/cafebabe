import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
@Deprecated
@Anno
public final class Main extends Object implements Serializable{
    private static int intx = 42;
    public String hello = "hello";
    public int sum(@Anno int z, @Bar int bar){
        return Integer.parseInt(getY()) + getX() + z + bar;
    }
    public String[] listString(List<@Foo String> strings) throws IOException {
        return new String[]{};
    }
    public void bootstrap(){
        long lengthyColors = Arrays.asList("Red", "Green", "Blue")
                .stream().filter(c -> c.length() > 3).count();
    }
    public String getY(){
        Runnable action = new Runnable() {
            @Override
            public void run() {
            }
        };
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
    public static class Sub{

    }
    class Inner{
    }
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}