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
                A.reverseRowTrasformation(a, c, d, op);
                A.reverseRowTrasformation(b, c, d, op);
            } else {
                A.reverseColumnTrasformation(a, c, d, op);
                A.reverseColumnTrasformation(b, c, d, op);
            }
            System.out.println("sub-key : " + subKey);
            System.out.println(A);
        }
        return getPlainText(A);
    }

    private String getPlainText(Matrix A) {
        String plaintText = "";
        System.out.println("4) Convert the matrix M to a list of points");
        A.toPoints().stream().forEach(System.out::print);
        System.out.println("");
        
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
