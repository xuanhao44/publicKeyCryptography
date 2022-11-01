package edu.elgamal;

import java.math.BigInteger;

public class ELGamal {

    /**
     一次展示
     */
    public static void show(BigInteger m) {
        ELGamalUtils elGamalUtils = new ELGamalUtils(1024, 100);
        Signature sig = elGamalUtils.sig(m);
        System.out.println(m);
        boolean ver = elGamalUtils.ver(sig);
        System.out.println(ver);
    }

    public static void main(String[] args) {
        // 签名的信息 m 是你的学号
        BigInteger m = BigInteger.valueOf(200111407);

        for (int i = 1; i <= 2; i++)
            show(m);

    }
}
