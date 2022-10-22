package edu.rsa;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SelfUtils {

    private static boolean isEven(BigInteger bi) {
        return bi.mod(BigInteger.TWO).equals(BigInteger.ZERO);
    }

    private static BigInteger getRandomBi(BigInteger n, Random rnd) {
        // From http://stackoverflow.com/a/2290089
        BigInteger r;
        do {
            r = new BigInteger(n.bitLength(), rnd);
        } while (r.compareTo(n) >= 0);
        return r;
    }

    // m 为底数, e 为指数, n
    public static BigInteger quickPow1(BigInteger m, BigInteger e, BigInteger n) {

        BigInteger ans = BigInteger.ONE;

        int eLength = e.bitLength();
        for (int i = 0; i < eLength; i++) {
            if (e.testBit(i))
                ans = ans.multiply(m);

            m = (m.multiply(m)).remainder(n);
        }

        return ans.remainder(n);
    }

    public static BigInteger quickPow2(BigInteger m, BigInteger e, BigInteger n) {

        BigInteger ans = BigInteger.ONE;

        int eLength = e.bitLength();
        for (int i = 0; i < eLength; i++) {
            if (e.testBit(i))
                ans = ans.multiply(m).remainder(n);
            m = (m.multiply(m)).remainder(n);
        }

        return ans;
    }

    // gcd(a,b) = gcd(b,a mod b)
    public static BigInteger mGcd(BigInteger a, BigInteger b) {

        if (b.equals(BigInteger.ZERO))
            return a;

        return mGcd(b, a.remainder(b));
    }

    // exGcd(a,b)
    private static xyResult exGcd(BigInteger a, BigInteger b) {
        // b == 0
        if (b.equals(BigInteger.ZERO))
            // x = 1, y = 0
            return new xyResult(BigInteger.ONE, BigInteger.ZERO);

        // call exGcd(b, a mod b)
        xyResult next = exGcd(b, a.remainder(b));

        // x = next.y
        BigInteger nowX = next.y;
        // y = next.x - (a/b) * next.y
        BigInteger nowY = (next.x).subtract(a.divide(b).multiply(next.y));

        return new xyResult(nowX, nowY);
    }

    // a 是模数, b 是待求逆元的数
    // 返回 ax + by = gcd(a,b) = 1 的一组解
    // 其中 y 是要求的逆元
    public static BigInteger modR(BigInteger a, BigInteger b) {
        xyResult result = exGcd(a, b);
        // y < 0 负余数
        if ((result.y).compareTo(BigInteger.ZERO) < 0) {
            return a.add(result.y);
        } else {
            return result.y;
        }
    }

    private static List<BigInteger> genA(BigInteger n, int rounds) {
        List<BigInteger> aList = new ArrayList<>();

        aList.add(BigInteger.valueOf(2));
        aList.add(BigInteger.valueOf(325));
        aList.add(BigInteger.valueOf(9375));
        aList.add(BigInteger.valueOf(28178));
        aList.add(BigInteger.valueOf(450775));
        aList.add(BigInteger.valueOf(9780504));
        aList.add(BigInteger.valueOf(1795265022));

        // 不考虑连 aList 都用不完的情况
        // 再加 10 个 2 到 n 的随机数
        for (int i = 1; i <= rounds; i++) {
            Random r = new Random();
            BigInteger g = getRandomBi(n, r);
            aList.add(g);
        }

        return aList;
    }

    private static boolean MRTestWithA(BigInteger n, BigInteger u, BigInteger t, BigInteger a) {
        BigInteger v = a.modPow(u, n); // v = a^u mod n

        if (v.equals(BigInteger.ONE) || v.equals(n.subtract(BigInteger.ONE)))
            return true; // 1 or n-1

        for (BigInteger j = BigInteger.ONE; j.compareTo(t) <= 0; j = j.add(BigInteger.ONE)) {
            v = v.modPow(BigInteger.TWO, n); // v = v^2 mod n
            // 得到 n-1, 说明接下来都是 1, 可以退出了
            if (v.equals(n.subtract(BigInteger.ONE)) && !j.equals(t)) {
                v = BigInteger.ONE;
                break;
            }
            // 在中途而非开头得到 1，却没有经过 n-1，说明存在其他数字 y≠-1 满足 y^2 ≡ 1，则 x 一定不是奇素数
            if (v.equals(BigInteger.ONE))
                return false;
        }
        // 查看是不是以 1 结尾
        return v.equals(BigInteger.ONE);
    }

    public static boolean MRTest(BigInteger n, int rounds) {

        if (n.equals(BigInteger.ONE)) {
            return false;
        } else if (n.equals(BigInteger.TWO))
            return true; // n = 2 true
        else if (isEven(n))
            return false;

        // n = u * 2 ^ t + 1, calc u and t (the largest odd)
        BigInteger u = n.subtract(BigInteger.ONE);
        BigInteger t = BigInteger.ZERO;
        while (isEven(u)) {
            u = u.divide(BigInteger.TWO); // u /= 2
            t = t.add(BigInteger.ONE); // t++
        }

        // get suitable aList
        List<BigInteger> aList = genA(n, rounds);

        // use aList to test
        for (BigInteger a : aList)
            if (!MRTestWithA(n, u, t, a))
                return false;

        return true;
    }

    public static BigInteger genPrime(int bitLength) {
        boolean res;
        BigInteger bi;

        Random r;

        int rounds = 1024;
        do {
            r = new Random();
            bi = new BigInteger(bitLength, r);
            res = MRTest(bi, rounds);
        } while (!res); // till find res = true

        return bi;
    }

    // ax + by = gcd(a,b) 的一组解 (x,y)
    record xyResult(BigInteger x, BigInteger y) {
    }
}
