package edu.elgamal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Random;

class PrimitiveRootTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void genRandomRoot() {

        PrimitiveRoot primitiveRoot = new PrimitiveRoot(3, 100, new Random());

        System.out.println("p = " + primitiveRoot.getP());
        System.out.println("g = " + primitiveRoot.getG());
    }
}