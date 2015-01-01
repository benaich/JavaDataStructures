package Heap;

public class TreeHeap {

    public static class Node<T extends Comparable<? super T>> {

        public Node<T> left;
        public Node<T> right;
        public T data;
        public int rank;

        public Node(T data) {
            this.data = data;
            rank = 1;
        }

        public void insert(T data) {
            if (rank == 1) {
                if (left == null) {
                    left = new Node<>(data);
                } else {
                    right = new Node<>(data);
                    this.rank++;
                }
            } else {
                if (this.left.rank > this.right.rank) {
                    right.insert(data);
                } else {
                    left.insert(data);
                }
                rank++;
            }
        }

        public void insertSorted(T data) {
            this.insert(data);
            this.shiftUp();
        }

        public void shiftUp() {
            if (left != null) {
                left.shiftUp();
            }
            if (right != null) {
                right.shiftUp();
            }
            if (!isLeaf()) {
                Node<T> min;
                if(right == null) min = left;
                else{
                    min = (left.data.compareTo(right.data) < 0) ? left : right;
                }
                if (min.data.compareTo(data) < 0) {
                    T tmp = data;
                    data = min.data;
                    min.data = tmp;
                }
            }

        }

        public boolean isLeaf() {
            return right == null && left == null;
        }
    }

    public static <T extends Comparable<? super T>> Node<T> merge(Node<T> x, Node<T> y) {
        if (x == null) {
            return y;
        }
        if (y == null) {
            return x;
        }

        if (x.data.compareTo(y.data) > 0) {
            Node temp = x;
            x = y;
            y = temp;
        }

        x.right = merge(x.right, y);

        if (x.left == null) {
            x.left = x.right;
            x.right = null;

        } else {
            if (x.left.rank < x.right.rank) {
                Node temp = x.left;
                x.left = x.right;
                x.right = temp;
            }
            x.rank = x.right.rank + 1;
        }
        return x;
    }

    public static <T extends Comparable<? super T>> T min(Node<T> root) {
        if (root == null) {
            return null;
        }
        T tmp = root.data;
        return tmp;
    }

    public static <T extends Comparable<? super T>> Node<T> delete(Node<T> root) {
        if (root == null) {
            return null;
        }
        return merge(root.left, root.right);
    }

    public static <T extends Comparable<? super T>> Node<T> delete(Node<T> root, T data) {
        if (root == null) {
            return null;
        }
        Node left = delete(root.left, data);
        Node right = delete(root.right, data);

        if (data.compareTo(root.data) == 0) {
            root = merge(left, right);
        }
        return root;
    }

    public static <T extends Comparable<? super T>> Node<T> insert(T... a) {
        Node<T> root = new Node<>(a[0]);
        for (int i = 1; i < a.length; i++) {
            root = merge(root, new Node<>(a[i]));
        }
        return root;
    }

    public static void printNice(Node root, int level) {
        if (root == null) {
            return;
        }
        printNice(root.right, level + 1);
        if (root.rank != 0) {
            for (int i = 0; i < level - 1; i++) {
                System.out.print("|   ");
            }
            System.out.println("|---" + root.data);
        } else {
            System.out.println(root.data);
        }
        printNice(root.left, level + 1);
    }

    public static void main(String[] args) {
        Node<Integer> heap1, heap2, heap3, heap4;

        /*heap1 = insert(3, 1, 9, 10);
         System.out.println("------------ heap1 ------------");
         printNice(heap1, 1);

         heap2 = insert(4, 8, 5, 6, 11);
         System.out.println("------------ heap2 ------------");
         printNice(heap2, 1);

         heap3 = merge(heap1, heap2);
         System.out.println("------------ heap3 = heap1 + heap2 ------------");
         printNice(heap3, 1);

         System.out.println("------------- Min(heap3)  ------------");
         System.out.println(min(heap3));
        
         heap3 = delete(heap3);
         System.out.println("------------ deleter(heap3) ------------");
         printNice(heap3, 1);
        
         heap4 = insert(3, 1, 9, 10,1);
         System.out.println("------------ heap4 ------------");
         printNice(heap4, 1);
         heap4 = delete(heap4, 1);
         System.out.println("------------ delete(heap4, 1) ------------");
         printNice(heap4, 1);*/
        heap1 = insert(5);
        heap1.insertSorted(7);
        heap1.insertSorted(10);
        heap1.insertSorted(1);
        heap1.insertSorted(2);
        printNice(heap1, 1);
    }
}
