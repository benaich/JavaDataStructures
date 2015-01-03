package HashTable.Cuckoo;

import HashTable.LinearProbing.*;

public class Node<K, V> {

    private K key;
    private V value;
    private boolean status = false;
    
    public int[] hachCode = new int[2];
    public Node(K key, V value) {
        setKey(key);
        setValue(value);
    }
    public Node(K key, V value, int vh1, int vh2) {
        this(key, value);
        hachCode[0] = vh1;
        hachCode[1] = vh2;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    public boolean getStatus() {
        return status;
    }
    public void toggleStatus(){
        status = !status;
    }

    @Override
    public String toString() {
        //return "(" + key + ", " + value + ")";
        return "(" + key + ", [" + hachCode[0] +  "," + hachCode[1] + "])";
    }
}

