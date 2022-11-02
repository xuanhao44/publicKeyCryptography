package edu.elgamal;

import java.math.BigInteger;

/**
 明文以及数字签名
 */
public class Signature {
    /**
     明文 m
     */
    public BigInteger m;
    /**
     签名 (r, s): r
     */
    public BigInteger r;
    /**
     * 签名 (r, s): s
     */
    public BigInteger s;

    public Signature(BigInteger m, BigInteger r, BigInteger s) {
        this.m = m;
        this.r = r;
        this.s = s;
    }

    public void printAll() {
        System.out.println("Signature start display");
        System.out.println("len(m) = " + m.bitLength() + ", m = " + m);
        System.out.println("len(r) = " + r.bitLength() + ", r = " + r);
        System.out.println("len(s) = " + s.bitLength() + ", s = " + s);
        System.out.println("Signature end display\n");
    }


}
