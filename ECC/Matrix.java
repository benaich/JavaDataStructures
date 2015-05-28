package ECC;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Matrix {

    private final String[][] data;
    private final int rows;
    private final int columns;

    public Matrix(String[][] data) {
        this.data = data;
        this.rows = data.length;
        this.columns = data[0].length;
    }

    public int countRows() {
        return rows;
    }

    public int countColumns() {
        return columns;
    }

    public String[][] getData() {
        return data;
    }

    public String getData(int r, int c) {
        return data[r][c];
    }

    public static Matrix make(String[][] data) {
        return new Matrix(data);
    }

    public static Matrix make(int[] array) {
        int keyLength = 2 * ECC.PAD;
        Integer[] data = new Integer[(array.length - keyLength)];
        // get the matrix
        for (int i = 0; i < data.length; i++) {
            data[i] = array[i + keyLength];
        }
        Matrix M = Helpers.listToMatrix(Arrays.asList(data));

        return M;
    }

    public void scramble(boolean type) {
        int n, m, row1, row2, col1, col2, x1, x2, op;
        n = data[0].length;
        m = data.length;
        row1 = ECC.getRandom().nextInt(m);
        row2 = Helpers.getNotEqualTo(row1, m);
        col1 = ECC.getRandom().nextInt(n);
        col2 = Helpers.getNotEqualTo(col1, m);
        op = new BigInteger(2, ECC.getRandom()).intValue();     //operations:
        if (type) {
            x1 = Integer.min(col1, col2); // first index
            x2 = Integer.max(col1, col2); // last index
            rowTrasformation(row1, x1, x2, op);
            rowTrasformation(row2, x1, x2, op);
            log("R", op, row1, row2, x1, x2);
        } else {
            x1 = Integer.min(row1, row2);
            x2 = Integer.max(row1, row2);
            columnTrasformation(col1, x1, x2, op);
            columnTrasformation(col2, x1, x2, op);
            log("C", op, col1, col2, x1, x2);
        }
    }

    public void rowTrasformation(int r1, int x1, int x2, int op) {
        if (op == 0) {
            circularLeftShift(r1, x1, x2);
        } else if (op == 1) {
            circularRightShift(r1, x1, x2);
        } else {
            reverseRow(r1, x1, x2);
        }
    }

    public void columnTrasformation(int c1, int x1, int x2, int op) {
        if (op == 0) {
            circularUpwardShift(c1, x1, x2);
        } else if (op == 1) {
            circularDownwardShift(c1, x1, x2);
        } else {
            reverseColumn(c1, x1, x2);
        }
    }

    public void reverseRowTrasformation(int r1, int x1, int x2, int op) {
        if (op == 0) {
            op = 1;
        } else if (op == 1) {
            op = 0;
        }
        rowTrasformation(r1, x1, x2, op);
    }

    public void reverseColumnTrasformation(int c1, int x1, int x2, int op) {
        if (op == 0) {
            op = 1;
        } else if (op == 1) {
            op = 0;
        }
        columnTrasformation(c1, x1, x2, op);
    }

    public void circularLeftShift(int row, int c1, int c2) {
        String tmp = data[row][c1];
        for (int i = c1; i < c2; i++) {
            data[row][i] = data[row][i + 1];
        }
        data[row][c2] = tmp;
    }

    public void circularRightShift(int row, int c1, int c2) {
        String tmp = data[row][c2];
        for (int i = c2; i > c1; i--) {
            data[row][i] = data[row][i - 1];
        }
        data[row][c1] = tmp;
    }

    public void reverseRow(int row, int c1, int c2) {
        String tmp;
        while (c1 < c2) {
            tmp = data[row][c1];
            data[row][c1] = data[row][c2];
            data[row][c2] = tmp;
            c1++;
            c2--;
        }
    }

    public void circularUpwardShift(int col, int r1, int r2) {
        String tmp = data[r1][col];
        for (int i = r1; i < r2; i++) {
            data[i][col] = data[i + 1][col];
        }
        data[r2][col] = tmp;
    }

    public void circularDownwardShift(int col, int r1, int r2) {
        String tmp = data[r2][col];
        for (int i = r2; i > r1; i--) {
            data[i][col] = data[i - 1][col];
        }
        data[r1][col] = tmp;
    }

    public void reverseColumn(int col, int r1, int r2) {
        String tmp;
        while (r1 < r2) {
            tmp = data[r1][col];
            data[r1][col] = data[r2][col];
            data[r2][col] = tmp;
            r1++;
            r2--;
        }
    }

    public static String[][] additionCode = new String[][]{
        {"01", "10", "11", "00"},
        {"10", "11", "00", "01"},
        {"11", "00", "01", "10"},
        {"00", "01", "10", "11"}
    };
    public static String[][] subsractionCode = new String[][]{
        {"11", "10", "01", "00"},
        {"00", "11", "10", "01"},
        {"01", "00", "11", "10"},
        {"10", "01", "00", "11"}
    };

    public void performAddition(String[] key) {
        performOperation(key, true);
    }

    public void performSubstraction(String[] key) {
        performOperation(key, false);
    }

    public void performOperation(String[] key, boolean op) {
        int r, c;
        String[][] operationCode = (op) ? additionCode : subsractionCode;
        for (int i = 0; i < countColumns(); i++) {
            for (int j = 0; j < countRows(); j++) {
                r = Integer.parseInt(data[j][i], 2);
                c = Integer.parseInt(key[j], 2);
                data[j][i] = operationCode[r][c];
            }
        }
    }

    public int[] toArray(String[] key) {
        // return array of key + data;
        int k, keySize = key.length * 2;
        int[] array = new int[countColumns() * countRows() * 2 + keySize];
        //append the key first
        for (k = 0; k < keySize; k = k + 2) {
            String str = key[k / 2];
            array[k] = Integer.parseInt(str.charAt(0) + "");
            array[k + 1] = Integer.parseInt(str.charAt(1) + "");
        }
        // then add the data
        for (int i = 0; i < countColumns(); i++) {
            for (int j = 0; j < countRows(); j++) {
                array[k] = Integer.parseInt(data[j][i].charAt(0) + "");
                array[k + 1] = Integer.parseInt(data[j][i].charAt(1) + "");
                k = k + 2;
            }
        }
        return array;
    }

    public List<Point> toPoints() {
        List<Point> list = new ArrayList<>();
        for (int i = 0; i < countColumns(); i++) {
            String pointCode = "";
            for (int j = 0; j < countRows(); j++) {
                pointCode += data[j][i];
            }
            int x = Integer.parseInt(pointCode.substring(0, pointCode.length() / 2), 2);
            int y = Integer.parseInt(pointCode.substring(pointCode.length() / 2), 2);
            if (x == 0 && y == 0) {
                list.add(Point.getInfinity());
            } else {
                list.add(new Point(x, y));
            }
        }
        return list;
    }

    @Override
    public String toString() {
        StringBuilder sbResult = new StringBuilder();

        for (int i = 0; i < countRows(); i++) {
            for (int j = 0; j < countColumns(); j++) {
                sbResult.append(data[i][j]);
                sbResult.append("\t");
            }
            sbResult.append("\n");
        }

        return sbResult.toString();
    }

    public void log(String t, int op, int a, int b, int c, int d) {
        System.out.println("subkey : " + t + "/" + op + "/" + a + "/" + b + "/" + c + "/" + d);
        Helpers.saveSubKey(t, op, a, b, c, d);
    }
}
