package edu.elgamal;

import java.math.BigInteger;
import java.util.Random;

public class ELGamalUtils {

    private static final BigInteger ONE = BigInteger.ONE;

    /**
     大素数 p, 可以使得求解离散对数为困难问题
     */
    private final BigInteger p;
    /**
     大素数 p 的一个原根 g
     */
    private final BigInteger g;
    /**
     私钥 x: 随机数, 满足 1 < x < p - 1
     */
    private final BigInteger x;
    /**
     公钥参数 y = g^x mod p
     */
    private final BigInteger y;

    private BigInteger Hash(BigInteger m) {
        return m;
    }

    public ELGamalUtils(int bitLength, int certainty) {

        PrimitiveRoot primitiveRoot = new PrimitiveRoot(bitLength, certainty, new Random());

        p = primitiveRoot.getP();
        g = primitiveRoot.getG();
        // 1 < x < p - 1
        x = PrimitiveRoot.getRandomBi(p.subtract(ONE), new Random());
        // y = g^x mod p
        y = g.modPow(x, p);
    }

    /**
     对明文签名
     */
    public Signature sig(BigInteger m) {
        BigInteger k;
        BigInteger p_1 = p.subtract(ONE);
        BigInteger k_modInverse;
        BigInteger r;
        BigInteger s;

        do {
            k = PrimitiveRoot.getRandomBi(p_1, new Random());
        } while (!k.gcd(p_1).equals(ONE));

        k_modInverse = k.modInverse(p_1);

        r = g.modPow(k, p);
        // s = k^{-1} * (m - xr) mod (p - 1)
        s = k_modInverse.multiply(m.subtract(x.multiply(r))).modInverse(p_1);

        return new Signature(m, r, s);
    }

    /**
     检验签名
     */
    public boolean ver(Signature signature) {

        BigInteger v1 = g.modPow(signature.m, p);

        BigInteger v2_part1 = y.modPow(signature.r, p);
        BigInteger v2_part2 = (signature.r).modPow(signature.s, p);
        BigInteger v2 = (v2_part1.multiply(v2_part2)).mod(p);

        return v1.equals(v2);
    }
}
