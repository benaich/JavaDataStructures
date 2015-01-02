package HashTable.SeparateChaining;

public class Node<K, V> {

    private K key;
    private V value;
    private Node<K, V> next;

    public Node(K key, V value) {
        setKey(key);
        setValue(value);
    }

    public Node(K key, V value, Node<K, V> next) {
        setKey(key);
        setValue(value);
        setNext(next);
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

    public Node<K, V> getNext() {
        return next;
    }

    public void setNext(Node<K, V> next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "(" + key + ", " + value + ")";
    }
}
