package com.challenge.application.game.service.ai;

import com.challenge.application.game.domain.InputNumber;
import com.challenge.application.game.domain.OutputNumber;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DividableByThreeAiTest {

    DividableByThreeAi dividableByThreeAi;

    @Before
    public void setUp() throws Exception {
        dividableByThreeAi = new DividableByThreeAi();
    }

    @Test
    public void shouldCalculateClosestDividableNumberFor10() {
        OutputNumber outputNumber = new OutputNumber(10);

        InputNumber result = dividableByThreeAi.calculateNextInputNumberFor(outputNumber);

        assertEquals("Closest divisible by three number for 10 should be 9.", new InputNumber(9), result);
    }

    @Test
    public void shouldCalculateClosestDividableNumberFor11() {
        OutputNumber outputNumber = new OutputNumber(11);

        InputNumber result = dividableByThreeAi.calculateNextInputNumberFor(outputNumber);

        assertEquals("Closest divisible by three number for 11 should be 12.", new InputNumber(12), result);
    }

    @Test
    public void shouldCalculateClosestDividableNumberFor12() {
        OutputNumber outputNumber = new OutputNumber(12);

        InputNumber result = dividableByThreeAi.calculateNextInputNumberFor(outputNumber);

        assertEquals("Closest divisible by three number for 12 should be 12.", new InputNumber(12), result);
    }

    @Test
    public void shouldCalculateClosestDividableNumberFor0() {
        OutputNumber outputNumber = new OutputNumber(0);

        InputNumber result = dividableByThreeAi.calculateNextInputNumberFor(outputNumber);

        assertEquals("Closest divisible by three number for 0 should be 0.", new InputNumber(0), result);
    }
}