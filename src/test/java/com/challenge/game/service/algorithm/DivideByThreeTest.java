package com.challenge.game.service.algorithm;

import com.challenge.game.domain.InputNumber;
import com.challenge.game.domain.OutputNumber;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DivideByThreeTest {

    private IGameAlgorithm divideByThree;

    @Before
    public void setUp() throws Exception {
        divideByThree = new DivideByThree();
    }


    @Test
    public void when6ShouldReturn2() {
        OutputNumber result = divideByThree.apply(new InputNumber(6));

        assertEquals("When 6 it should return 2.", new OutputNumber(2), result);
    }

    @Test
    public void when7ShouldReturn2() {
        OutputNumber result = divideByThree.apply(new InputNumber(7));

        assertEquals("When 7 it should return 2.", new OutputNumber(2), result);
    }

    @Test
    public void when8ShouldReturn3() {
        OutputNumber result = divideByThree.apply(new InputNumber(8));

        assertEquals("When 8 it should return 3.", new OutputNumber(3), result);
    }

    @Test
    public void when0ShouldReturn0() {
        OutputNumber result = divideByThree.apply(new InputNumber(0));

        assertEquals("When 0 it should return 0.", new OutputNumber(0), result);
    }
}