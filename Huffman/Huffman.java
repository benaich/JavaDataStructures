package Huffman;

import List.ListModule;
import List.ListModule.List;

public class Huffman {

    public static void main(String[] args) {
        String text = "There is no patch ...";
        List<Character> ascii = ListModule.emptyList();
        for (int i = 0; i < text.length(); i++) {
            ascii = ListModule.list(text.charAt(i), ascii);
        }
        CodeTree cTree = Fork.makeCodeTreeFromText(ascii);
        System.out.println("-------- CodeTree --------");
        System.out.println(cTree);
        
        List<Integer> code = cTree.encode(ascii, ListModule.emptyList());
        System.out.println("-------- Binnary Code --------");
        System.out.println(code);
        
        List<Character> plain = cTree.decode(code, ListModule.emptyList());
        System.out.println("-------- Plain Text --------");
        String result = plain.reduce("", (x, y) -> y + x);
        System.out.println(result);
    }
}
