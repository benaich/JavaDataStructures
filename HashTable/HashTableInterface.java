package HashTable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.function.Function;

public interface HashTableInterface<K, V> {

    public static enum HASH_FUNCTION {

        UNIVERSAL, MODULO, MULTIPLICATIF
    };
    public static final int INITIAL_TABLE_SIZE = 10;
    public static final int MAX_TABLE_SIZE = 10000000;
    public static final double LOAD_FACTOR = 0.6;
    public static final int BIGNUMBER = 9161;
    public static final Random r = new Random();
    public static final String BLACK_CIRCLE = "\u25CF";
    public static final double GOLDEN_RATIO = (1 + Math.sqrt(5)) / 2;

    /* get the position of key */
    int indexOf(K key);

    /* check whether the specified key exist in the hashtable */
    boolean contains(K key);

    /* update or insert a new item to the hashtable */
    void put(K key, V val);

    /* get the value of the key*/
    V get(K key);

    /* remove the specified item */
    void delete(K key);

    /* remove all data from the hashtable */
    void clear();

    /* Double the size of the hashtable */
    void resize();

    /* get the number of elements in the table */
    int getSize();

    /* get the table size */
    int getTableSize();

    /* get the number of collisions */
    int getNumCollisions();

    /* get the number of resizes */
    int getNumResizes();

    /* get the current load factor */
    default float loadFactor() {
        return getSize() / (float) getTableSize();
    }

    /* return de distrubution of the hash table */
    public double getDistribution();

    /* print the table */
    void print();

    /* print the table like a "graph" */
    void printGraph();

    /* print the table curent status */
    default void printStatus() {
        System.out.println("------------- Table Status -------------------");
        System.out.println("Table size : " + getTableSize() + " Actual size : " + getSize());
        //System.out.println("Numbre of collisions : " + getNumCollisions());
        //System.out.println("Numbre of resises : " + getNumResizes());
        System.out.println("Deviation : " + getDistribution());
    }

    /* return a random universal hashing function */
    default Function<K, Integer> getHashingFunction() {
        return getHashingFunction(HASH_FUNCTION.UNIVERSAL);
    }

    /* return a hashing function (modulo, universal or multiplicatif) */
    default Function<K, Integer> getHashingFunction(HASH_FUNCTION type) {
        Function<K, Integer> f;
        if (type == HASH_FUNCTION.MODULO) {
            f = x -> Math.abs(x.hashCode()) % getTableSize();
        } else if (type == HASH_FUNCTION.UNIVERSAL) {
            int a, b, p;
            do{
                a = r.nextInt();
                b = r.nextInt();
            }
            while (a == 0 || b == 0);
            final int r1 = a, r2 = b;
            p = nextPrime(BIGNUMBER);
            f = x -> Math.abs(((r1 * x.hashCode() + r2) % p) % getTableSize());
        } else {
            f = x -> {
                double c = GOLDEN_RATIO - 1;
                double tmp = x.hashCode() * c;
                return (int) (((Math.abs(tmp - (int) tmp))) * getTableSize());
            };
        }
        return f;
    }

    /* get the next prime number after a*/
    static int nextPrime(int a) {
        while (!isPrime(a)) {
            a++;
        }
        return a;
    }

    /* check if a number is prime */
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

    /* return a random string (for testing) */
    static String randomString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            char c = (char) (r.nextInt((int) (Character.MAX_VALUE)));
            sb.append(c);
        }
        return sb.toString();
    }

    static void importFromFile(File file, HashTableInterface<String, String> ht) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s");
                ht.put(parts[0], parts[1]);
            }
            reader.close();
        } catch (IOException ex) {
            System.out.println("file not found");
        }
    }

    static double getStdDeviation(int[] data) {
        double sum = 0L;
        for (double a : data) {
            sum += a;
        }
        double mean = sum / data.length;
        sum = 0L;
        for (double a : data) {
            sum += (mean - a) * (mean - a);
        }

        return Math.sqrt(sum / data.length);
    }

}
