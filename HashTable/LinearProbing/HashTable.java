package HashTable.LinearProbing;

import java.util.Random;
import java.util.function.Function;

public class HashTable<K, V> {

    public static final int INITIAL_TABLE_SIZE = 10;
    public static final double LOAD_FACTOR = 0.5;
    public static final int BIGNUMBER = 3345;

    private Node[] array;
    private Function<K, Integer> foh;
    private int tableSize = INITIAL_TABLE_SIZE;
    private int size;
    private static int numCollisions = 0;
    private static int numResizes = 0;

    public HashTable() {
        array = new Node[INITIAL_TABLE_SIZE];
        foh = getHashingFunction();
    }

    public HashTable(Function<K, Integer> f) {
        array = new Node[INITIAL_TABLE_SIZE];
        foh = f;
    }

    public float loadFactor() {
        return size / (float) tableSize;
    }

    public int indexOf(K key) {
        return foh.apply(key);
    }

    public void put(K key, V val) {
        if (loadFactor() > LOAD_FACTOR) {
            resize();
        }
        int index = indexOf(key);
        int i = index;
        while (array[i] != null && array[i].getKey().equals(key)){
            i = (i + 1 % array.length);
        }
        if (i != index) {
            numCollisions++;
        }
        array[i] = new Node(key, val);
        size++;
    }

    public V get(K key) {
        int i = indexOf(key);
        while (array[i] != null && !array[i].getKey().equals(key)) {
            i = (i + 1 % array.length);
        }
        return (array[i] != null) ? (V)array[i].getValue() : null;
    }

    public void resize() {
        numResizes++;
        Node[] oldArray = array;
        array = new Node[tableSize * 2];
        tableSize = tableSize * 2;
        for (int i = 0; i < oldArray.length; i++) {
            Node<K, V> node = oldArray[i];
            if(node != null)
                put(node.getKey(), node.getValue());
        }
    }

    public int getSize() {
        return size;
    }

    public int getNumCollisions() {
        return numCollisions;
    }

    public int getNumResizes() {
        return numResizes;
    }

    private Function<K, Integer> getHashingFunction() {

        Random r = new Random();
        int a, b, p;
        a = r.nextInt();
        b = r.nextInt();
        p = nextPrime(BIGNUMBER);

        return (x) -> Math.abs(((a * x.hashCode() + b * x.hashCode()) * p) % tableSize);
    }

    public void print() {
        for (int i = 0; i < array.length; i++) {

            if (array[i] != null) {
                System.out.print(i + " -> " + array[i]);
            } else {
                System.out.print(i + " -> x");
            }
            System.out.println();
        }
    }

    /* helpers */
    private static int nextPrime(int a) {
        while (!isPrime(a)) {
            a++;
        }
        return a;
    }

    private static boolean isPrime(int a) {
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

    public static void main(String[] args) {
        Function<String, Integer> modulo = x -> Math.abs(x.hashCode()) % INITIAL_TABLE_SIZE;

        HashTable<String, String> ht = new HashTable<>(modulo);
         ht.put("banana", "yellow");
         ht.put("apple", "green");
         ht.put("android", "green");
         ht.put("cat", "white");
         ht.put("body", "black");

         System.out.println("----------- Print -----------");
         ht.print();

         System.out.println("----------- Get -----------");
         String deletedKey = "apple";
         System.out.println(deletedKey + " : i -> " + ht.indexOf(deletedKey) + " -> " + ht.get(deletedKey));

         System.out.println("----------- Delete -----------");
        // ht.delete(deletedKey);
         System.out.println("----------- Print -----------");
         ht.print();
         
        /*HashTable<String, String> table = new HashTable<>(modulo);

        for (int i = 0; i < 100000; i++) {
            table.put(randomString(), randomString());
        }

        System.out.println(table.getNumCollisions());
        System.out.println(table.getNumResizes());*/
    }

    public static String randomString() {
        Random r = new Random(); // perhaps make it a class variable so you don't make a new one every time
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            char c = (char) (r.nextInt((int) (Character.MAX_VALUE)));
            sb.append(c);
        }
        return sb.toString();
    }
}
