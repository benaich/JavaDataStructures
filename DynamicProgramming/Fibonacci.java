package DynamicProgramming;

import java.util.HashMap;

public class Fibonacci {
    public static HashMap<Integer, Integer> tab = new HashMap<>();

    public static int get(int i) {
        if(i == 0 || i == 1){
            tab.put(i, i);
        }else{
            tab.put(i, get(i-1) + get(i-2));
        }
        return tab.get(i);
    }
    public static void main(String[] args) {
        System.out.println("fib(3) = "+ Fibonacci.get(3));
    }
    
}
