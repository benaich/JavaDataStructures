package HuffmanCode;

import java.io.IOException;
import java.io.InputStream;

public final class BitInputStream {
	private InputStream input;
	private int nextBits;
	private int numBitsRemaining;
	private boolean isEndOfStream;
        
	public BitInputStream(InputStream in) {
		input = in;
		numBitsRemaining = 0;
		isEndOfStream = false;
	}
	
	public int read() throws IOException {
		if (isEndOfStream)
			return -1;
		if (numBitsRemaining == 0) {
			nextBits = input.read();
			if (nextBits == -1) {
				isEndOfStream = true;
				return -1;
			}
			numBitsRemaining = 8;
		}
		numBitsRemaining--;
		return (nextBits >>> numBitsRemaining) & 1;
	}
        
	public void close() throws IOException {
		input.close();
	}
	
}
