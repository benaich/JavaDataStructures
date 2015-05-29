package ECC;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Random;

public class ECC {

    public static int PAD = 5;
    public static final Random r = new Random();

    private HashMap<Point, Integer> pointTable;
    private HashMap<Integer, Point> charTable;

    private Encoder mEncoder;
    private Decoder mDecoder;

    public ECC(EllipticCurve c) {
        initCodeTable(c);
        this.mEncoder = new Encoder(charTable);
        this.mDecoder = new Decoder(pointTable);
    }

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

        System.out.println("----------------- Encryption process -----------------");
        System.out.println(c);
        System.out.println("Mesage to encrypt, m = " + msg);
        System.out.println("Bob's public key, Pb = " + publicKey);
        System.out.println("Alice's private key, k = " + k);
        System.out.println("The ecryption key, sharedSecret = k * Pb = " + sharedSecret);
        System.out.println("The hint to compute sharedSecret for bob, keyHint = " + keyHint);
        
        Matrix mMatrix = mEncoder.encode(msg);
        mMatrix.performAddition(Helpers.toBinary(sharedSecret));
        System.out.println("sharedSecret binary format :");
        Helpers.print(Helpers.toBinary(sharedSecret));
        System.out.println("4) encrypt the matrix with sharedSecret (code addition)");
        System.out.println(mMatrix);
        return mMatrix.toArray(Helpers.toBinary(keyHint));
    }

    private String decrypt(int[] cipherText, PrivateKey key) {
        EllipticCurve c = key.getCurve();
        Point g = c.getBasePoint();
        BigInteger privateKey = key.getKey();
        BigInteger p = c.getP();

        Point keyHint = Point.make(cipherText);
        Point sharedSecret = c.multiply(keyHint, privateKey);

        System.out.println("\n----------------- Decryption process -----------------");
        System.out.println("1) Bob receive this :");
        Helpers.print(cipherText);
        System.out.println("");
        System.out.println("2) Extract keyhint and the matrix C");
        System.out.println("KeyHint = "+keyHint);

        //get the decypted matrix
        Matrix mMatrix = Matrix.make(cipherText);
        System.out.println("C = ");
        System.out.println(mMatrix);
        //substract the key form the matrix
        mMatrix.performSubstraction(Helpers.toBinary(sharedSecret));
        System.out.println("Matrix after substraction");
        System.out.println(mMatrix);
        //decode the matrix
        System.out.println("3) Reverse Matrix Scrambling");
        return mDecoder.decode(mMatrix);
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

        return new KeyPair(
                new PublicKey(c, publicKey),
                new PrivateKey(c, privateKey)
        );
    }

    public final void initCodeTable(EllipticCurve curve) {
        charTable = new HashMap<>();
        pointTable = new HashMap<>();
        Point p = curve.getBasePoint();
        for (int i = 1; i < 27; i++) {
            do {
                p = curve.multiply(curve.getBasePoint(), i);
            } while (p.isInfinity());
            charTable.put(i + 96, p); // 0 here refers to char 97 witch is a 
        }
        //special characters
        charTable.put(32, Point.getInfinity()); //space
        int[] codeAscii = new int[]{10, 13, 39, 40, 41, 44, 46, 58, 59};
        for (int i : codeAscii) {
            p = curve.add(p, curve.getBasePoint());
            charTable.put(i, p);
        }

        //populate the points symbol table
        for (Integer key : charTable.keySet()) {
            pointTable.put(charTable.get(key), key);
        }
    }

    public void displayCodeTable() {
        System.out.println("------ Code Table -------");
        charTable.forEach((cle, val) -> {
            System.out.println((char) cle.intValue() + " -> " + val);
        });
    }

    public static void main(String[] args) {
        EllipticCurve c = new EllipticCurve(4, 20, 29, new Point(1, 5));
        ECC ecc = new ECC(c);
        ecc.displayCodeTable();
        String msg = "i understood the importance in principle of public key cryptography, but it is all moved much faster than i expected i did not expect it to be a mainstay of advanced communications technology";
        msg = "hi there";
        // generate pair of keys
        KeyPair keys = generateKeyPair(c);
        // encrypt the msg
        int[] cipherText = ecc.encrypt(msg, keys.getPublicKey());
        System.out.println("5) Alice send this to Bob:");
        Helpers.print(cipherText);

        // decrypt the result
        String plainText = ecc.decrypt(cipherText, keys.getPrivateKey());
        System.out.println("\n5) Translate each point to a carracter");

        //System.out.println("Cipher : ");
        //Helpers.print(cipherText);
        System.out.println("Plain text : \n" + plainText);
    }
}
