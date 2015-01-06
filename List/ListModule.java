package List;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.*;
import java.util.function.Predicate;

public class ListModule {

    public static interface List<T> {

        T head();

        List<T> tail();

        boolean isEmpty();

        default List<T> filter(Predicate<T> p) {
            return emptyList();
        }

        default <R> List<R> map(Function<T, R> f) {
            return emptyList();
        }

        default <V> V reduce(V seed, BiFunction<V, T, V> f) {
            return seed;
        }

        default void forEach(Consumer<T> f) {
        }

    }

    /* Non Empty List class */
    public static final class NonEmptyList<T> implements List<T> {

        private T head;
        private List<T> tail;

        NonEmptyList(T head, List<T> tail) {
            this.head = head;
            this.tail = tail;
        }

        @Override
        public String toString() {
            return head() + " " + tail();
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public T head() {
            return head;
        }

        @Override
        public List<T> tail() {
            return tail;
        }

        @Override
        public List<T> filter(Predicate<T> p) {
            if (p.test((T) head())) {
                return list(head(), (List) tail().filter(p));
            }
            return tail().filter(p);
        }

        @Override
        public <R> List<R> map(Function<T, R> f) {
            return list(f.apply(head()), tail().map(f));
        }

        @Override
        public <V> V reduce(V seed, BiFunction<V, T, V> f) {
            return tail().reduce(f.apply(seed, head()), f);
        }

        @Override
        public void forEach(Consumer<T> f) {
            f.accept(head);
            tail().forEach(f);
        }
    }

    /* Exceptions */
    public static class EmptyListHasNoHead extends RuntimeException {
    }

    public static class EmptyListHasNoTail extends RuntimeException {
    }

    /* empty list class */
    public static final List<? extends Object> EMPTY = new List<Object>() {

        @Override
        public String toString() {
            return "";
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public Object head() {
            throw new EmptyListHasNoHead();
        }

        @Override
        public List tail() {
            throw new EmptyListHasNoTail();
        }
    };

    /* return the EMPTY instance (singleton) */
    public static <T> List<T> emptyList() {
        /* the same instance for all type of lists */
        return (List<T>) EMPTY;
    }

    public static <T> List<T> list(T head, List<T> tail) {
        return new NonEmptyList<>(head, tail);
    }

    public static <T> List<T> list(T head) {
        return new NonEmptyList<>(head, emptyList());
    }

    public static <T> List<T> list(T... array) {
        List<T> p = emptyList();
        for (T item : array) {
            p = list(item, p);
        }
        return p;
    }

    public static class Paire<T> {

        private List<T> first;
        private List<T> second;

        public Paire(List<T> first, List<T> second) {
            this.first = first;
            this.second = second;
        }

        public List<T> getFirst() {
            return first;
        }

        public List<T> getSecond() {
            return second;
        }
    }
    /* merge two sorted list */

    public static <T extends Comparable<? super T>> List<T> merge(List<T> l1, List<T> l2) {
        if (l1.isEmpty()) {
            return l2;
        }
        if (l2.isEmpty()) {
            return l1;
        }

        if (l1.head().compareTo(l2.head()) < 0) {
            return list(l1.head(), merge(l1.tail(), l2));
        }
        return list(l2.head(), merge(l2.tail(), l1));

    }

    // split a list
    public static <T> Paire<T> split(List<T> l) {
        if (l.tail().isEmpty()) {
            return new Paire<>(l, (List<T>) emptyList());
        }
        Paire<T> p = split(l.tail());
        return new Paire<>(list(l.head(), p.getSecond()), p.getFirst());
    }

    // sort a list using merge sort 
    public static <T extends Comparable<? super T>> List<T> mergeSort(List<T> l) {
        if (l.isEmpty() || l.tail().isEmpty()) {
            return l;
        }
        Paire<T> p = split(l);
        return merge(mergeSort(p.getFirst()), mergeSort(p.getSecond()));

    }

    // Returns true if a list contains the specified element.
    public static <T> boolean contain(T data, List<T> l) {
        if (l.isEmpty()) {
            return false;
        }
        if (l.head().equals(data)) {
            return true;
        }
        return contain(data, l.tail());
    }

    /* concatenate two lists */
    public static <T> List<T> concat(List<T> l1, List<T> l2) {
        if (l1.isEmpty()) {
            return l2;
        }
        return list(l1.head(), concat(l1.tail(), l2));
    }

    /* sort by insert */
    public static <T extends Comparable<? super T>> List<T> sort(List<T> l) {
        if (l.isEmpty()) {
            return l;
        }
        return insert(l.head(), sort(l.tail()));
    }

    public static <T extends Comparable<? super T>> List<T> insert(T data, List<T> l) {
        if (l.isEmpty()) {
            return list(data, (List) emptyList());
        }
        if (data.compareTo(l.head()) < 0) {
            return list(data, l);
        }
        return list(l.head(), insert(data, l.tail()));
    }

    public static <T extends Comparable<? super T>> List<T> unique(List<T> l) {
        if (l.isEmpty()) {
            return l;
        }
        if (!contain(l.head(), l.tail())) {
            return list(l.head(), unique(l.tail()));
        }
        return unique(l.tail());
    }

    public static <T> List<T> qSort(List<T> l, Comparator<? super T> c) {
        if (l.isEmpty()) {
            return l;
        }
        List<T> inf = l.tail().filter((x) -> c.compare(x, l.head()) < 0);
        List<T> sup = l.tail().filter((x) -> c.compare(x, l.head()) > 0);
        List<T> rest = l.tail().filter((x) -> c.compare(x, l.head()) == 0);
        return concat(concat(qSort(inf, c), list(l.head(), emptyList())),
                concat(qSort(rest, c), qSort(sup, c)));
    }

    public static <T> Optional<T> min(List<T> l, Comparator<T> c) {
        if (l.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(l.reduce(l.head(), BinaryOperator.minBy(c)));
    }

    public static <T> boolean isSorted(List<T> l, Comparator<T> c) {
        if (l.isEmpty() || l.tail().isEmpty()) {
            return true;
        }
        if (c.compare(l.head(), l.tail().head()) > 0) {
            return false;
        }
        return isSorted(l.tail(), c);
    }

    public static void main(String[] args) {

        List<Integer> l1 = list(1, list(5, list(2, list(8, list(2, emptyList())))));

        System.out.println("--- test1: list ---");
        System.out.println(l1);

        System.out.println("--- test2 : filter ---");
        Predicate<Integer> p = x -> x % 2 == 0;
        System.out.println(l1.filter(p.and(x -> (x > 4)).and(x -> (x != 4))));

        System.out.println("--- test3 : sort ---");
        Comparator<Integer> c = Comparator.comparing(Integer::byteValue);
        c.thenComparing(Comparator.naturalOrder());
        List<Integer> l1_sorted = qSort(l1, c);
        System.out.println(l1_sorted);

        System.out.println("--- test4 : map ---");
        l1.map(Integer::bitCount).forEach(System.out::println);

        System.out.println("--- test5 : reduce min ---");
        Optional<Integer> min = min(l1, Comparator.naturalOrder());
        //Optional<Integer> min = Optional.of(l1.reduce(l1.head(), BinaryOperator.minBy(Comparator.naturalOrder()) ));
        min.ifPresent(System.out::println);

        System.out.println("--- test6 : reduce sum ---");
        Optional<Integer> sum = Optional.of(l1.reduce(0, (x, y) -> x + y));
        sum.ifPresent(System.out::println);

        System.out.println("--- test7 : isSorted ---");
        if (isSorted(l1_sorted, Comparator.naturalOrder())) {
            System.out.println("sorted");
        } else {
            System.out.println("not sorted");
        }

    }
}
