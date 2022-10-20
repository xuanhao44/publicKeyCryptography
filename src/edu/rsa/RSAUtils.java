package edu.rsa;

import java.io.FileOutputStream;
import java.io.IOException;
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
     * 指数 e
     */
    private final BigInteger e;
    /**
     * 指数逆元 d
     */
    private final BigInteger d;

    /**
     * 生成密钥
     *
     * @param bitLength 大素数长度
     */
    public RSAUtils(int bitLength) {
        Random r = new Random();

        // 内部也是用 Miller-Rabin 算法
        p = BigInteger.probablePrime(bitLength, r);
        q = p.nextProbablePrime();

        // 大素数乘积
        n = p.multiply(q);

        // phiN = (p - 1) * (q - 1)
        phiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        // gcd(e, phiN) = 1, 且 e 不可以太小, 同时 d 也不可以太小, 于是随机生成一个 bitLength 长度的 e
        BigInteger temp = new BigInteger(bitLength, r);
        while (!temp.gcd(phiN).equals(BigInteger.ONE)) {
            temp = temp.add(BigInteger.ONE);
        }
        e = temp;

        // d = e^{-1} mod phiN
        d = e.modInverse(phiN);
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
        System.out.println("phiN  = " + phiN + ", len(phiN) = " + phiN.bitLength());
        System.out.println("e  = " + e + ", len(e) = " + e.bitLength());
        System.out.println("gcd(e, phiN) = " + e.gcd(phiN));
        System.out.println("d  = " + d + ", len(d) = " + d.bitLength());
        System.out.println("e * d mod phiN = " + e.multiply(d).mod(phiN));
    }

    /**
     * 获得公钥 {e, n}
     *
     * @return publicKey
     */
    private String getPublicKey() {
        return e.toString() + n.toString();
    }

    /**
     * 获得秘钥 {d, n}
     *
     * @return privateKey
     */
    private String getPrivateKey() {
        return d.toString() + n.toString();
    }

    /**
     * 输出公钥对
     *
     * @param puName 公钥文件路径
     * @param prName 密钥文件路径
     */
    public void genKeys(String puName, String prName) throws IOException {
        FileOutputStream fosPu = new FileOutputStream(puName);
        FileOutputStream fosPr = new FileOutputStream(prName);

        fosPu.write(getPublicKey().getBytes());
        fosPr.write(getPrivateKey().getBytes());

        fosPu.close();
        fosPr.close();

    }

    public BigInteger getN() {
        return n;
    }
}
