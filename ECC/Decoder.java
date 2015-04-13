package ECC;

import java.util.HashMap;
import java.util.List;

public class Decoder {

    public static int PAD = 5;
    private HashMap<Point, Integer> pointTable;

    public Decoder(HashMap<Point, Integer> pointTable) {
        this.pointTable = pointTable;
    }

    public String decode(Matrix A) {
        List<String> subKeys = Helpers.getSubKeys();
        for (String subKey : subKeys) {
            String[] array = subKey.split("\\/");
            String transformation = array[0];
            int op = Integer.parseInt(array[1]);
            int a = Integer.parseInt(array[2]);
            int b = Integer.parseInt(array[3]);
            int c = Integer.parseInt(array[4]);
            int d = Integer.parseInt(array[5]);
            if (transformation.equals("R")) {
                //System.out.println("subkey : R/" + op + "/" + a + "/" + b + "/" + c + "/" + d);
                A.reverseRowTrasformation(a, c, d, op);
                A.reverseRowTrasformation(b, c, d, op);
            } else {
                //System.out.println("subkey : C/" + op + "/" + a + "/" + b + "/" + c + "/" + d);
                A.reverseColumnTrasformation(a, c, d, op);
                A.reverseColumnTrasformation(b, c, d, op);
            }
        }
        return getPlainText(A);
    }

    private String getPlainText(Matrix A) {
        String plaintText = "";
        for (Point p : A.toPoints()) {
            if (pointTable.get(p) != null) {
                int asciCode = pointTable.get(p);
                plaintText += Character.toString((char) asciCode);
            } else {
                plaintText += "$";
            }
        }
        return plaintText;
    }
}
