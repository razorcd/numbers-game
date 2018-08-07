package com.challenge.game;

import org.junit.Test;

import java.util.InputMismatchException;

import static org.junit.Assert.*;

public class GameTest {

    @Test(expected = InputMismatchException.class)
    public void initialiseGameWithANumberBelowLimitShouldThrow() {
        new Game(new InputNumber(1));
    }

    @Test(expected = InputMismatchException.class)
    public void initialiseGameWithANumberBelow0ShouldThrow() {
        new Game(new InputNumber(-1));
    }

    @Test
    public void gameWithValidInputShouldReturnValidOutput() {
        Game game = new Game(new InputNumber(9));

        assertEquals("Game with input 9 should return 3 as output.", 3, game.getOutput());
    }

    @Test
    public void gameWithValidInputShouldReturnValidOutput2() {
        Game game = new Game(new InputNumber(10));

        assertEquals("Game with input 10 should return 3 as output.", 3, game.getOutput());
    }

    @Test
    public void gameWithValidInputShouldReturnValidOutput3() {
        Game game = new Game(new InputNumber(11));

        assertEquals("Game with input 11 should return 4 as output.", 4, game.getOutput());
    }

    @Test
    public void gameWith2AsInputShouldWinGame() {
        Game game = new Game(new InputNumber(2));

        assertTrue("Game with input 2 should win the game.", game.isWinner());
        assertEquals("Game with winning input value should return output value as 1", 1, game.getOutput());
    }

    @Test
    public void gameWith3AsInputShouldWinGame() {
        Game game = new Game(new InputNumber(3));

        assertTrue("Game with input 3 should win the game.", game.isWinner());
        assertEquals("Game with winning input value should return output value as 1", 1, game.getOutput());
    }

    @Test
    public void gameWith4AsInputShouldWinGame() {
        Game game = new Game(new InputNumber(4));

        assertTrue("Game with input 4 should win the game.", game.isWinner());
        assertEquals("Game with winning input value should return output value as 1", 1, game.getOutput());
    }

    @Test
    public void gameWith5AsInputShouldNotWinGame() {
        Game game = new Game(new InputNumber(5));

        assertFalse("Game with input 5 should NOT win the game.", game.isWinner());
        assertNotEquals("Game with NOT winning input value should NOT return output value as 1", 1, game.getOutput());
    }
}