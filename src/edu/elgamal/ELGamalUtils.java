package edu.elgamal;

import java.math.BigInteger;
import java.util.Random;

public class ELGamalUtils {

    private static final BigInteger ONE = BigInteger.ONE;

    /**
     * 大素数 p, 可以使得求解离散对数为困难问题
     */
    private final BigInteger p;
    /**
     * 用于计算方便的 p - 1
     */
    private final BigInteger p_1;
    /**
     * 大素数 p 的一个本原根 g
     */
    private final BigInteger g;
    /**
     * 私钥 x: 随机数, 满足 1 < x < p - 1
     * x \in {2....,p - 2}
     * or can be called sk_A
     */
    private final BigInteger x;
    /**
     * 公钥 y: y = g^x mod p
     * or can be called pk_A
     */
    private final BigInteger y;

    public ELGamalUtils(int bitLength, int certainty) {
        PrimitiveRoot primitiveRoot = new PrimitiveRoot(bitLength, certainty, new Random());
        // 得到的大素数 p
        p = primitiveRoot.getP();
        // 用于计算方便的 p - 1
        p_1 = p.subtract(ONE);
        // 大素数 p 的一个原根 g
        g = primitiveRoot.getG();
        // 私钥 x: 随机数, 满足 1 < x < p - 1
        x = PrimitiveRoot.getRandomBi(p_1, new Random());
        // 公钥参数 y = g^x mod p
        y = g.modPow(x, p);
    }

    /**
     * 填充密钥
     *
     * @param p 大素数 p
     * @param g p 的一个原根 g
     * @param x 密钥, 满足 1 < x < p - 1
     */
    public ELGamalUtils(BigInteger p, BigInteger g, BigInteger x) {
        this.p = p;
        this.g = g;
        this.x = x;

        this.p_1 = p.subtract(ONE);
        this.y = g.modPow(x, p);
    }

    /**
     * 明文哈希处理
     *
     * @param m 待签消息
     * @return hashM 哈希处理消息
     */
    private BigInteger hash(BigInteger m) {
        return m;
    }

    /**
     * 签名过程
     *
     * @param m 待签消息
     * @return Signature 明文和签名的组合
     */
    public Signature sig(BigInteger m) {
        // 随机整数 k, 满足 gcd(k, p - 1) = 1
        BigInteger k;
        do {
            k = PrimitiveRoot.getRandomBi(p_1, new Random());
        } while (!k.gcd(p_1).equals(ONE));

        // 实验测试用输出
        System.out.println("len(k) = " + k.bitLength() + ", k = " + k);

        // k 关于 p - 1 的逆元
        BigInteger kModInverse = k.modInverse(p_1);

        // 求明文 m 的哈希值
        BigInteger hashM = hash(m);

        // r = g^k mod p
        BigInteger r = g.modPow(k, p);

        /*
         * 签名空间 s = k^{-1} * (hash(m) - xr) mod (p - 1)
         * k^{-1} * (hash(m) - xr) 可能是负数
         */
        BigInteger s = (kModInverse.multiply(hashM.subtract(x.multiply(r)))).mod(p_1);

        // 返回明文和签名的组合
        return new Signature(m, r, s);
    }

    /**
     验证签名过程
     */
    public boolean ver(Signature signature) {
        // 接收方收到 m 和 (r, s)
        BigInteger m = signature.m;
        BigInteger r = signature.r;
        BigInteger s = signature.s;

        // 计算 hash(m)
        BigInteger hashM = hash(m);

        // v1 = g^hash(m) mod p
        BigInteger v1 = g.modPow(hashM, p);

        /*
         * v2 = (y^r * r^s) mod p
         * 计算利用同余性质
         * v2p1 = y^r mod p
         * v2p2 = r^s mod p
         * v2 = (v2p1 * v2p2) mod p
         */
        BigInteger v2p1 = y.modPow(r, p);
        BigInteger v2p2 = r.modPow(s, p);
        BigInteger v2 = (v2p1.multiply(v2p2)).mod(p);

        // 验证结果
        return v1.equals(v2);
    }

    public void printAll() {
        System.out.println("ELGamalUtils param start display");

        System.out.println("ELGamalUtils pubKey start display");
        System.out.println("len(p) = " + p.bitLength() + ", p = " + p);
        System.out.println("len(g) = " + g.bitLength() + ", g = " + g);
        System.out.println("len(y) = " + y.bitLength() + ", y = " + y);
        System.out.println("ELGamalUtils pubKey end display");

        System.out.println("ELGamalUtils priKey start display");
        System.out.println("len(x) = " + x.bitLength() + ", x = " + x);
        System.out.println("ELGamalUtils priKey start display");

        System.out.println("ELGamalUtils param end display");
    }
}
