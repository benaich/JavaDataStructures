package Heap;

public class HeapModule {

    public static interface Heap<T> {

        Heap left();
        Heap right();
        T data();
        int rank();
        boolean isEmpty();
        @Override
        String toString();
        int size();
    }

    public static class NonEmptyHeap<T> implements Heap<T> {

        public Heap left;
        public Heap right;
        public T data;
        public int rank;

        public NonEmptyHeap(T x, Heap a, Heap b) {
            this(0, x, a, b);
        }

        public NonEmptyHeap(int r, T x, Heap a, Heap b) {
            rank = r;
            data = x;
            left = a;
            right = b;
        }

        public Heap left() {
            return left;
        }

        public Heap right() {
            return right;
        }

        public T data() {
            return data;
        }

        public int rank() {
            return rank;
        }

        public boolean isEmpty() {
            return false;
        }
        
        public int size(){
            return 1 + left.size() + right.size();
        }
    }

    public static final Heap<? extends Object> EMPTY = new Heap<Object>() {

        public Heap left() {
            throw new UnsupportedOperationException();
        }

        public Heap right() {
            throw new UnsupportedOperationException();
        }

        public Object data() {
            throw new UnsupportedOperationException();
        }

        public int rank() {
            return 0;
        }

        public String toString() {
            return "";
        }

        public boolean isEmpty() {
            return true;
        }
        public int size(){
            return 0;
        }
    };

    public static <T> Heap<T> emptyHeap() {
        return (Heap) EMPTY;
    }

    public static <T extends Comparable<? super T>> Heap<T> heap(T x, Heap<T> a, Heap<T> b) {
        if (a.rank() > b.rank()) {
            return new NonEmptyHeap<>(b.rank() + 1, x, a, b);
        }
        return new NonEmptyHeap<>(a.rank() + 1, x, b, a);
    }

    public static <T extends Comparable<? super T>> Heap<T> heap(T x) {
        return heap(x, emptyHeap(), emptyHeap());
    }

    public static <T extends Comparable<? super T>> Heap<T> insert(Heap<T> a, T x) {
        if (a.isEmpty()) {
            return heap(x);
        }
        if (a.data().compareTo(x) <= 0) {
            return heap(a.data(), a.left(), insert(a.right(), x));
        } else {
            return heap(x, a.left(), insert(a.right(), a.data()));
        }
    }

    public static <T extends Comparable<? super T>> Heap<T> insert(T... array) {
        Heap<T> root = emptyHeap();
        for (T item : array) {
            root = insert(root, item);
        }
        return root;
    }

    public static <T extends Comparable<? super T>> Heap<T> leftList(T... array) {
        Heap<T> root = emptyHeap();
        for (T item : array) {
            root = merge(root, heap(item));
        }
        return root;
    }

    public static <T extends Comparable<? super T>> Heap<T> merge(Heap<T> a, Heap<T> b) {
        if (a.isEmpty()) {
            return b;
        }
        if (b.isEmpty()) {
            return a;
        }

        if (a.data().compareTo(b.data()) <= 0) {
            return heap(a.data(), a.left(), merge(a.right(), b));
        }
        return heap(b.data(), b.left(), merge(a, b.right()));
    }
    
    public static <T extends Comparable<? super T>> Heap<T> simpleMerge(Heap<T> a, Heap<T> b) {
        if (a.isEmpty()) {
            return b;
        }
        if (b.isEmpty()) {
            return a;
        }

        if (a.data().compareTo(b.data()) <= 0) {
            return heap(a.data(), b, merge(a.left(), a.right()));
        }
        return heap(b.data(), merge(a.data(), b.left(), b.right()), merge(a.left(), a.right()));
    }
    
    public static <T extends Comparable<? super T>> Heap<T> merge(T x, Heap<T> a, Heap<T> b) {
        if (a.isEmpty()) {
            return insert(a, x);
        }
        if (b.isEmpty()) {
            return insert(b, x);
        }

        if ((x.compareTo(a.data()) <= 0) && (x.compareTo(b.data()) <= 0)) {
            return heap(x, a, b);
        }
        if ((a.data().compareTo(b.data()) <= 0) && (a.data().compareTo(x) <= 0)) {
            return heap(a.data(), merge(x, a.left(), a.right()), b);
        }
        return heap(b.data(), merge(x, b.left(), b.right()), a);
    }
    
    public static <T extends Comparable<? super T>> T min(Heap<T> root) {
        if (root == null) {
            return null;
        }
        return  root.data();
    }
    
    public static <T extends Comparable<? super T>> Heap<T> delete(Heap<T> root) {
        if (root == null) {
            return null;
        }
        return merge(root.left(), root.right());
    }

    public static <T> void printNice(Heap<T> root, int level) {
        if (root.isEmpty()) {
            return;
        }
        printNice(root.right(), level + 1);
        if (level != 0) {
            for (int i = 0; i < level - 1; i++) {
                System.out.print("|   ");
            }
            System.out.println("|---" + root.data());
        } else {
            System.out.println(root.data());
        }
        printNice(root.left(), level + 1);
    }

    public static void main(String[] args) {

        Heap<Integer> heap1 = insert(5, 9, 3);
        System.out.println("------------ Balanced Heap ------------");
        printNice(heap1, 1);
        Heap<Integer> heap3 = insert(1, 8, 2);
        System.out.println("------------ Balanced Heap ------------");
        printNice(heap3, 1);
        printNice(simpleMerge(heap1, heap3), 1);
        Heap<Integer> heap2 = leftList(5, 9, 3, 1, 2, 15, 22);
        System.out.println("------------ LeftList Heap ------------");
        printNice(heap2, 1);
    }
}
