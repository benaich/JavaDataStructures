package Huffman;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Node implements Comparable<Node>, Serializable {

    Paire p;
    Node left;
    Node right;

    public Node(Paire cle, Node left, Node right) {
        this.p = cle;
        this.left = left;
        this.right = right;
    }

    public Node(Paire cle) {
        this.p = cle;
    }

    public Node() {
        this.p = null;
    }

    public void insert(Node x) {
        if (p == null) {
            left = x.left;
        } else {
            left = this;
        }

        right = x.right;
        p = x.p;
    }

    @Override
    public int compareTo(Node n) {
        return p.compareTo(n.p);
    }

    public boolean isLeaf() {
        return right == null && left == null;
    }

    public static Node build(Paire cle, Node left, Node right) {
        return new Node(cle, left, right);
    }

    public static void serialaze(Node x, String src) throws IOException {
        try {
            try (FileOutputStream ostream = new FileOutputStream(src)) {
                ObjectOutputStream p = new ObjectOutputStream(ostream);
                p.writeObject(x);
                p.flush();
                ostream.close();
            }
        } catch (Exception ex) {
        }
    }

    public static Node deserialaze(String src) throws IOException {
        try {
            FileInputStream istream = new FileInputStream(src);
            ObjectInputStream q = new ObjectInputStream(istream);

            Node new_tree = (Node) q.readObject();
            istream.close();
            q.close();
            return new_tree;
        } catch (Exception ex) {
        }
        return null;
    }

}
