package HashTable.SeparateChaining;

import HashTable.HashTableInterface;
import java.io.File;
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

    public SCHashTable(HASH_FUNCTION type) {
        array = new Node[tableSize];
        foh = getHashingFunction(type);
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

        if (loadFactor() > LOAD_FACTOR && getTableSize() * 2 < MAX_TABLE_SIZE) {
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

    @Override
    public double getDistribution() {
        int[] tab = new int[getTableSize()];
        
        for (int i = 0; i < getTableSize(); i++) {
            Node node = array[i];
            while (node != null) {
                tab[i]++;
                node = node.getNext();
            }
        }
        
        return HashTableInterface.getStdDeviation(tab);
    }
    
    private static void testing(HASH_FUNCTION type) {
        System.out.println("------------------ " + type + " ------------------");
        SCHashTable<String, String> table = new SCHashTable<>(type);
        for (int i = 0; i < 100000; i++) {
            table.put(HashTableInterface.randomString(), HashTableInterface.randomString());
            if(i==100 || i==500 || i==1000 || i==2000 || i==5000 || i==10000 || i==50000 || i== 80000 || i==100000)
                table.printStatus();
        }
        
    }
    
    public static void main(String[] args) {
        /*
        SCHashTable<String, String> ht = new SCHashTable<>(HASH_FUNCTION.UNIVERSAL);
        File file = new File("src/HashTable/data.txt");
        HashTableInterface.importFromFile(file, ht);
        ht.printStatus();
                */
        
        
        testing(HASH_FUNCTION.UNIVERSAL);
        testing(HASH_FUNCTION.MULTIPLICATIF);
        testing(HASH_FUNCTION.MODULO);
    }

}
