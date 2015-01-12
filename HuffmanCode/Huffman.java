package HuffmanCode;

import Heap.LeftistModule;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Huffman{

    int[] occ = new int[256];
    Node aC;
    LeftistModule.Leftist<Node> tas = LeftistModule.emptyLeftist();
    String[] code = new String[256];

    public void encodeMessage(File in, File out) {
        countFrequency(in);
        buildLeftist();
        buildCodeTree();
        generateCodes();

        try {
            FileReader fr = new FileReader(in);
            BitOutputStream fw = new BitOutputStream(new FileOutputStream(out));
            int lu;
            while ((lu = fr.read()) != -1) {
                fw.write(code[lu]);
            }
            serializeTree();
            fw.close();
            fr.close();
        } catch (IOException e) {
        }
    }

    public void decodeMessage(File in, File out) {
        deserializeTree();
        try {
            BitInputStream fr = new BitInputStream(new FileInputStream(in));
            FileWriter fw = new FileWriter(out, true);
            int lu, count = aC.p.fr;
            Node tmp = aC;
            while ((lu = fr.read()) != -1 && count > 0) {
                if (tmp.isLeaf()) {
                    fw.append((char) tmp.p.car);
                    count--;
                    tmp = aC;
                }

                if (lu == 0) {
                    tmp = tmp.left;
                } else {
                    tmp = tmp.right;
                }

            }

            fw.close();
            fr.close();
        } catch (Exception ex) {

        }
    }

    public void countFrequency(File in) {
        try {
            FileReader fr = new FileReader(in);
            int lu;
            while (-1 != (lu = fr.read())) {
                occ[lu]++;
            }
            fr.close();
        } catch (IOException e) {
            System.out.println("file not found");
        }
    }

    public void buildLeftist() {
        for (int i = 0; i < occ.length; i++) {
            if (occ[i] != 0) {
                tas = tas.insert(new Node(new Paire(occ[i], i)));
            }
        }
    }

    public void buildCodeTree() {
        while (tas.size() > 1) {
            Node x = tas.min();
            tas = tas.delete();
            Node y = tas.min();
            tas = tas.delete();
            aC = Node.build(new Paire(x.p.fr + y.p.fr), x, y);
            tas = tas.insert(aC);
        }
        aC = tas.min();
    }

    public void generateCodes() {
        construire(aC, "");
    }

    public void construire(Node x, String s) {
        if (x.isLeaf()) {
            this.code[x.p.car] = s;
        } else {
            construire(x.left, s + "0");
            construire(x.right, s + "1");
        }
    }

    public void serializeTree() {
        try {
            Node.serialaze(aC, "src/data/tree");
        } catch (IOException ex) {
        }
    }

    public void deserializeTree() {
        try {
            aC = Node.deserialaze("src/HuffmanCode/data/tree");
        } catch (IOException ex) {
        }
    }

    public static void main(String[] args) {
        Huffman hf = new Huffman();

        File in = new File("src/HuffmanCode/data/plain");
        File out = new File("src/HuffmanCode/data/code");

        hf.encodeMessage(in, out);
        //hf.decodeMessage(out, in);
    }
}
