package edu.elgamal;

import org.junit.jupiter.api.*;

import java.math.BigInteger;

class ELGamalUtilsTest {

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
    @DisplayName("Test sig and ver method")
    void testSigAndVer(){
        System.out.println("**--- Test sig and ver method executed ---**");

        BigInteger p = BigInteger.valueOf(19);
        BigInteger g = BigInteger.valueOf(10);
        BigInteger x = BigInteger.valueOf(16);

        ELGamalUtils elGamalUtils = new ELGamalUtils(p, g, x);

        BigInteger m = BigInteger.valueOf(14);

        Signature sig = elGamalUtils.sig(m);
        boolean ver = elGamalUtils.ver(sig);
        System.out.println("verify: " + ver);
    }
}