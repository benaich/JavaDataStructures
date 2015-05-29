package ECC;

import java.math.BigInteger;

public class EllipticCurve {

    // The three parameters of the elliptic curve equation.
    private BigInteger a;
    private BigInteger b;
    private BigInteger p;

    // Optional attribute, the base point g.
    private Point basePoint = null;
    // some BigInteger constants that might help us in some calculations
    private static BigInteger THREE = new BigInteger("3");

    public EllipticCurve(BigInteger a, BigInteger b, BigInteger p, Point g) {
        this.a = a;
        this.b = b;
        this.p = p;
        this.basePoint = g;
    }

    public EllipticCurve(BigInteger a, BigInteger b, BigInteger p) {
        this(a, b, p, null);
    }

    public EllipticCurve(long a, long b, long p, Point g) {
        this(BigInteger.valueOf(a), BigInteger.valueOf(b), BigInteger.valueOf(p), g);
    }

    public EllipticCurve(long a, long b, long p) {
        this(a, b, p, null);
    }

    public BigInteger getA() {
        return a;
    }

    public BigInteger getB() {
        return b;
    }

    public BigInteger getP() {
        return p;
    }

    public Point getBasePoint() {
        return basePoint;
    }
    public void setBasePoint(Point p) {
        basePoint = p;
    }

    /**
     * This method will check whether a point belong to this curve or not.
     */
    public boolean contains(Point point) {
        if (point.isInfinity()) {
            return true;
        }

        return point.getX().multiply(point.getX()).mod(p).add(a).multiply(point.getX()).add(b)
                .mod(p).subtract(point.getY().multiply(point.getY())).mod(p)
                .compareTo(BigInteger.ZERO) == 0;
    }

    /**
     * add two point
     */
    public Point add(Point p1, Point p2) {
        if (p1 == null || p2 == null) {
            return null;
        }

        if (p1.isInfinity()) {
            return new Point(p2);
        } else if (p2.isInfinity()) {
            return new Point(p1);
        }

        // The lambda (the slope of the line formed by the two points) is different when the two points are the same.
        BigInteger lambda;
        if (p1.getX().subtract(p2.getX()).mod(p).compareTo(BigInteger.ZERO) == 0) {
            if (p1.getY().subtract(p2.getY()).mod(p).compareTo(BigInteger.ZERO) == 0) {
                // lambda = (3x1^2 + a) / (2y1)
                BigInteger nom = p1.getX().multiply(p1.getX()).multiply(THREE).add(a);
                BigInteger den = p1.getY().add(p1.getY());
                lambda = nom.multiply(den.modInverse(p));
            } else {
                // lambda = infinity
                return Point.getInfinity();
            }
        } else {
            // lambda = (y2 - y1) / (x2 - x1)
            BigInteger nom = p2.getY().subtract(p1.getY());
            BigInteger den = p2.getX().subtract(p1.getX());
            lambda = nom.multiply(den.modInverse(p));
        }

        // Now the easy part:
        // The result is (lambda^2 - x1 - y1, lambda(x2 - xr) - yp)
        BigInteger xr = lambda.multiply(lambda).subtract(p1.getX()).subtract(p2.getX()).mod(p);
        BigInteger yr = lambda.multiply(p1.getX().subtract(xr)).subtract(p1.getY()).mod(p);
        return new Point(xr, yr);
    }

    /**
     * Subtract two points, according to this equation: p1 - p2 = p1 + (-p2),
     */
    public Point subtract(Point p1, Point p2) {
        if (p1 == null || p2 == null) {
            return null;
        }
        return add(p1, p2.negate());
    }

    /**
     * Multiply p1 to a scalar n. That is, perform addition n times.
     */
    public Point multiply(Point p1, BigInteger n) {
        if (p1.isInfinity()) {
            return Point.getInfinity();
        }

        Point result = Point.getInfinity();
        int bitLength = n.bitLength();
        for (int i = bitLength - 1; i >= 0; --i) {
            result = add(result, result);
            if (n.testBit(i)) {
                result = add(result, p1);
            }
        }

        return result;
    }

    public Point multiply(Point p1, long n) {
        return multiply(p1, BigInteger.valueOf(n));
    }

    /**
     * Calculate the right hand side of the equation.
     */
    public BigInteger calculateRhs(BigInteger x) {
        return x.multiply(x).mod(p).add(a).multiply(x).add(b).mod(p);
    }

    public static void main(String[] args) {
        // This computes (2, 4) + (5, 9) in y^2 = x^3 + x + 6 mod 11
        EllipticCurve e = new EllipticCurve(4, 20, 29);
        Point p = new Point(1, 5);
        Point q = new Point(5, 9);

        System.out.println(p + " + " + q + " = " + e.add(p, q));
        for (int i = 0; i < 20; ++i) {
            System.out.println(p + " x " + i + " = " + e.multiply(p, i));
        }
    }

    @Override
    public String toString() {
        return "EllipticCurve EC : " + "y^2 = x^3 + " + a + "x + " + b + "c mod " + p + "\nGenerator point G = " + basePoint + '}';
    }
    
}
