package HashTable.LinearProbing;

import HashTable.HashTableInterface;
import java.util.function.Function;

public class HashTableLP<K, V> implements HashTableInterface<K, V> {

    private Node[] array;
    private Function<K, Integer> foh;
    private int tableSize = INITIAL_TABLE_SIZE;
    private int size;
    private int numCollisions = 0;
    private int numResizes = 0;

    public HashTableLP() {
        array = new Node[INITIAL_TABLE_SIZE];
        foh = getHashingFunction();
    }

    public HashTableLP(Function<K, Integer> f) {
        array = new Node[INITIAL_TABLE_SIZE];
        foh = f;
    }

    @Override
    public int indexOf(K key) {
        return foh.apply(key);
    }

    @Override
    public void put(K key, V val) {
        if (loadFactor() > LOAD_FACTOR) {
            resize();
        }
        int index = indexOf(key);
        int i = index;
        while (array[i] != null && !array[i].getKey().equals(key)) {
            i = (i + 1) % array.length;
        }
        if (i != index) {
            numCollisions++;
        }
        if (array[i] == null) {
            size++;
        }
        array[i] = new Node(key, val);
    }

    @Override
    public V get(K key) {
        int i = indexOf(key);
        while (array[i] != null && !array[i].getKey().equals(key)) {
            i = (i + 1) % array.length;
        }
        return (array[i] != null) ? (V) array[i].getValue() : null;
    }

    @Override
    public void delete(K key) {
        int i = indexOf(key);
        while (array[i] != null && !array[i].getKey().equals(key)) {
            i = (i + 1) % array.length;
        }
        array[i] = null;
        size--;
    }

    @Override
    public void clear() {
        array = new Node[tableSize];
        size = 0;
        numCollisions = 0;
    }

    @Override
    public void resize() {
        Node[] oldArray = array;
        tableSize = tableSize * 2;
        numResizes++;
        clear();

        for (Node<K, V> node : oldArray) {
            if (node != null) {
                put(node.getKey(), node.getValue());
            }
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int getTableSize() {
        return tableSize;
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
    public void print() {
        for (int i = 0; i < array.length; i++) {
            System.out.print(i + " -> ");
            if (array[i] != null) {
                System.out.print(array[i] + " " + indexOf((K) array[i].getKey()));
            }
            System.out.println();
        }
    }

    @Override
    public void printGraph() {
        for (int i = 0; i < array.length; i++) {
            System.out.print(i + " -> ");
            if (array[i] != null) {
                System.out.print(indexOf((K) array[i].getKey()));
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {

        HashTableLP<String, String> ht = new HashTableLP<>();
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

        System.out.println("----------- Print -----------");
        ht.print();

        System.out.println("----------- Get -----------");
        String deletedKey = "apple";
        System.out.println(deletedKey + " : i -> " + ht.indexOf(deletedKey) + " -> " + ht.get(deletedKey));

        System.out.println("----------- Delete " + deletedKey +" -----------");
        ht.delete(deletedKey);
        ht.printGraph();
        ht.printStatus();

        // Testing
        HashTableLP<String, String> table = new HashTableLP<>();
        for (int i = 0; i < 100000; i++) {
            table.put(HashTableInterface.randomString(), HashTableInterface.randomString());
        }
        table.printStatus();
    }
}
