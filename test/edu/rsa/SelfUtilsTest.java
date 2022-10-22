package edu.rsa;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SelfUtilsTest {

    private static final BigInteger ONE = BigInteger.ONE;

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

    /**
     * 快速幂: 最后取模
     */
    @ParameterizedTest
    @Order(1)
    @DisplayName("Test quickPow1 method")
    @CsvSource({"5, 117, 19"})
    void quickPow1(BigInteger m, BigInteger e, BigInteger n) {
        System.out.println("**--- Test quickPow1 method executed ---**");

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

    /**
     * 快速幂: 循环取模
     */
    @ParameterizedTest
    @Order(2)
    @DisplayName("Test quickPow2 method")
    @CsvSource({"2, 29"})
    void quickPow2(BigInteger a, BigInteger p) {
        System.out.println("**--- Test quickPow2 method executed ---**");

        // 离散对数
        System.out.println("a = " + a); // 本原根
        System.out.println("p = " + p); // 模数

        // (x,y): dlog_(a,p) (x) = y
        Map<BigInteger, BigInteger> dLogAp = new HashMap<>();

        for (BigInteger i = ONE; i.compareTo(p) < 0; i = i.add(ONE))
            dLogAp.put(a.modPow(i, p), i); // x = a ^ y mod p

        for (BigInteger j = ONE; j.compareTo(p) < 0; j = j.add(ONE))
            System.out.println("dlog_{" + a + "," + p + "}" + "(" + j + "} = " + dLogAp.get(j));

    }

    /**
     * 求最大公因数
     */
    @ParameterizedTest
    @Order(3)
    @DisplayName("Test mGcd method")
    @CsvSource({"24140, 16762"})
    void mGcd(BigInteger a, BigInteger b) {
        System.out.println("**--- Test mGcd method executed ---**");

        System.out.println("a = " + a);
        System.out.println("b = " + b);
        System.out.println("Calc: ans = gcd(a,b)");

        BigInteger self2 = SelfUtils.mGcd(a, b);
        BigInteger ans = a.gcd(b);

        System.out.println("ans = " + ans);
        System.out.println("self2 = " + self2);

        assertEquals(ans, self2);
    }

    /**
     * 求模逆元
     */
    @ParameterizedTest
    @Order(4)
    @DisplayName("Test modR method")
    @CsvSource({"1234, 4321"})
    void modR(BigInteger a, BigInteger b) {
        System.out.println("**--- Test modR method executed ---**");

        System.out.println("a = " + a);
        System.out.println("b = " + b);
        System.out.println("Calc: ans = a^{-1} mod b");

        BigInteger self = SelfUtils.modR(b, a); // 第一个是模数, 第二个是待求逆元的数
        BigInteger ans = a.modInverse(b);

        System.out.println("ans = " + ans);
        System.out.println("self = " + self);

        assertEquals(ans, self);
    }

    /**
     * BigInteger.probablePrime 生成一个素数, 用我们的 MRTest 去测试
     * 重复 100 次
     */
    @RepeatedTest(100)
    @Order(5)
    @DisplayName("Test MRTest method")
    void MRTest() {
        System.out.println("**--- Test MRTest method executed ---**");

        int bitLength = 1024;
        Random r = new Random();
        BigInteger n = BigInteger.probablePrime(bitLength, r);
        System.out.println("n = " + n);

        int rounds = 1024;
        boolean self = SelfUtils.MRTest(n, rounds);
        System.out.println("self = " + self);

        assertTrue(self);
    }

    /**
     * 我们生成一个大素数, 用 BigInteger 的 isProbablePrime 去测试
     * 重复 100 次
     */
    @RepeatedTest(100)
    @Order(6)
    @DisplayName("Test genPrime method")
    void genPrime() {
        System.out.println("**--- Test genPrime method executed ---**");

        int bitLength = 1024;
        BigInteger self = SelfUtils.genPrime(bitLength);
        System.out.println("self genPrime = " + self);

        int certainty = 100; // 可信度: 1 - (1/2)^certainty
        boolean judge = self.isProbablePrime(certainty);
        System.out.println("judge = " + judge);

        assertTrue(judge);
    }
}