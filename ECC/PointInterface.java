package ECC;

import java.math.BigInteger;

public interface PointInterface {
    BigInteger getX();
    BigInteger getY();
    boolean isInfinity();
    PointInterface negate();
}
