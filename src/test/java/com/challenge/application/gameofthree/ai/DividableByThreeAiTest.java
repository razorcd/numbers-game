package com.challenge.application.gameofthree.ai;

import com.challenge.application.gameofthree.game.domain.InputNumber;
import com.challenge.application.gameofthree.game.domain.OutputNumber;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DividableByThreeAiTest {

    private DivideByThreeAi dividableByThreeAi;

    @Before
    public void setUp() throws Exception {
        dividableByThreeAi = new DivideByThreeAi();
    }

    @Test
    public void shouldCalculateClosestAdditionForDividableByThreeWhen10() {
        OutputNumber outputNumber = new OutputNumber(10);

        InputNumber result = dividableByThreeAi.calculateNextInputNumberFor(outputNumber);

        assertEquals("Closest addition dividable by three for 10 should be -1.", new InputNumber(-1), result);
    }

    @Test
    public void shouldCalculateClosestAdditionForDividableByThreeWhen11() {
        OutputNumber outputNumber = new OutputNumber(11);

        InputNumber result = dividableByThreeAi.calculateNextInputNumberFor(outputNumber);

        assertEquals("Closest addition dividable by three for 10 should be 1.", new InputNumber(1), result);
    }

    @Test
    public void shouldCalculateClosestAdditionForDividableByThreeWhen12() {
        OutputNumber outputNumber = new OutputNumber(12);

        InputNumber result = dividableByThreeAi.calculateNextInputNumberFor(outputNumber);

        assertEquals("Closest addition dividable by three for 12 should be 0.", new InputNumber(0), result);
    }

    @Test
    public void shouldCalculateClosestAdditionForDividableByThreeWhen0() {
        OutputNumber outputNumber = new OutputNumber(0);

        InputNumber result = dividableByThreeAi.calculateNextInputNumberFor(outputNumber);

        assertEquals("Closest addition dividable by three for 0 should be 0.", new InputNumber(0), result);
    }
}