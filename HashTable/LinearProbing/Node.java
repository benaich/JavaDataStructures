package HashTable.LinearProbing;

public class Node<K, V> {

    private K key;
    private V value;

    public Node(K key, V value) {
        setKey(key);
        setValue(value);
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

    @Override
    public String toString() {
        return "(" + key + ", " + value + ")";
    }
}

