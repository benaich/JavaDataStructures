package HashTable;

import java.util.Random;
import java.util.function.Function;

public class HashTable<K, V> {

    public static final int INITIAL_TABLE_SIZE = 10;
    public static final double LOAD_FACTOR = 0.5;
    public static final int BIGNUMBER = 3345;

    private HashNode[] array;
    private Function<K, Integer> foh;
    private int tableSize = INITIAL_TABLE_SIZE;
    private int size;
    private static int numCollisions = 0;
    private static int numResizes = 0;

    public HashTable() {
        array = new HashNode[tableSize];
        CreateHashingFunction();
    }

    public HashTable(Function<K, Integer> f) {
        array = new HashNode[INITIAL_TABLE_SIZE];
        foh = f;
    }

    public float loadFactor() {
        return size / (float) tableSize;
    }

    public int indexOf(K key) {
        return foh.apply(key);
    }

    public void insert(K key, V val) {
        int index = indexOf(key);
        HashNode e = new HashNode(key, val);

        if (loadFactor() > LOAD_FACTOR) {
            //resize
            numResizes++;
            HashNode[] oldArray = array;
            array = new HashNode[tableSize * 2];
            tableSize = tableSize * 2;
            for (int i = 0; i < oldArray.length; i++) {
                HashNode<K, V> node = oldArray[i];
                while (node != null) {
                    insert(node.getKey(), node.getValue());
                    node = node.getNext();
                }
            }
        }

        if (array[index] == null) {
            array[index] = e;
        } else {
            insert(array[index], e);
            numCollisions++;
        }
        size++;
    }

    public void insert(HashNode old, HashNode e) {
        if (old.getNext() == null) {
            old.setNext(e);
        } else {
            insert(old.getNext(), e);
        }
    }

    public V get(K key) {
        int index = indexOf(key);
        HashNode<K, V> node = array[index];
        while (node != null) {
            if (node.getKey().equals(key)) {
                return node.getValue();
            }
            node = node.getNext();
        }
        return null;
    }

    public void delete(K key) {
        int index = indexOf(key);
        array[index] = null;
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

    private void CreateHashingFunction() {
        foh = (x) -> {
            Random r = new Random();
            int a, b, p;
            a = r.nextInt();
            b = r.nextInt();
            p = nextPrime(BIGNUMBER);

            return Math.abs(((a * x.hashCode() + b * x.hashCode()) * p) % tableSize);
        };
    }

    public void print() {
        for (HashNode item : array) {
            if (item != null) {
                System.out.print(item);
                print(item.getNext());
            } else {
                System.out.println("x");
            }
            System.out.println();
        }
    }

    public void print(HashNode e) {
        if (e == null) {
            return;
        }
        System.out.print(" - " + e);
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
        HashTable<String, String> ht = new HashTable<>(x -> Math.abs(x.hashCode()) % INITIAL_TABLE_SIZE);
        ht.insert("banana", "yellow");
        ht.insert("apple", "green");
        ht.insert("android", "green");
        ht.insert("cat", "white");
        ht.insert("body", "black");
        
        ht.print();
        
        System.out.println("----------- Get -----------");
        System.out.println("apple : " + ht.get("apple"));
        
        
        ht.delete("apple");
        ht.print();
    }

}
