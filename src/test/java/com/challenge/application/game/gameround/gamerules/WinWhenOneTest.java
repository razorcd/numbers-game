package com.challenge.application.game.gameround.gamerules;

import com.challenge.application.game.domain.OutputNumber;
import com.challenge.application.game.gameround.gamerules.gamewinlogic.IGameWinLogic;
import com.challenge.application.game.gameround.gamerules.gamewinlogic.WinWhenOne;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WinWhenOneTest {

    private IGameWinLogic winWhenOne;

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