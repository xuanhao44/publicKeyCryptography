package edu.elgamal;

import java.math.BigInteger;
import java.util.Random;

/**
 用于公钥密码体系的素数本原根生成
 */
public class PrimitiveRoot {

    private static final BigInteger ONE = BigInteger.ONE;
    private static final BigInteger TWO = BigInteger.TWO;

    /**
    大素数 q
     */
    BigInteger q;
    /**
     大素数 p = 2 * q + 1
     */
    BigInteger p;
    /**
     p 的本原根, 计算得到的是任意一个
     */
    BigInteger g;

    /**
     * 获取等长, 但小于 n 的一个随机数 r
     *
     * @param n 从中获取长度和大小
     * @return r 符合要求的随机数
     */
    public static BigInteger getRandomBi(BigInteger n, Random rnd) {
        // From http://stackoverflow.com/a/2290089
        BigInteger r;
        do {
            r = new BigInteger(n.bitLength(), rnd);
        } while (r.compareTo(n) >= 0);
        return r;
    }

    /**
     * PrimitiveRoot 构造函数
     *
     * @param bitLength 素数长度
     * @param certainty 判断素数时的可信度
     * @param rnd 随机数种子
     */
    public PrimitiveRoot (int bitLength, int certainty, Random rnd) {
        // 1. 生成一个大素数 q, p 是素数, 其中 p = 2q + 1
        do {
            // 生成一个大素数 q
            q = BigInteger.probablePrime(bitLength, rnd);
            // p = 2 * q + 1
            p = q.multiply(TWO).add(ONE);
        } while(!p.isProbablePrime(certainty));

        // 2. 生成一个随机数 g, 1 < g < p - 1, 直到 g^2 mod p 和 g^q mod p 都不等于 1
        BigInteger modPow_g_2_p, modPow_g_q_p;
        boolean g_2_p_1, g_p_q_1;

        do {
            g = getRandomBi(p.subtract(ONE), new Random());
            modPow_g_2_p = g.modPow(TWO, p); // g^2 mod p
            modPow_g_q_p = g.modPow(q, p); // g^q mod p
            g_2_p_1 = modPow_g_2_p.equals(ONE);
            g_p_q_1 = modPow_g_q_p.equals(ONE);
        } while (g_2_p_1 && g_p_q_1);
    }

    public BigInteger getP() {
        return p;
    }

    public BigInteger getG() {
        return g;
    }

}
