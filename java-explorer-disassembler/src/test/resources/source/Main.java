import java.io.Serializable;

public final class Main extends Object implements Serializable{
    private static int intx = 42;
    public String hello = "hello";
    public int sum(){
        return Integer.parseInt(getY()) + getX();
    }
    public String getY(){
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