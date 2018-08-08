package com.challenge.game.service;

import com.challenge.game.service.algorithm.DivideByThree;
import com.challenge.game.service.algorithm.WinWhenOne;
import com.challenge.game.domain.GameRoundResult;
import com.challenge.game.domain.InputNumber;
import com.challenge.game.domain.OutputNumber;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameRoundServiceTest {

    private GameRoundService gameRoundService;

    @Before
    public void setUp() throws Exception {
        gameRoundService = new GameRoundService(new DivideByThree(), new WinWhenOne());
    }


    @Test(expected = IllegalArgumentException.class)
    public void playGameWithANumberBelowLimitShouldThrow() {
        gameRoundService.play(new InputNumber(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void playGameWithANumberBelowZeroShouldThrow() {
        gameRoundService.play(new InputNumber(-1));
    }

    @Test
    public void playGameWithValidInputShouldReturnValidOutput() {
        GameRoundResult gameRoundResult = gameRoundService.play(new InputNumber(9));

        assertEquals("GameRoundService with input 9 should return 3 as output.", new OutputNumber(3), gameRoundResult.getOutputNumber());
    }

    @Test
    public void playGameWithValidInputShouldReturnValidOutput2() {
        GameRoundResult gameRoundResult = gameRoundService.play(new InputNumber(10));

        assertEquals("GameRoundService with input 10 should return 3 as output.", new OutputNumber(3), gameRoundResult.getOutputNumber());
    }

    @Test
    public void playGameWithValidInputShouldReturnValidOutput3() {
        GameRoundResult gameRoundResult = gameRoundService.play(new InputNumber(11));

        assertEquals("GameRoundService with input 11 should return 4 as output.", new OutputNumber(4), gameRoundResult.getOutputNumber());
    }

    @Test
    public void playGameWith2AsInputShouldWinGame() {
        GameRoundResult gameRoundResult = gameRoundService.play(new InputNumber(2));

        assertTrue("GameRoundService with input 2 should win the gameRoundService.", gameRoundResult.isWinner());
        assertEquals("GameRoundService with winning input value should return output value as 1", new OutputNumber(1), gameRoundResult.getOutputNumber());
    }

    @Test
    public void playGameWith3AsInputShouldWinGame() {
        GameRoundResult gameRoundResult = gameRoundService.play(new InputNumber(3));

        assertTrue("GameRoundService with input 3 should win the gameRoundService.", gameRoundResult.isWinner());
        assertEquals("GameRoundService with winning input value should return output value as 1", new OutputNumber(1), gameRoundResult.getOutputNumber());
    }

    @Test
    public void playGameWith4AsInputShouldWinGame() {
        GameRoundResult gameRoundResult = gameRoundService.play(new InputNumber(4));

        assertTrue("GameRoundService with input 4 should win the gameRoundService.", gameRoundResult.isWinner());
        assertEquals("GameRoundService with winning input value should return output value as 1", new OutputNumber(1), gameRoundResult.getOutputNumber());
    }

    @Test
    public void playGameWith5AsInputShouldNotWinGame() {
        GameRoundResult gameRoundResult = gameRoundService.play(new InputNumber(5));

        assertFalse("GameRoundService with input 5 should NOT win the gameRoundService.", gameRoundResult.isWinner());
        assertNotEquals("GameRoundService with NOT winning input value should NOT return output value as 1", new OutputNumber(1), gameRoundResult.getOutputNumber());
    }
}