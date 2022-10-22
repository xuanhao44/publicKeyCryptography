package edu.rsa;

import org.junit.jupiter.api.*;

import java.math.BigInteger;

class RSAUtilsTest {

    RSAUtils test;

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
    void GivenBitLength() {
        System.out.println("**--- Test GivenBitLength ---**");

        int bitLength = 100;

        RSAUtils rsaUtils = new RSAUtils(bitLength);
        rsaUtils.printAll();
    }

    @Test
    void GivenParams() {
        System.out.println("**--- Test Given params construction method executed ---**");

        BigInteger p = BigInteger.valueOf(5);
        BigInteger q = BigInteger.valueOf(11);
        BigInteger e = BigInteger.valueOf(3);

        test = new RSAUtils(p, q, e);
        test.printAll();

        BigInteger m = BigInteger.valueOf(9);
        BigInteger c = test.en(m);
        System.out.println("en: c = " + c);

        BigInteger d = test.de(c);
        System.out.println("de: d = " + d);
    }
}