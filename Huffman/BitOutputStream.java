package Huffman;

import java.io.IOException;
import java.io.OutputStream;

/* BitOutputStream using decorator pattern */
public final class BitOutputStream {

    private OutputStream output;
    private int currentByte;
    private int numBitsInCurrentByte;

    public BitOutputStream(OutputStream out) {
        if (out == null) {
            throw new NullPointerException("Argument is null");
        }
        output = out;
        currentByte = 0;
        numBitsInCurrentByte = 0;
    }

    public void write(int b) throws IOException {
        if (!(b == 0 || b == 1)) {
            throw new IllegalArgumentException("Argument must be 0 or 1");
        }
        currentByte = currentByte << 1 | b;
        numBitsInCurrentByte++;
        if (numBitsInCurrentByte == 8) {
            output.write(currentByte);
            numBitsInCurrentByte = 0;
        }
    }

    public void write(String s) throws IOException {
        for (int i = 0; i < s.length(); i++) {
            write(Character.getNumericValue(s.charAt(i)));
        }
    }

    public void close() throws IOException {
        while (numBitsInCurrentByte != 0) {
            write(0);
        }
        output.close();
    }

}
