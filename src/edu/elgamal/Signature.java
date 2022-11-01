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
     签名 (r, s): s
     */
    public BigInteger s;

    public Signature(BigInteger m, BigInteger r, BigInteger s) {
        this.m = m;
        this.r = r;
        this.s = s;
    }
}
