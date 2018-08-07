package com.challenge.game;

import com.challenge.game.domain.InputNumber;
import com.challenge.game.domain.OutputNumber;
import org.junit.Test;

import java.util.InputMismatchException;

import static org.junit.Assert.*;

public class GameRoundTest {

    @Test(expected = InputMismatchException.class)
    public void initialiseGameRoundWithANumberBelowLimitShouldThrow() {
        new GameRound(new InputNumber(1));
    }

    @Test(expected = InputMismatchException.class)
    public void initialiseGameRoundWithANumberBelowZeroShouldThrow() {
        new GameRound(new InputNumber(-1));
    }

    @Test
    public void gameRoundWithValidInputShouldReturnValidOutput() {
        GameRound gameRound = new GameRound(new InputNumber(9));

        assertEquals("GameRound with input 9 should return 3 as output.", new OutputNumber(3), gameRound.getOutput());
    }

    @Test
    public void gameRoundWithValidInputShouldReturnValidOutput2() {
        GameRound gameRound = new GameRound(new InputNumber(10));

        assertEquals("GameRound with input 10 should return 3 as output.", new OutputNumber(3), gameRound.getOutput());
    }

    @Test
    public void gameRoundWithValidInputShouldReturnValidOutput3() {
        GameRound gameRound = new GameRound(new InputNumber(11));

        assertEquals("GameRound with input 11 should return 4 as output.", new OutputNumber(4), gameRound.getOutput());
    }

    @Test
    public void gameRoundWith2AsInputShouldWinGame() {
        GameRound gameRound = new GameRound(new InputNumber(2));

        assertTrue("GameRound with input 2 should win the gameRound.", gameRound.isWinner());
        assertEquals("GameRound with winning input value should return output value as 1", new OutputNumber(1), gameRound.getOutput());
    }

    @Test
    public void gameRoundWith3AsInputShouldWinGame() {
        GameRound gameRound = new GameRound(new InputNumber(3));

        assertTrue("GameRound with input 3 should win the gameRound.", gameRound.isWinner());
        assertEquals("GameRound with winning input value should return output value as 1", new OutputNumber(1), gameRound.getOutput());
    }

    @Test
    public void gameRoundWith4AsInputShouldWinGame() {
        GameRound gameRound = new GameRound(new InputNumber(4));

        assertTrue("GameRound with input 4 should win the gameRound.", gameRound.isWinner());
        assertEquals("GameRound with winning input value should return output value as 1", new OutputNumber(1), gameRound.getOutput());
    }

    @Test
    public void gameRoundWith5AsInputShouldNotWinGame() {
        GameRound gameRound = new GameRound(new InputNumber(5));

        assertFalse("GameRound with input 5 should NOT win the gameRound.", gameRound.isWinner());
        assertNotEquals("GameRound with NOT winning input value should NOT return output value as 1", new OutputNumber(1), gameRound.getOutput());
    }
}