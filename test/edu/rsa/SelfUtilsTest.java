package edu.rsa;

import org.junit.jupiter.api.*;

import java.math.BigInteger;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SelfUtilsTest {

    @BeforeAll
    static void beforeAll() {
        System.out.println("**--- Executed once before all test methods in this class ---**");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("**--- Executed once after all test methods in this class ---**");
    }

    @BeforeEach
    void setUp() {
        System.out.println("**--- Executed before each test method in this class ---**");
    }

    @AfterEach
    void tearDown() {
        System.out.println("**--- Executed after each test method in this class ---**");
    }

    @Test
    void quickPow1() {
        System.out.println("**--- Test quickPow1 method executed ---**");

        BigInteger m = BigInteger.valueOf(5);
        BigInteger e = BigInteger.valueOf(117);
        BigInteger n = BigInteger.valueOf(19);
        System.out.println("m = " + m);
        System.out.println("e = " + e);
        System.out.println("n = " + n);
        System.out.println("Calc: ans = m^e mod n");

        BigInteger self1 = SelfUtils.quickPow1(m, e, n);
        BigInteger ans = m.modPow(e, n);

        System.out.println("ans = " + ans);
        System.out.println("self1 = " + self1);

        assertEquals(ans, self1);
    }

    @Test
    void quickPow2() {
        System.out.println("**--- Test quickPow2 method executed ---**");

        BigInteger m = BigInteger.valueOf(5);
        BigInteger e = BigInteger.valueOf(117);
        BigInteger n = BigInteger.valueOf(19);
        System.out.println("m = " + m);
        System.out.println("e = " + e);
        System.out.println("n = " + n);
        System.out.println("Calc: ans = m^e mod n");

        BigInteger self2 = SelfUtils.quickPow2(m, e, n);
        BigInteger ans = m.modPow(e, n);

        System.out.println("ans = " + ans);
        System.out.println("self2 = " + self2);

        assertEquals(ans, self2);
    }

    @Test
    void mGcd() {
        System.out.println("**--- Test mGcd method executed ---**");

        BigInteger a = BigInteger.valueOf(24140);
        BigInteger b = BigInteger.valueOf(16762);
        System.out.println("a = " + a);
        System.out.println("b = " + b);
        System.out.println("Calc: ans = gcd(a,b)");

        BigInteger self2 = SelfUtils.mGcd(a, b);
        BigInteger ans = a.gcd(b);

        System.out.println("ans = " + ans);
        System.out.println("self2 = " + self2);

        assertEquals(ans, self2);
    }

    @Test
    void modR() {
        System.out.println("**--- Test modR method executed ---**");

        BigInteger a = BigInteger.valueOf(1234);
        BigInteger b = BigInteger.valueOf(4321);
        System.out.println("a = " + a);
        System.out.println("b = " + b);
        System.out.println("Calc: ans = a^{-1} mod b");

        BigInteger self = SelfUtils.modR(b, a); // 第一个是模数, 第二个是待求逆元的数
        BigInteger ans = a.modInverse(b);

        System.out.println("ans = " + ans);
        System.out.println("self = " + self);

        assertEquals(ans, self);
    }

    // BigInteger.probablePrime 生成一个素数, 用我们的 MRTest 去测试
    boolean MRTest() {
        System.out.println("**--- Test MRTest method executed ---**");

        int bitLength = 1024;
        Random r = new Random();
        BigInteger n = BigInteger.probablePrime(bitLength, r);
        System.out.println("n = " + n);

        int rounds = 1024;
        boolean self = SelfUtils.MRTest(n, rounds);
        System.out.println("self = " + self);

        return self;
    }

    @Test
    void MRTestN() {
        System.out.println("**--- Test MRTest method many times---**");

        boolean result;
        do {
            result = MRTest();
        } while (result); // 直到错了才能停
    }

    // 我们生成一个大素数, 用 BigInteger 的 isProbablePrime 去测试
    boolean genPrime() {
        System.out.println("**--- Test genPrime method executed ---**");

        int bitLength = 1024;
        BigInteger self = SelfUtils.genPrime(bitLength);
        System.out.println("self genPrime = " + self);

        int certainty = 100;
        boolean judge = self.isProbablePrime(certainty);
        System.out.println("judge = " + judge);

        return judge;
    }

    @Test
    void genPrimeN() {
        System.out.println("**--- Test genPrime method many times---**");

        boolean result;
        do {
            result = genPrime();
        } while (result); // 直到错了才能停
    }

}