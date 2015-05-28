package ECC;

import java.io.File;
import java.io.PrintStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * The key consists of: c, the elliptic curve used in the calculations, pK, the
 * point obtained from k * G, where k is the corresponding private key and G is
 * the base point of c.
 */
public class PublicKey {

    private EllipticCurve c;
    private Point pK;

    public PublicKey(EllipticCurve c, Point pK) {
        this.c = c;
        this.pK = pK;
    }

    public PublicKey(String pathFile) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(pathFile), StandardCharsets.UTF_8);
            BigInteger a = new BigInteger(lines.get(0), 16);
            BigInteger b = new BigInteger(lines.get(1), 16);
            BigInteger p = new BigInteger(lines.get(2), 16);
            BigInteger g1 = new BigInteger(lines.get(3), 16);
            BigInteger g2 = new BigInteger(lines.get(4), 16);
            BigInteger pK1 = new BigInteger(lines.get(5), 16);
            BigInteger pK2 = new BigInteger(lines.get(6), 16);
            EllipticCurve eC = new EllipticCurve(a, b, p, new Point(g1, g2));
            Point eCP = new Point(pK1, pK2);
            this.c = eC;
            this.pK = eCP;
        } catch (Exception e) {

        }
    }

    public EllipticCurve getCurve() {
        return c;
    }

    public void setCurve(EllipticCurve c) {
        this.c = c;
    }

    public Point getKey() {
        return pK;
    }

    public void setKey(Point pK) {
        this.pK = pK;
    }

    public Point getBasePoint() {
        return c.getBasePoint();
    }
}
