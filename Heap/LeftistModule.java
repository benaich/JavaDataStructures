package Heap;

public class LeftistModule {

    public static interface Leftist<T extends Comparable<? super T>> {

        Leftist left();

        Leftist right();

        T data();

        int rank();

        boolean isEmpty();

        int size();

        Leftist<T> merge(Leftist<T> h);

        T min();

        Leftist<T> delete();

        default Leftist<T> insert(T t) {
            return merge(heap(t));
        }

        default Leftist<T> insert(T... array) {
            Leftist<T> root = emptyLeftist();
            for (T item : array) {
                root = root.merge(heap(item));
            }
            return root;
        }
    }

    public static class NonEmptyLeftist<T extends Comparable<? super T>> implements Leftist<T> {

        public Leftist left;
        public Leftist right;
        public T data;
        public int rank;

        public NonEmptyLeftist(T x, Leftist a, Leftist b) {
            this(0, x, a, b);
        }

        public NonEmptyLeftist(int r, T x, Leftist a, Leftist b) {
            rank = r;
            data = x;
            left = a;
            right = b;
        }

        @Override
        public Leftist left() {
            return left;
        }

        @Override
        public Leftist right() {
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
        public int size() {
            return 1 + left.size() + right.size();
        }

        @Override
        public Leftist<T> merge(Leftist<T> h) {
            if (h.isEmpty()) {
                return this;
            }
            if (data.compareTo(h.data()) <= 0) {
                return heap(data, left, right.merge(h));
            }
            return heap(h.data(), h.left(), h.right().merge(this));
        }

        public T min() {
            return data;
        }

        public Leftist<T> delete() {
            return left.merge(right);
        }
    }

    public static class Empty<T extends Comparable<? super T>> implements Leftist<T> {

        static protected final Empty empty = new Empty();

        @Override
        public Leftist left() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Leftist right() {
            throw new UnsupportedOperationException();
        }

        @Override
        public T data() {
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
        public int size() {
            return 0;
        }

        @Override
        public Leftist<T> merge(Leftist<T> h) {
            return h;
        }

        public T min() {
            throw new UnsupportedOperationException();
        }

        public Leftist<T> delete() {
            throw new UnsupportedOperationException();
        }
    };

    /* retourn EMPTY instnace (singleton pattern) */
    public static <T extends Comparable<? super T>> Leftist<T> emptyLeftist() {
        return (Leftist) Empty.empty;
    }

    /* build a leftist heap v1 */
    public static <T extends Comparable<? super T>> Leftist<T> heap(T x, Leftist<T> a, Leftist<T> b) {
        if (a.rank() > b.rank()) {
            return new NonEmptyLeftist<>(b.rank() + 1, x, a, b);
        }
        return new NonEmptyLeftist<>(a.rank() + 1, x, b, a);
    }

    /* build a leftist heap v2 */
    public static <T extends Comparable<? super T>> Leftist<T> heap(T x) {
        return heap(x, emptyLeftist(), emptyLeftist());
    }
    
    /* used by HuffmanFunctionalProgramming package */
    public static <T extends Comparable<? super T>> Leftist<T> insert(Leftist<T> p, T val) {
        return p.insert(val);
    }

    /* print heap element */
    public static <T extends Comparable<? super T>> void printNice(Leftist<T> root, int level) {
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

        Leftist<Integer> p1 = emptyLeftist();
        Leftist<Integer> p2 = emptyLeftist();
        
        p1 = p1.insert(2, 3, 4, 1, 2);
        System.out.println("----------- Leftits Heap 1 -----------");
        printNice(p1, 1);
        System.out.println("----------- delete -----------");
        p1 = p1.delete();
        printNice(p1, 1);
        
        p2 = p2.insert(5, 9, 3, 1, 2, 15, 22);
        System.out.println("----------- Leftits Heap 1 -----------");
        printNice(p2, 1);
        
        Leftist<Integer> p3 = p1.merge(p2);
        System.out.println("----------- merge Heap 1 & Heap 2 -----------");
        printNice(p3, 1);
        
        
    }
}
