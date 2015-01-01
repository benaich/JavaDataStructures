package Heap;

public class LLRB<T extends Comparable<? super T>> {

    public static boolean RED = true;
    public static boolean BLACK = false;

    public class Node {

        public Node left;
        public Node right;
        public T key;
        public boolean color;

        public Node(T key, Node left, Node right, boolean color) {
            this.key = key;
            this.left = left;
            this.right = right;
            this.color = color;
        }

        public Node rotateLeft() {
            return build(right.key, build(key, left, right.left, RED), right.right, color);
        }

        public Node rotateRihgt() {
            return build(left.key, left.left, build(key, right, left.right, RED), color);
        }

        public void sand() {
            color = RED;
            left.color = BLACK;
            right.color = BLACK;
        }

        public Node balance() {
            if (isRed(right) && !isRed(left)) {
                return rotateLeft().balance();
            }
            if (isRed(left) && isRed(left.left)) {
                return rotateRihgt();
            }
            return this;
        }
    }

    public Node build(T key, Node left, Node right, boolean color) {
        return new Node(key, left, right, color);
    }

    public Node build(T key) {
        return new Node(key, null, null, RED);
    }

    public Node put(Node p, T x) {
        if (p == null) {
            return build(x);
        }
        if (isRed(p.left) && isRed(p.right)) {
            p.sand();
        }
        if (p.key.compareTo(x) > 0) {
            return build(p.key, put(p.left, x), p.right, p.color).balance();
        } else {
            return build(p.key, p.left, put(p.right, x), p.color).balance();
        }
    }

    public boolean isRed(Node p) {
        return (p != null) && p.color == RED;
    }

    public void printNice(Node root, int level) {
        if (root == null) {
            return;
        }
        printNice(root.right, level + 1);
        if (level != 0) {
            for (int i = 0; i < level - 1; i++) {
                System.out.print("|   ");
            }
            if (isRed(root)) {
                System.out.println("|===" + root.key);
            } else {
                System.out.println("|---" + root.key);
            }
        } else {
            System.out.println(root.key);
        }
        printNice(root.left, level + 1);
    }

    public static void main(String[] args) {

        LLRB<Integer> app = new LLRB<>();
        LLRB.Node root = app.build(2);
        root = app.put(root, 4);
        root = app.put(root, 1);
        root = app.put(root, 3);
        root = app.put(root, 5);
        root = app.put(root, 6);
        app.printNice(root, 1);

    }

}
