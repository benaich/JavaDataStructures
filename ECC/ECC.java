package ECC;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Random;

public class ECC {

    public static int PAD = 5;
    public static final Random r = new Random();
    
    private HashMap<Point, Integer> pointTable;
    private HashMap<Integer, Point> charTable;

    public static Random getRandom() {
        return r;
    }

    private int[] encrypt(String msg, PublicKey key) {
        EllipticCurve c = key.getCurve();
        Point g = c.getBasePoint();
        Point publicKey = key.getKey();
        BigInteger p = c.getP();
        int numBits = p.bitLength();
        BigInteger k;
        do {
            k = new BigInteger(numBits, getRandom());
        } while (k.mod(p).compareTo(BigInteger.ZERO) == 0);
        Point sharedSecret = c.multiply(publicKey, k);
        Point keyHint = c.multiply(g, k); // key to send

        Encoder encoder = new Encoder(charTable);
        Matrix M = encoder.encode(msg);
        M.performAddition(Helpers.toBinary(sharedSecret));
        return M.toArray(Helpers.toBinary(keyHint));
    }

    private String decrypt(int[] cipherText, PrivateKey key) {
        EllipticCurve c = key.getCurve();
        Point g = c.getBasePoint();
        BigInteger privateKey = key.getKey();
        BigInteger p = c.getP();
        Point recivedPoint = Point.make(cipherText);
        Point sharedSecret = c.multiply(recivedPoint, privateKey);
        //get the decypted matrix
        Matrix M = Matrix.make(cipherText);
        //substract the key form the matrix
        M.performSubstraction(Helpers.toBinary(sharedSecret));
        //decode the matrix
        Decoder decoder = new Decoder(pointTable);
        return decoder.decode(M);
    }

    /**
     * Generate a random key-pair, given the elliptic curve being used.
     */
    public static KeyPair generateKeyPair(EllipticCurve c) {
        // Randomly select the private key, such that it is relatively prime to p
        BigInteger p = c.getP();
        BigInteger privateKey;
        do {
            privateKey = new BigInteger(p.bitLength(), getRandom());
        } while (privateKey.mod(p).compareTo(BigInteger.ZERO) == 0);

        // Calculate the public key, k * g.
        Point g = c.getBasePoint();
        Point publicKey = c.multiply(g, privateKey);

        KeyPair result = new KeyPair(
                new PublicKey(c, publicKey),
                new PrivateKey(c, privateKey)
        );
        return result;
    }
    
    public void initCodeTable(EllipticCurve curve){
        Point p = curve.multiply(curve.getBasePoint(), 2);
        charTable = new HashMap<>();
        pointTable = new HashMap<>();
        for (int i = 0; i < 26; i++) {
            do{
                p = curve.add(p, p);
            }while(p.isInfinity());
            charTable.put(i + 97, p); // 0 here refers to char 97 witch is a 
        }
        //special characters
        int[] codeAscii = new int[]{39, 44, 46, 58, 59};
        for(int i: codeAscii){
            p = curve.add(p, p);
            charTable.put(i, p);
        }
        charTable.put(32, Point.getInfinity()); 
        for(Integer key : charTable.keySet()){
            pointTable.put(charTable.get(key), key);
        }
    }

    public static void main(String[] args) {
        ECC ecc = new ECC();
        EllipticCurve c = new EllipticCurve(4, 20, 29, new Point(1, 5));
        ecc.initCodeTable(c);
        
        String msg = "i understood the importance in principle of public key cryptography, but it is all moved much faster than i expected i did not expect it to be a mainstay of advanced communications technology";
        // generate pair of keys
        KeyPair keys = generateKeyPair(c);
        // encrypt the msg
        int[] cipherText = ecc.encrypt(msg, keys.getPublicKey());

        // decrypt the result
        String plainText = ecc.decrypt(cipherText, keys.getPrivateKey());

        System.out.println("Cipher : ");
        Helpers.print(cipherText);
        System.out.println("Plain text : \n" + plainText);
    }
}
