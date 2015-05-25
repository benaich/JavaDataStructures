package ECC;

import static ECC.Helpers.toBinary;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Encoder {
    private HashMap<Integer, Point> charTable;
    
    public Encoder(HashMap<Integer, Point> charTable) {
        this.charTable = charTable;
    }
    
    public Matrix encode(String plainText) {
        Matrix mMatrix = createMatrix(plainText);
        System.out.println("Matrix before scrambling : ");
        System.out.println(mMatrix);
        //int w = new BigInteger(ECC.PAD, ECC.getRandom()).intValue();
        int w = 6;
        int[] bits = Helpers.toBinary(ECC.getRandom().nextInt(1024), ECC.PAD * 2);
        System.out.println("Bits : ");
        Helpers.print(bits);
        int bit, i = 0;
        do {
            bit = bits[i];
            if (bit == 0) {
                mMatrix.scramble(true);
            } else {
                mMatrix.scramble(false);
            }
            if (i == bits.length) {
                i = 0;
            }
            System.out.println("scrambling...");
            System.out.println(mMatrix);
            w--;
        } while (w > 0);
        return mMatrix;
    }

    private Matrix createMatrix(String plainText) {
        List<Point> pList = new ArrayList<>();
        for (Character c : plainText.toCharArray()) {
            Point p = charTable.get((int)c.charValue());
            pList.add(p);
        }
        System.out.print("List of Points : ");
        pList.stream().forEach(System.out::print);
        System.out.println("");
        List<Integer> bList = new ArrayList<>();
        for (Point p : pList) {
            String str = toBinary(p.getX()) + "" + toBinary(p.getY());
            for (int i = 0; i < str.length(); i++) {
                bList.add((str.charAt(i) == '0') ? 0 : 1);
            }
        }
        return Helpers.listToMatrix(bList);
    }

}
