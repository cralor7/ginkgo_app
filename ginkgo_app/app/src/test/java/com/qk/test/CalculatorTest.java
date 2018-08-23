package com.qk.test;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author fengyezong & cuiweilong
 * @date 2018/8/16
 */
public class CalculatorTest {
    private Calculator mCalculator;
    @Before
    public void setUp() throws Exception {
        mCalculator = new Calculator();
    }

    @Test
    public void sum() throws Exception {
        assertEquals(6d, mCalculator.sum(1d, 5d), 0);
    }

    @Test
    public void substract() throws Exception {
        assertEquals(2d, mCalculator.substract(5d, 4d), 1);
    }

    @Test
    public void divide() throws Exception {
        assertEquals(3d, mCalculator.divide(20d, 5d), 1);
    }

    @Test
    public void multiply() throws Exception {
        assertEquals(12d, mCalculator.multiply(2d, 5d), 1);
    }

}