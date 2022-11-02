package edu.elgamal;

import java.math.BigInteger;
import java.util.Random;

public class ELGamal {

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
     * 篡改消息 m
     * <p>
     * 若篡改, 则使用等位长的随机数来篡改
     *
     * @param m    消息
     * @param does 是否篡改消息
     */
    public static BigInteger changeM(BigInteger m, boolean does) {
        if (does) {
            System.out.println("\nSignature is changed by attacker.\n");
            return getRandomBi(m, new Random());
        } else {
            return m;
        }

    }

    /**
     * 一次过程展示
     *
     * @param m    消息
     * @param does 是否篡改消息
     */
    public static void show(BigInteger m, boolean does) {
        System.out.println("ELGamalUtils init...");
        ELGamalUtils elGamalUtils = new ELGamalUtils(1024, 100);
        elGamalUtils.printAll();
        System.out.println("ELGamalUtils success!\n");

        System.out.println("Signature gen...");
        Signature sig = elGamalUtils.sig(m);
        System.out.println("Signature gen complete.");
        sig.printAll();

        sig.m = changeM(m, does);

        boolean ver = elGamalUtils.ver(sig);
        System.out.println("verify the signature: " + ver + "\n");
        if (!ver) System.out.println("the m has been changed!\n");
    }

    public static void main(String[] args) {
        // 签名的信息 m 是你的学号
        BigInteger m = BigInteger.valueOf(200111407);
        System.out.println("m = " + m + "\n");

        // 随机生成两次不同的 k 的签名信息和签名验证的结果
        for (int i = 1; i <= 2; i++) {
            System.out.println("test " + i + "\n");
            show(m, false);
        }

        // 验证签名时, 假设消息 m 被篡改的情况, 要输出验证签名不通过的信息
        System.out.println("test change m\n");
        show(m, true);

    }
}
