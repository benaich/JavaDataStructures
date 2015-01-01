package Huffman;

import Heap.LeftistModule.Leftist;
import Heap.LeftistModule;
import datastructures.ListModule;
import datastructures.ListModule.List;

public class Fork implements CodeTree {

    private Paire key;
    private CodeTree left;
    private CodeTree right;

    public Fork(Paire key, CodeTree left, CodeTree right) {
        this.key = Paire.merge(left.paire(), right.paire());
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public Paire paire() {
        return key;
    }

    public int compareTo(CodeTree n) {
        return paire().compareTo(n.paire());
    }

    @Override
    public CharBits getChar(List<Integer> bits) {
        if (bits.head() == 0) {
            return left.getChar(bits.tail());
        }
        return right.getChar(bits.tail());
    }

    @Override
    public List<Integer> getCode(Character car, List<Integer> acc) {
        if (ListModule.contain(car, left.paire().getChars())) {
            return ListModule.list(0, left.getCode(car, acc));
        }
        return ListModule.list(1, right.getCode(car, acc));
    }

    @Override
    public List<Integer> encode(List<Character> chars, List<Integer> acc) {
        if (chars.isEmpty()) {
            return acc;
        }
        acc = ListModule.concat(getCode(chars.head(), ListModule.emptyList()), acc);
        return encode(chars.tail(), acc);
    }

    public static CodeTree makeCodeTreeFromText(List<Character> text) {
        return combineCodeTrees(makeLeftistFromFreqs(countFrequency(text, ListModule.emptyList()))).min();
    }

    public static List<Paire> countFrequency(List<Character> chars, List<Paire> acc) {
        if (chars.isEmpty()) {
            return acc;
        }
        if (!acc.reduce(false, (seed, head) -> head.getChars().head().equals(chars.head()))) {
            return countFrequency(chars.tail(),
                    ListModule.list(new Paire(1, ListModule.list(chars.head())), acc));
        }
        return countFrequency(chars.tail(),
                ListModule.list(new Paire(acc.head().getFr() + 1, acc.head().getChars()), acc.tail()));
    }

    public static Leftist<CodeTree> combineCodeTrees(Leftist<CodeTree> p) {
        if (p.isEmpty()) {
            return null;
        }
        if (p.size() == 1) {
            return p;
        }
        CodeTree x = p.min();
        p = p.delete();
        CodeTree y = p.min();
        p = p.delete();
        return combineCodeTrees(p.insert(new Fork(new Paire(x.paire().getFr() + y.paire().getFr()), x, y)));
    }

    public static Leftist<CodeTree> makeLeftistFromFreqs(List<Paire> frequency) {
        if (frequency.isEmpty()) {
            return LeftistModule.emptyLeftist();
        }
        return LeftistModule.insert(makeLeftistFromFreqs(frequency.tail()), new Leaf(frequency.head()));
    }

    @Override
    public String toString() {
        return " (" + left + ", " + right + ")";
    }


    

}
