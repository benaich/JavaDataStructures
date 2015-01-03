package HashTable;

import java.util.Random;
import java.util.function.Function;

public interface HashTableInterface<K, V> {

    public static final int INITIAL_TABLE_SIZE = 10;
    public static final double LOAD_FACTOR = 0.5;
    public static final int BIGNUMBER = 9161;
    public static final Random r = new Random();
    public static final String BLACK_CIRCLE = "\u25CF";
    
    default float loadFactor() {
        return getSize() / (float) getTableSize();
    }

    int indexOf(K key);

    void put(K key, V val);

    V get(K key);

    void delete(K key);
    
    void clear();

    void resize();

    int getSize();
    int getTableSize();

    int getNumCollisions();

    int getNumResizes();

    void print();
    
    void printGraph();
    
    default void printStatus() {
        System.out.println("------------- Table Status -------------------");
        System.out.println("Table size : " + getTableSize());
        System.out.println("Actual size : " + getSize());
        System.out.println("Numbre of collisions : " + getNumCollisions());
        System.out.println("Numbre of resises : " + getNumResizes());
    }
    
    default Function<K, Integer> getHashingFunction() {
        int a, b, p;
        a = r.nextInt();
        b = r.nextInt();
        p = HashTableInterface.nextPrime(BIGNUMBER);

        return (x) -> Math.abs(((a * x.hashCode() + b * x.hashCode()) * p) % getTableSize());
    }
    
    static int nextPrime(int a) {
        while (!isPrime(a)) {
            a++;
        }
        return a;
    }

    static boolean isPrime(int a) {
        if (a == 2) {
            return true;
        } else if (a % 2 == 0) {
            return false;
        } else {
            int i = 3, end = (int) Math.sqrt(a + 0.0);
            while (a % i != 0 && i <= end) {
                i = i + 2;
            }
            return i > end;
        }
    }
    static String randomString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            char c = (char) (r.nextInt((int) (Character.MAX_VALUE)));
            sb.append(c);
        }
        return sb.toString();
    }
}
