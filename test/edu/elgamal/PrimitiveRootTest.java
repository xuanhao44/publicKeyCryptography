package edu.elgamal;

import edu.rsa.SelfUtils;
import org.junit.jupiter.api.*;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

class PrimitiveRootTest {

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
     * 计算素数和其一个随机原根
     * 原根验证: 使用较小规模的数据, 直接打印离散对数表
     */
    @Test
    @DisplayName("Test PrimitiveRoot method")
    void printRoot() {
        System.out.println("**--- Test PrimitiveRoot executed ---**");

        // 发现生成的 g 和 q 才是 bitLength 位的
        PrimitiveRoot primitiveRoot = new PrimitiveRoot(4, 100, new Random());
        BigInteger p = primitiveRoot.getP();
        BigInteger q = primitiveRoot.getQ();
        BigInteger g = primitiveRoot.getG();

        System.out.println("素数 p = " + p + ", len(p) = " + p.bitLength());
        System.out.println("随机原根 g = " + g + ", len(q) = " + g.bitLength());
        System.out.println("中间参数 q = " + q + ", len(q) = " + q.bitLength());

        // (x,y): dlog_(g,p) (x) = y
        Map<BigInteger, BigInteger> dLogGp = new HashMap<>();

        for (BigInteger i = ONE; i.compareTo(p) < 0; i = i.add(ONE))
            dLogGp.put(SelfUtils.quickPow2(g, i, p), i); // x = a ^ y mod p

        for (BigInteger j = ONE; j.compareTo(p) < 0; j = j.add(ONE))
            System.out.println("dlog_{" + g + "," + p + "}" + "(" + j + ") = " + dLogGp.get(j));
    }
}