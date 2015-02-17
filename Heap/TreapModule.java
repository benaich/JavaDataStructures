package Heap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TreapModule {

    public static final Random r = new Random();

    public static interface Treap<T> {

        Treap left();

        Treap right();

        T data();

        int rank();

        boolean isEmpty();

        int size();

        @Override
        String toString();

        public T min();
    }

    public static class NonEmptyTreap<T> implements Treap<T> {

        public Treap left;
        public Treap right;
        public T data;
        public int rank;

        public NonEmptyTreap(T x, Treap a, Treap b) {
            this(r.nextInt(20), x, a, b);
        }

        public NonEmptyTreap(int r, T x, Treap a, Treap b) {
            rank = r;
            data = x;
            left = a;
            right = b;
        }

        @Override
        public Treap left() {
            return left;
        }

        @Override
        public Treap right() {
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
        public T min() {
            if (left().isEmpty()) {
                return data();
            }
            return (T) left.min();
        }

        @Override
        public String toString() {
            return data + "-" + rank;
        }
    }

    /**
     *
     */
    public static final Treap<? extends Object> EMPTY = new Treap<Object>() {

        @Override
        public Treap left() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Treap right() {
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
        public int size() {
            return 0;
        }

        @Override
        public Object min() {
            return null;
        }
    };

    /* retourn EMPTY instnace (singleton pattern) */
    public static <T> Treap emptyTreap() {
        return (Treap) EMPTY;
    }

    /* build a Treap v1 */
    public static <T> Treap<T> treap(T x, Treap<T> a, Treap<T> b) {
        return new NonEmptyTreap<>(x, a, b);
    }
    /* build a Treap v2 */

    public static <T> Treap<T> treap(int rank, T x, Treap<T> a, Treap<T> b) {
        return new NonEmptyTreap<>(rank, x, a, b);
    }

    /* insert an element into a Treap without rotation*/
    public static <T extends Comparable<? super T>> Treap<T> insertNoFix(Treap<T> a, T x) {
        if (a.isEmpty()) {
            return treap(x, emptyTreap(), emptyTreap());
        }
        if (x.compareTo(a.data()) < 0) {
            return treap(a.data(), insertNoFix(a.left(), x), a.right());
        }
        return treap(a.data(), a.left(), insertNoFix(a.right(), x));
    }

    public static <T extends Comparable<? super T>> Treap<T> insertNoFix(Treap<T> a, T x, int rank) {
        if (a.isEmpty()) {
            return treap(rank, x, emptyTreap(), emptyTreap());
        }
        if (x.compareTo(a.data()) < 0) {
            return treap(a.rank(), a.data(), insertNoFix(a.left(), x, rank), a.right());
        }
        return treap(a.rank(), a.data(), a.left(), insertNoFix(a.right(), x, rank));
    }

    /* insert (rotate if needed) an element into a Treap */
    public static <T extends Comparable<? super T>> Treap<T> insert(Treap<T> a, T x) {
        return rotate(insertNoFix(a, x));
    }

    public static <T extends Comparable<? super T>> Treap<T> insert(Treap<T> a, T x, int rank) {
        return rotate(insertNoFix(a, x, rank));
    }

    /* Rotate to fix the Treap */
    public static <T> Treap<T> rotate(Treap<T> root) {
        if (root.isEmpty()) {
            return root;
        }
        Treap<T> left = rotate(root.left());
        Treap<T> right = rotate(root.right());

        root = treap(root.rank(), root.data(), left, right);

        if (!root.left().isEmpty()) {
            if (root.rank() > root.left().rank()) {
                return rotate(rotateRight(root));
            }
        }
        if (!root.right().isEmpty()) {
            if (root.rank() > root.right().rank()) {
                return rotate(rotateLeft(root));
            }
        }
        return treap(root.rank(), root.data(), root.left(), root.right());
    }

    private static <T> Treap<T> rotateRight(Treap<T> root) {
        return treap(root.left().rank(),
                root.left().data(),
                merge(root.left().left(), root.left().right()),
                treap(root.rank(), root.data(), emptyTreap(), root.right()));
    }

    private static <T> Treap<T> rotateLeft(Treap<T> root) {
        return treap(root.right().rank(),
                root.right().data(),
                treap(root.rank(), root.data(), root.left(), emptyTreap()),
                merge(root.right().right(), root.right().left())
        );
    }

    public static <T extends Comparable<? super T>> boolean contains(Treap<T> root, T x) {
        if (root.isEmpty()) {
            return false;
        }
        int c = root.data().compareTo(x);
        if (c == 0) {
            return true;
        } else if (c < 0) {
            return contains(root.right(), x);
        }
        return contains(root.left(), x);
    }

    /* create a balanced Treap from an array */
    public static <T extends Comparable<? super T>> Treap<T> insert(T... array) {
        Treap<T> root = emptyTreap();
        for (T item : array) {
            root = insert(root, item);
        }
        return root;
    }

    /* merge two balanced Treaps into one balanced Treap*/
    public static <T extends Comparable<? super T>> Treap<T> merge(Treap<T> a, Treap<T> b) {
        if (a.isEmpty()) {
            return b;
        }
        if (b.isEmpty()) {
            return a;
        }
        if (a.size() > b.size()) {
            return merge(insert(a, b.data(), b.rank()), merge(b.left(), b.right()));
        }
        return merge(insert(b, a.data(), a.rank()), merge(a.left(), a.right()));
    }

    /* remove the minimum key of a Treap */
    public static <T extends Comparable<? super T>> Treap<T> delete(Treap<T> root, T x) {
        if (!contains(root, x)) {
            return root;
        }
        int c = root.data().compareTo(x);
        if (c == 0) {
            return merge(root.left(), root.right());
        }
        if (c > 0) {
            return treap(root.rank(), root.data(), delete(root.left(), x), root.right());
        }
        return treap(root.rank(), root.data(), root.left(), delete(root.right(), x));
    }

    /* split a treap */
    public static <T extends Comparable<? super T>> List<Treap<T>> split(Treap<T> a, T x, List<Treap<T>> seed) {
        if (seed == null || seed.isEmpty()) {
            seed = new ArrayList<>();
            seed.add(emptyTreap());
            seed.add(emptyTreap());
        }
        if(a.isEmpty())return seed;
        if (a.min().compareTo(x) > 0) {
            seed.set(0, a);
            return seed;
        }
        int c = a.data().compareTo(x);
        if (c < 0) {
            seed.set(0,
                    merge(seed.get(0), treap(a.rank(), a.data(), a.left(), emptyTreap())));
            return split(a.right(), x, seed);
        }
        seed.set(1,
                merge(seed.get(1), treap(a.rank(), a.data(), emptyTreap(), a.right())));
        return split(a.left(), x, seed);
    }

    public static <T extends Comparable<? super T>> List<Treap<T>> split2(Treap<T> a, T x, T y) {
        List<Treap<T>> seed = new ArrayList<>();
        seed.add(emptyTreap());
        seed.add(emptyTreap());
        seed.add(emptyTreap());

        List<Treap<T>> seed1 = split(a, x, null);
        List<Treap<T>> seed2 = split(seed1.get(1), y, null);
        seed.set(0, seed1.get(0));
        seed.set(1, seed2.get(0));
        seed.set(2, seed2.get(1));
        return seed;
    }

    /* print Treap element */
    public static <T> void printNice(Treap<T> root, int level) {
        if (root.isEmpty()) {
            return;
        }
        printNice(root.right(), level + 1);
        if (level != 0) {
            for (int i = 0; i < level - 1; i++) {
                System.out.print("|   ");
            }
            System.out.println("|---" + root);
        } else {
            System.out.println(root);
        }
        printNice(root.left(), level + 1);
    }

    public static void main(String[] args) {

        Treap<Integer> a = emptyTreap();
        Treap<Integer> b = emptyTreap();
        Treap<Integer> c = emptyTreap();

        a = insertNoFix(a, 5, 16);
        a = insertNoFix(a, 1, 12);
        a = insertNoFix(a, 2, 10);
        a = insertNoFix(a, 8, 8);
        a = insertNoFix(a, 6, 3);
        System.out.println("------------ insert ------------");
        printNice(a, 1);

        System.out.println("------------ rotate ------------");
        b = rotate(a);
        printNice(b, 1);

        System.out.println("------------ contains 8 ------------");
        if (contains(b, 8)) {
            System.out.println("yes");
        } else {
            System.out.println("no");
        }

        System.out.println("------------ min ------------");
        System.out.println(b.min());

        System.out.println("------------ delete 2 ------------");
        c = delete(b, 2);
        printNice(c, 1);

        System.out.println("------------ Split 3 ------------");
        List<Treap<Integer>> list1 = split(b, 3, null);
        System.out.println("list 1");
        printNice(list1.get(0), 1);
        System.out.println("list 2");
        printNice(list1.get(1), 1);

        System.out.println("------------ Split 3 & 8 ------------");
        List<Treap<Integer>> list2 = split2(b, 3, 8);
        System.out.println("list 1");
        printNice(list2.get(0), 1);
        System.out.println("list 2");
        printNice(list2.get(1), 1);
        System.out.println("list 3");
        printNice(list2.get(2), 1);


    }
}
