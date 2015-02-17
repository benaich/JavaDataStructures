package Heap;

public class HeapModule {

    public static interface Heap<T> {
        Heap left();
        Heap right();
        T data();
        int rank();
        boolean isEmpty();
        int size();
        @Override
        String toString();
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

        @Override
        public Heap left() {
            return left;
        }

        @Override
        public Heap right() {
            return right;
        }

        @Override
        public T data() {
            return data;
        }

        @Override
        public int rank() {
            return rank;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
        
        @Override
        public int size(){
            return 1 + left.size() + right.size();
        }
    }

    public static final Heap<? extends Object> EMPTY = new Heap<Object>() {

        @Override
        public Heap left() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Heap right() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object data() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int rank() {
            return 0;
        }

        @Override
        public String toString() {
            return "";
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
        
        @Override
        public int size(){
            return 0;
        }
    };

    /* retourn EMPTY instnace (singleton pattern) */
    public static <T> Heap<T> emptyHeap() {
        return (Heap) EMPTY;
    }
    
    /* build a heap v1 */
    public static <T extends Comparable<? super T>> Heap<T> heap(T x, Heap<T> a, Heap<T> b) {
        if (a.rank() > b.rank()) {
            return new NonEmptyHeap<>(b.rank() + 1, x, a, b);
        }
        return new NonEmptyHeap<>(a.rank() + 1, x, b, a);
    }

    /* build a heap v2 */
    public static <T extends Comparable<? super T>> Heap<T> heap(T x) {
        return heap(x, emptyHeap(), emptyHeap());
    }

    /* insert an element into a heap */
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
    
    /* create a balanced heap from an array */
    public static <T extends Comparable<? super T>> Heap<T> insert(T... array) {
        Heap<T> root = emptyHeap();
        for (T item : array) {
            root = insert(root, item);
        }
        return root;
    }
    
    /* merge two balanced heaps into one balanced heap*/
    public static <T extends Comparable<? super T>> Heap<T> merge(Heap<T> a, Heap<T> b) {
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
    
    /* merge a key + two balanced heaps into one balanced heap*/
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
        if (a.data().compareTo(b.data()) <= 0) {
            return heap(a.data(), merge(x, a.left(), a.right()), b);
        }
        return heap(b.data(), merge(x, b.left(), b.right()), a);
    }
    
    /* return the minimum key of a heap */
    public static <T extends Comparable<? super T>> T min(Heap<T> root) {
        if (root == null) {
            return null;
        }
        return  root.data();
    }
    
    /* remove the minimum key of a heap */
    public static <T extends Comparable<? super T>> Heap<T> delete(Heap<T> root) {
        if (root == null) {
            return null;
        }
        return merge(root.left(), root.right());
    }

    /* print heap element */
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
        System.out.println("------------ Balanced Heap 1 ------------");
        printNice(heap1, 1);
        Heap<Integer> heap3 = insert(1, 8, 2);
        System.out.println("------------ Balanced Heap 2 ------------");
        printNice(heap3, 1);
        System.out.println("------------ Merge Heap 1 & Heap 2 ------------");
        printNice(merge(heap1, heap3), 1);
    }
}
