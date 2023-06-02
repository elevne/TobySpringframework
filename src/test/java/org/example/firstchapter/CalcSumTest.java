package org.example.firstchapter;

import org.example.thirdchapter.Calculator;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

public class CalcSumTest {

    Calculator calculator;
    String numFilePath;

    @Before
    public void setUp() {
        this.calculator = new Calculator();
        this.numFilePath = getClass().getResource("/numbers.txt").getPath();
    }

    @Test
    public void sumOfNumbers() throws IOException {
        Assert.assertThat(calculator.calcSum(this.numFilePath), Is.is(10));
    }

    @Test
    public void multiplicationOfNumbers() throws IOException {
        Assert.assertEquals(Optional.ofNullable(calculator.calcMultiply(this.numFilePath)), Optional.of(24));
    }

    @Test
    public void concatenationOfNumbers() throws IOException {
        Assert.assertEquals(Optional.ofNullable(calculator.concatenate(this.numFilePath)), Optional.of("1234"));
    }

}
