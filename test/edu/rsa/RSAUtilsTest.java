package edu.rsa;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigInteger;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RSAUtilsTest {

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
     * 测试密钥生成
     */
    @ParameterizedTest
    @Order(1)
    @DisplayName("Test GivenBitLength RSAUtils method")
    @ValueSource(ints = {16, 256, 512, 1024})
    void GivenBitLength(int bitLength) {
        System.out.println("**--- Test GivenBitLength ---**");

        RSAUtils rsaUtils = new RSAUtils(bitLength);
        rsaUtils.printAll();
    }

    /**
     * 给定密钥 p q e 的运算
     */
    @ParameterizedTest
    @Order(2)
    @DisplayName("Test GivenParamsAndData RSAUtils method")
    @CsvSource({"5, 11, 3, 9"})
    void GivenParamsAndData(BigInteger p, BigInteger q, BigInteger e, BigInteger m) {
        System.out.println("**--- Test GivenParamsAndData ---**");

        RSAUtils rsaUtils = new RSAUtils(p, q, e);
        rsaUtils.printAll();

        BigInteger c = rsaUtils.en(m);
        System.out.println("en: c = " + c);

        BigInteger d = rsaUtils.de(c);
        System.out.println("de: d = " + d);
    }
}