package ECC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Helpers {

    static final File SUB_KEYS_FILE = new File("subkey");

    static int[] toBinary(int n, int base) {
        final int[] bits = new int[base];
        for (int i = 0; i < base; i++) {
            bits[i] = n >> i & 1;
        }
        return bits;
    }

    static String toBinary(BigInteger x) {
        String ret = "";
        for (int b : toBinary(x.intValue(), ECC.PAD)) {
            ret = b + ret;
        }
        return ret;
    }

    static String[] toBinary(Point p) {
        String[] tab = new String[ECC.PAD];
        String str = toBinary(p.getX()) + "" + toBinary(p.getY());
        for (int i = 0; i < str.length(); i = i + 2) {
            tab[i / 2] = str.charAt(i) + "" + str.charAt(i + 1);
        }
        return tab;
    }

    static Matrix listToMatrix(List<Integer> list) {
        int n, m, row, col;
        n = ECC.PAD;
        m = list.size() / (2 * n);
        String[][] bits = new String[n][m];
        for (int i = 0; i < list.size(); i = i + 2) {
            row = i / 2 % n;
            col = i / 2 / n;
            bits[row][col] = list.get(i) + "" + list.get(i + 1);
        }
        return Matrix.make(bits);
    }

    static void saveSubKey(String t, int op, int a, int b, int c, int d) {
        try {
            BufferedWriter writer;
            writer = new BufferedWriter(new FileWriter(SUB_KEYS_FILE, true));
            writer.append(t + "/" + op + "/" + a + "/" + b + "/" + c + "/" + d);
            writer.newLine();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Helpers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static void saveEncryptedMsg(char t, int op, int a, int b, int c, int d) {
        try {
            BufferedWriter writer;
            writer = new BufferedWriter(new FileWriter(SUB_KEYS_FILE, true));
            writer.append(t + "/" + op + "/" + a + "/" + b + "/" + c + "/" + d);
            writer.newLine();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Helpers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static List<String> getSubKeys() {
        List<String> keys = new ArrayList<>();
        try {
            BufferedReader reader;
            reader = new BufferedReader(new FileReader(SUB_KEYS_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                keys.add(line);
            }
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(Helpers.class.getName()).log(Level.SEVERE, null, ex);
        }
        SUB_KEYS_FILE.deleteOnExit();
        Collections.reverse(keys);
        return keys;
    }

    static void print(String[] tab) {
        for (String tab1 : tab) {
            System.out.println(tab1 + " ");
        }
    }

    static void print(int[] tab) {
        byte b;
        for (int i : tab) {
            b = (byte) i;
            System.out.print(b);
        }
        System.out.println("");
    }

    static int getNotEqualTo(int a, int limit) {
        int b;
        do {
            b = ECC.getRandom().nextInt(limit);
        } while (b == a);
        return b;
    }

    static void main(String[] args) {
        for (int a : toBinary(15, 5)) {
            System.out.print(a);
        }
        System.out.println(" :" + Character.toString((char) 32) + ":");
    }
}
