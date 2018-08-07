package com.challenge.game.service.algorithm;

import com.challenge.game.service.domain.OutputNumber;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WinWhenOneTest {

    private WinLogic winWhenOne;

    @Before
    public void setUp() throws Exception {
        winWhenOne = new WinWhenOne();
    }

    @Test
    public void whenOneItShouldReturnTrue() {
        assertTrue("Should be truthy when value is 1.", winWhenOne.apply(new OutputNumber(1)));
    }

    @Test
    public void whenNotOneItShouldReturnFalse() {
        assertFalse("Should be falsy when value is NOT 1.", winWhenOne.apply(new OutputNumber(5)));
    }
}