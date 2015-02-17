package HuffmanFunctionalProgramming;

import List.ListModule;
import List.ListModule.List;

public interface CodeTree extends Comparable<CodeTree>{

    boolean isLeaf();

    Paire paire();
    // need to reurn the character and ramainig bits too
    CharBits getChar(List<Integer> bits);

    default List<Character> decode(List<Integer> bits, List<Character> acc) {
        if (bits.isEmpty()) {
            return acc;
        }
        CharBits tmp = getChar(bits);
        acc = ListModule.list(tmp.car, acc);
        return decode(tmp.bits, acc);
    }
    
    List<Integer> getCode(Character car, List<Integer> acc);
    List<Integer> encode(List<Character> chars, List<Integer> acc);

}
