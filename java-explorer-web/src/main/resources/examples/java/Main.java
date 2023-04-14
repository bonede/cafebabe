import java.lang.System;
/**
 * Example java source file.
 * You may edit it and click Build button.
 */
class Main{
    public class Hole{
        private String name;
    }
    @Deprecated(since = "1.0.0")
    public static int fib(int n) throws IllegalArgumentException{
        if (n <= 1){
            throw new IllegalArgumentException("N must be greater than 1");
        }
        try {
            return fib(n - 1) + fib(n - 2);
        }catch (Exception e){
            throw new RuntimeException("Should not throw");
        }

    }
}