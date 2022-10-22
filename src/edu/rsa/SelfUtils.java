package edu.rsa;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SelfUtils {

    private static final BigInteger ONE = BigInteger.ONE;
    private static final BigInteger TWO = BigInteger.TWO;
    private static final BigInteger ZERO = BigInteger.ZERO;

    /**
     * 判断是否为偶数
     *
     * @param n 待判断数
     * @return true/false
     */
    private static boolean isEven(BigInteger n) {
        return n.mod(TWO).equals(ZERO);
    }

    /**
     * 获取等长, 但小于 n 的一个随机数 r
     *
     * @param n 从中获取长度和大小
     * @return r 符合要求的随机数
     */
    private static BigInteger getRandomBi(BigInteger n, Random rnd) {
        // From http://stackoverflow.com/a/2290089
        BigInteger r;
        do {
            r = new BigInteger(n.bitLength(), rnd);
        } while (r.compareTo(n) >= 0);
        return r;
    }

    /**
     * m^e mod n 快速幂: 最后取模
     *
     * @param m 底数
     * @param e 指数
     * @param n 模数
     * @return ans 模幂结果
     */
    public static BigInteger quickPow1(BigInteger m, BigInteger e, BigInteger n) {

        BigInteger ans = ONE;

        int eLength = e.bitLength();
        for (int i = 0; i < eLength; i++) {
            if (e.testBit(i))
                ans = ans.multiply(m);

            m = (m.multiply(m)).remainder(n);
        }

        return ans.remainder(n);
    }

    /**
     * m^e mod n 快速幂: 循环取模
     *
     * @param m 底数
     * @param e 指数
     * @param n 模数
     * @return ans 模幂结果
     */
    public static BigInteger quickPow2(BigInteger m, BigInteger e, BigInteger n) {

        BigInteger ans = ONE;

        int eLength = e.bitLength();
        for (int i = 0; i < eLength; i++) {
            if (e.testBit(i))
                ans = ans.multiply(m).remainder(n);
            m = (m.multiply(m)).remainder(n);
        }

        return ans;
    }

    /**
     * gcd(a,b) 欧几里得算法, 求最大公因数, 辗转相除法
     *
     * @param a 数 a
     * @param b 数 b
     * @return 最大公因数 gcd(a,b)
     */
    public static BigInteger mGcd(BigInteger a, BigInteger b) {

        if (b.equals(ZERO))
            return a;

        // gcd(a,b) = gcd(b,a mod b)
        return mGcd(b, a.remainder(b));
    }

    /**
     * exGcd(a,b) 拓展欧几里得算法
     *
     * @param a 数 a
     * @param b 数 b
     * @return 解 (x,y)
     */
    private static xyResult exGcd(BigInteger a, BigInteger b) {
        // b == 0
        if (b.equals(ZERO))
            // x = 1, y = 0
            return new xyResult(ONE, ZERO);

        // call exGcd(b, a mod b)
        xyResult next = exGcd(b, a.remainder(b));

        // x = next.y
        BigInteger nowX = next.y;
        // y = next.x - (a/b) * next.y
        BigInteger nowY = (next.x).subtract(a.divide(b).multiply(next.y));

        return new xyResult(nowX, nowY);
    }

    /**
     * 求逆元 modInverse
     *
     * @param a 模数
     * @param b 待求逆元的数
     * @return ax + by = gcd(a,b) = 1 的一组解 (x,y) 中的 y, 即要求的逆元
     */
    public static BigInteger modR(BigInteger a, BigInteger b) {
        xyResult result = exGcd(a, b);
        // y < 0 负余数
        if ((result.y).compareTo(ZERO) < 0)
            return a.add(result.y);
        else return result.y;
    }

    /**
     * 生成用于素性测试的底数
     *
     * @param n      待测数 n
     * @param rounds 测试次数, 也即增添的底数数量
     * @return aList 底数列表
     */
    private static List<BigInteger> genA(BigInteger n, int rounds) {
        List<BigInteger> aList = new ArrayList<>();

        aList.add(BigInteger.valueOf(2));
        aList.add(BigInteger.valueOf(325));
        aList.add(BigInteger.valueOf(9375));
        aList.add(BigInteger.valueOf(28178));
        aList.add(BigInteger.valueOf(450775));
        aList.add(BigInteger.valueOf(9780504));
        aList.add(BigInteger.valueOf(1795265022));

        // 不考虑连 aList 都用不完的情况 (n < 1795265022)
        // 再加 rounds 个小于 n 的随机数
        for (int i = 1; i <= rounds; i++) {
            Random r = new Random();
            BigInteger g = getRandomBi(n, r);
            aList.add(g);
        }

        return aList;
    }

    /**
     * 用给定的 n a 进行一轮素性测试 (u, t 为 n 产生的参数)
     *
     * @param n 待测数 n
     * @param u 最大奇数
     * @param t n = u * 2^t + 1
     * @param a 用于素性测试的底数
     * @return 通过素性测试 true/false
     */
    private static boolean MRTestWithA(BigInteger n, BigInteger u, BigInteger t, BigInteger a) {
        BigInteger v = a.modPow(u, n); // v = a^u mod n

        if (v.equals(ONE) || v.equals(n.subtract(ONE)))
            return true; // 1 or n-1

        for (BigInteger j = ONE; j.compareTo(t) <= 0; j = j.add(ONE)) {
            v = v.modPow(TWO, n); // v = v^2 mod n
            // 得到 n-1, 说明接下来都是 1, 可以退出了
            if (v.equals(n.subtract(ONE)) && !j.equals(t)) {
                v = ONE;
                break;
            }
            // 在中途而非开头得到 1，却没有经过 n-1，说明存在其他数字 y ≠ -1 满足 y^2 ≡ 1，则 x 一定不是奇素数
            if (v.equals(ONE))
                return false;
        }
        // 查看是不是以 1 结尾
        return v.equals(ONE);
    }

    /**
     * 对待测数 n 进行近似 rounds 轮素性测试
     *
     * @param n      待测数 n
     * @param rounds ”近似“测试次数
     * @return 通过运行的所有素性测试, n 很大可能是素数 true/false
     */
    public static boolean MRTest(BigInteger n, int rounds) {

        if (n.equals(ONE))
            return false;
        else if (n.equals(TWO))
            return true;
        else if (isEven(n))
            return false;

        // n = u * 2^t + 1, calc t and u (the largest odd)
        BigInteger u = n.subtract(ONE);
        BigInteger t = ZERO;
        while (isEven(u)) {
            u = u.divide(TWO); // u /= 2
            t = t.add(ONE); // t++
        }

        // get suitable aList
        List<BigInteger> aList = genA(n, rounds);

        // use aList to test
        for (BigInteger a : aList)
            if (!MRTestWithA(n, u, t, a))
                return false;

        return true;
    }

    /**
     * 按照指定位长生成素数
     *
     * @param bitLength 位长
     * @return 使用 Miller-Rabin 算法得到的素数
     */
    public static BigInteger genPrime(int bitLength) {

        boolean judge;
        BigInteger probablePrime;
        Random r;
        int rounds = 1024;
        do {
            r = new Random();
            probablePrime = new BigInteger(bitLength, r);
            judge = MRTest(probablePrime, rounds);
        } while (!judge); // till find judge = true

        return probablePrime;
    }

    /**
     * 二元一次方程的解表示 (x, y)
     */
    record xyResult(BigInteger x, BigInteger y) {
    }
}
