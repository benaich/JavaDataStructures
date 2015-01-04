package HashTable.SeparateChaining;

import HashTable.HashTableInterface;
import java.util.function.Function;

public class SCHashTable<K, V> implements HashTableInterface<K, V> {

    private Node[] array;
    private Function<K, Integer> foh;
    private int tableSize = INITIAL_TABLE_SIZE;
    private int size = 0;
    private int numCollisions = 0;
    private int numResizes = 0;

    public SCHashTable() {
        array = new Node[tableSize];
        foh = getHashingFunction();
    }

    public SCHashTable(Function<K, Integer> f) {
        array = new Node[tableSize];
        foh = f;
    }

    @Override
    public int indexOf(K key) {
        return foh.apply(key);
    }
    
    @Override
    public boolean contains(K key) {
        return array[indexOf(key)] != null;
    }

    @Override
    public void put(K key, V val) {
        int index = indexOf(key);

        if (loadFactor() > LOAD_FACTOR) {
            resize();
        }

        Node<K, V> node = array[index];
        while (node != null) {
            if (node.getKey().equals(key)) {
                node.setValue(val);
                return;
            }
            node = node.getNext();
        }
        if (array[index] != null) {
            numCollisions++;
        }
        array[index] = new Node(key, val, array[index]);
        size++;
    }

    @Override
    public V get(K key) {
        int index = indexOf(key);
        Node<K, V> node = array[index];
        while (node != null) {
            if (node.getKey().equals(key)) {
                return node.getValue();
            }
            node = node.getNext();
        }
        return null;
    }

    @Override
    public void delete(K key) {
        int index = indexOf(key);
        if (array[index].getKey().equals(key)) {
            array[index] = null;
        } else {
            delete(key, array[index], array[index].getNext());
        }
        size--;
    }

    public void delete(K key, Node p, Node node) {
        if (node == null) {
            return;
        }
        if (node.getKey().equals(key)) {
            p.setNext(node.getNext());
        }
        delete(key, node, node.getNext());
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
            while (node != null) {
                put(node.getKey(), node.getValue());
                node = node.getNext();
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
                Node node = array[i];
                while (node != null) {
                    System.out.print(" " + node);
                    node = node.getNext();
                }
            }
            System.out.println();
        }
    }

    @Override
    public void printGraph() {
        System.out.println("----------- Print Graph-----------");
        for (int i = 0; i < array.length; i++) {
            System.out.print(i + " | ");
            if (array[i] != null) {
                Node node = array[i];
                while (node != null) {
                    System.out.print(BLACK_CIRCLE);
                    node = node.getNext();
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {

        SCHashTable<String, String> ht = new SCHashTable<>();
        ht.put("banana", "yellow");
        ht.put("apple", "green");
        ht.put("android", "green");
        ht.put("cat", "white");
        ht.put("body", "black");
        ht.put("class", "red");
        ht.put("jesy", "pinkman");
        ht.put("walter", "white");
        ht.put("arys", "startk");
        ht.put("dextr", "red");

        ht.print();

        System.out.println("----------- Get -----------");
        String deletedKey = "apple";
        System.out.println(deletedKey + " : i -> " + ht.indexOf(deletedKey) + " -> " + ht.get(deletedKey));

        System.out.println("----------- Delete " + deletedKey + " -----------");
        ht.delete(deletedKey);

        ht.printGraph();
        ht.printStatus();
        
        // Testing
        SCHashTable<String, String> table = new SCHashTable<>();
        for (int i = 0; i < 100000; i++) {
            table.put(HashTableInterface.randomString(), HashTableInterface.randomString());
        }
        table.printStatus();
    }
}
