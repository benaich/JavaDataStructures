package Huffman;

import datastructures.ListModule;
import datastructures.ListModule.List;
import java.io.Serializable;

public class Paire implements Comparable<Paire>, Serializable {

    private int fr;
    private List<Character> chars;

    public Paire(int fr, List<Character> chars) {
        this.fr = fr;
        this.chars = chars;
    }

    public int getFr() {
        return fr;
    }

    public List<Character> getChars() {
        return chars;
    }

    public Paire(int fr) {
        this.fr = fr;
    }

    public int compareTo(Paire p) {
        if (fr > p.fr) {
            return 1;
        } else if (fr < p.fr) {
            return -1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "("+chars+fr+")";
    }
    
    public static Paire merge(Paire a, Paire b){
        return new Paire(a.getFr()+b.getFr(), ListModule.merge(a.getChars(), b.getChars()));
    }
    
    
}
