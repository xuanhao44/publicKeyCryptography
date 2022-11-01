package edu.elgamal;

import java.math.BigInteger;

public class ELGamal {
    public static void main(String[] args) {
        ELGamalUtils elGamalUtils = new ELGamalUtils();

        BigInteger m = BigInteger.valueOf(200111407);

        Signature sig = elGamalUtils.sig(m);
        boolean ver = elGamalUtils.ver(sig);

        System.out.println(ver);
    }
}
