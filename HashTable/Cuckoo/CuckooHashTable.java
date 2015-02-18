package HashTable.Cuckoo;

import HashTable.HashTableInterface;
import java.util.function.Function;

public class CuckooHashTable<K, V> implements HashTableInterface<K, V> {
    /* How many times should we do the insert loop before rehaching */
    private final static int MAX_INSERT_TRIES = 10;

    private Node[][] array;
    private Function<K, Integer>[] foh = new Function[2];
    private int tableSize = INITIAL_TABLE_SIZE;
    private int size;
    private int numCollisions;
    private int numResizes = 0;
    private int numRehashes = 0;

    public CuckooHashTable() {
        this.tableSize = HashTableInterface.nextPrime(INITIAL_TABLE_SIZE);
        this.clear();
        this.hash();
    }

    public void hash() {
        foh[0] = getHashingFunction();
        foh[1] = getHashingFunction();
    }

    @Override
    public void clear() {
        array = new Node[2][tableSize];
        size = 0;
        numCollisions = 0;
    }

    @Override
    public int getTableSize() {
        return tableSize;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int getNumCollisions() {
        return numCollisions;
    }

    @Override
    public int getNumResizes() {
        return numResizes;
    }
    
    @Override
    public double getDistribution(){return 0;}

    @Override
    public int indexOf(K key) {
        int i = foh[0].apply(key);
        int j = foh[1].apply(key);
        if (array[0][i] != null && array[0][i].getKey().equals(key)) {
            return i;
        }

        if (array[1][j] != null && array[1][j].getKey().equals(key)) {
            return j;
        }

        return -1;
    }

    @Override
    public boolean contains(K key) {
        return indexOf(key) != -1;
    }
    
    @Override
    public void put(K key, V val) {
        if (contains(key)) {
            return;
        }

        if (loadFactor() > LOAD_FACTOR) {
            rehache(2 * tableSize);
        }

        Node<K, V> node = new Node<>(key, val, foh[0].apply(key), foh[1].apply(key));
        put(node, MAX_INSERT_TRIES);
        size++;
    }

    public void put(Node<K, V> node, int nuberOftries) {
        if(nuberOftries==0)
            rehache(tableSize);
        int index = (node.getStatus()) ? 1 : 0;
        int hachCode = node.hachCode[index];
        Node<K, V> tmp = array[index][hachCode];
        array[index][hachCode] = node;
        if (tmp == null) {
            return;
        }
        numCollisions++;
        tmp.toggleStatus();
        put(tmp, nuberOftries-1);
    }

    @Override
    public V get(K key) {
        int i = indexOf(key);
        if (i == -1) {
            return null;
        }
        if (array[0][i] != null && array[0][i].getKey().equals(key)) {
            return (V) array[0][i].getValue();
        }
        return (V) array[1][i].getValue();
    }

    @Override
    public void delete(K key) {
        int i = indexOf(key);
        if (i == -1) {
            return;
        }
        if (array[0][i] != null && array[0][i].getKey().equals(key)) {
            array[0][i] = null;
        } else {
            array[1][i] = null;
        }
        size--;
    }

    @Override
    public void resize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void print() {
        System.out.println("----------- Print -----------");
        System.out.println("id |   array 1  |   Array 2   ");
        Node[] subArray1 = array[0];
        Node[] subArray2 = array[1];

        for (int i = 0; i < tableSize; i++) {
            System.out.println(i + " | " + subArray1[i] + " | " + subArray2[i]);
        }
    }

    @Override
    public void printGraph() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void printStatus() {
        System.out.println("------------- Table Status -------------------");
        System.out.println("Table size : " + getTableSize());
        System.out.println("Actual size : " + getSize());
        System.out.println("Numbre of collisions : " + getNumCollisions());
        System.out.println("Numbre of resises : " + getNumResizes());
        System.out.println("Numbre of rehashes : " + numRehashes);
    }

    private void rehache(int newLength) {
        Node[][] oldArray = array;
        this.tableSize = HashTableInterface.nextPrime(newLength);
        this.clear();
        this.hash();
        numRehashes++;
        if (newLength != tableSize) {
            numResizes++;
        }

        for (Node[] subArray : oldArray) {
            for (Node<K, V> node : subArray) {
                if (node != null) {
                    put(node.getKey(), node.getValue());
                }
            }
        }
    }

    public static void main(String[] args) {
        CuckooHashTable<String, String> ht = new CuckooHashTable<>();

        ht.put("banana", "yellow");
        ht.put("apple", "green");
        ht.put("android", "green");
        ht.put("cat", "white");
        ht.put("body", "black");
        ht.put("glass", "red");
        ht.put("jessy", "pinkman");
        ht.put("walter", "white");
        ht.put("arya", "startk");
        ht.put("dexter", "red");

        ht.print();

        System.out.println("----------- Get -----------");
        String deletedKey = "apple";
        System.out.println(deletedKey + " : -> " + ht.get(deletedKey));
        
        System.out.println("----------- Delete " + deletedKey +" -----------");
        ht.delete(deletedKey);
        ht.print();
        ht.printStatus();

        // Testing
        CuckooHashTable<String, String> table = new CuckooHashTable<>();
        for (int i = 0; i < 100000; i++) {
            table.put(HashTableInterface.randomString(), HashTableInterface.randomString());
        }
        table.printStatus();
    }
}
