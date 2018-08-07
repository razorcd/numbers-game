package com.challenge.game;

import com.challenge.game.algorithm.DivideByThree;
import com.challenge.game.algorithm.WinWhenOne;
import com.challenge.game.domain.GameRoundResult;
import com.challenge.game.domain.InputNumber;
import com.challenge.game.domain.OutputNumber;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {

    private Game game;

    @Before
    public void setUp() throws Exception {
        game = new Game(new DivideByThree(), new WinWhenOne());
    }


    @Test(expected = IllegalArgumentException.class)
    public void playGameWithANumberBelowLimitShouldThrow() {
        game.play(new InputNumber(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void playGameWithANumberBelowZeroShouldThrow() {
        game.play(new InputNumber(-1));
    }

    @Test
    public void playGameWithValidInputShouldReturnValidOutput() {
        GameRoundResult gameRoundResult = game.play(new InputNumber(9));

        assertEquals("Game with input 9 should return 3 as output.", new OutputNumber(3), gameRoundResult.getOutputNumber());
    }

    @Test
    public void playGameWithValidInputShouldReturnValidOutput2() {
        GameRoundResult gameRoundResult = game.play(new InputNumber(10));

        assertEquals("Game with input 10 should return 3 as output.", new OutputNumber(3), gameRoundResult.getOutputNumber());
    }

    @Test
    public void playGameWithValidInputShouldReturnValidOutput3() {
        GameRoundResult gameRoundResult = game.play(new InputNumber(11));

        assertEquals("Game with input 11 should return 4 as output.", new OutputNumber(4), gameRoundResult.getOutputNumber());
    }

    @Test
    public void playGameWith2AsInputShouldWinGame() {
        GameRoundResult gameRoundResult = game.play(new InputNumber(2));

        assertTrue("Game with input 2 should win the game.", gameRoundResult.isWinner());
        assertEquals("Game with winning input value should return output value as 1", new OutputNumber(1), gameRoundResult.getOutputNumber());
    }

    @Test
    public void playGameWith3AsInputShouldWinGame() {
        GameRoundResult gameRoundResult = game.play(new InputNumber(3));

        assertTrue("Game with input 3 should win the game.", gameRoundResult.isWinner());
        assertEquals("Game with winning input value should return output value as 1", new OutputNumber(1), gameRoundResult.getOutputNumber());
    }

    @Test
    public void playGameWith4AsInputShouldWinGame() {
        GameRoundResult gameRoundResult = game.play(new InputNumber(4));

        assertTrue("Game with input 4 should win the game.", gameRoundResult.isWinner());
        assertEquals("Game with winning input value should return output value as 1", new OutputNumber(1), gameRoundResult.getOutputNumber());
    }

    @Test
    public void playGameWith5AsInputShouldNotWinGame() {
        GameRoundResult gameRoundResult = game.play(new InputNumber(5));

        assertFalse("Game with input 5 should NOT win the game.", gameRoundResult.isWinner());
        assertNotEquals("Game with NOT winning input value should NOT return output value as 1", new OutputNumber(1), gameRoundResult.getOutputNumber());
    }
}