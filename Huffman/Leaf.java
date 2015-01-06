/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Huffman;

import List.ListModule;
import List.ListModule.List;

/**
 *
 * @author home
 */
public class Leaf implements CodeTree {

    private Paire key;

    public Leaf(Paire key) {
        this.key = key;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public Paire paire() {
        return key;
    }

    @Override
    public CharBits getChar(List<Integer> bits) {
        return new CharBits(bits, paire().getChars().head());
    }

    @Override
    public List<Integer> getCode(Character car, List<Integer> acc) {
        return acc;
    }

    @Override
    public List<Integer> encode(List<Character> chars, List<Integer> acc) {
        return acc;
    }

    @Override
    public int compareTo(CodeTree n) {
        return key.compareTo(n.paire());
    }

    @Override
    public String toString() {
        return key.toString();
    }

}
