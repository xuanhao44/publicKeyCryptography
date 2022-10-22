package edu.rsa;

import java.math.BigInteger;
import java.util.Random;

public class RSAUtils {

    /**
     * 大素数 p
     */
    private final BigInteger p;
    /**
     * 大素数 q
     */
    private final BigInteger q;
    /**
     * 大素数乘积 n
     */
    private final BigInteger n;
    /**
     * n 的欧拉函数值
     */
    private final BigInteger phiN;
    /**
     * 指数逆元 d
     */
    private final BigInteger d;
    /**
     * 指数 e
     */
    private BigInteger e;

    /**
     * 生成密钥
     *
     * @param bitLength 大素数长度
     */
    public RSAUtils(int bitLength) {
        Random r = new Random();

        // 内部也是用 Miller-Rabin 算法
        // p, q 不应该很接近; 一般相差几个 bit
        p = BigInteger.probablePrime(bitLength + 5, r);
        q = BigInteger.probablePrime(bitLength - 5, r);

        // 大素数乘积
        n = p.multiply(q);

        // phiN = (p - 1) * (q - 1)
        phiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        // gcd(e, phiN) = 1, 且 e 不可以太小, 同时 d 也不可以太小, 于是随机生成一个 bitLength 长度的 e
        e = new BigInteger(bitLength, r);
        while (!e.gcd(phiN).equals(BigInteger.ONE)) {
            e = e.add(BigInteger.ONE);
        }

        // d = e^{-1} mod phiN
        d = e.modInverse(phiN);
    }

    /**
     * 填充密钥
     *
     * @param p 大素数 p
     * @param q 大素数 q
     * @param e gcd(e, phiN) = 1
     */
    public RSAUtils(BigInteger p, BigInteger q, BigInteger e) {
        this.p = p;
        this.q = q;
        this.e = e;

        this.n = p.multiply(q);
        this.phiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        this.d = e.modInverse(phiN);
    }

    /**
     * 对明文加密 m^e mod n
     *
     * @param m 明文
     * @return m.modPow(e, n) 密文
     */
    public BigInteger en(BigInteger m) {
        return m.modPow(e, n);
    }

    /**
     * 对密文解密 c^d mod n
     *
     * @param c 密文
     * @return c.modPow(d, n) 解密明文
     */
    public BigInteger de(BigInteger c) {
        return c.modPow(d, n);
    }

    /**
     * 输出参数
     */
    public void printAll() {
        System.out.println("p = " + p + ", len(p) = " + p.bitLength());
        System.out.println("q = " + q + ", len(q) = " + q.bitLength());
        System.out.println("n = " + n + ", len(n) = " + n.bitLength());
        System.out.println("phiN = " + phiN + ", len(phiN) = " + phiN.bitLength());
        System.out.println("e = " + e + ", len(e) = " + e.bitLength());
        System.out.println("gcd(e, phiN) = " + e.gcd(phiN));
        System.out.println("d = " + d + ", len(d) = " + d.bitLength());
        System.out.println("e * d mod phiN = " + e.multiply(d).mod(phiN));
        System.out.println("e * d - 1 = " + e.multiply(d).subtract(BigInteger.ONE));
        System.out.println("(e * d - 1) / phi_N = " + (e.multiply(d).subtract(BigInteger.ONE)).divide(phiN));
    }

    public BigInteger getN() {
        return n;
    }
}
