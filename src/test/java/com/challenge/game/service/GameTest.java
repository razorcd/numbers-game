package com.challenge.game.service;

import com.challenge.game.service.algorithm.DivideByThree;
import com.challenge.game.service.algorithm.WinWhenOne;
import com.challenge.game.service.domain.InputNumber;
import com.challenge.game.service.domain.OutputNumber;
import com.challenge.game.service.domain.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {

    private Game game;

    @Before
    public void setUp() throws Exception {
        GameRoundService gameRoundService = new GameRoundService(new DivideByThree(), new WinWhenOne());
        game = new Game(gameRoundService);
    }

    @Test
    public void shouldInitialiseTheGameInIdleState() {
        assertEquals("New game should be in IDLE state.", GameState.IDLE, game.getCurrentState());
    }

    @Test(expected = RuntimeException.class)
    public void gameWithoutPlayersShouldNotStart() {
        game.start();
    }

    @Test(expected = RuntimeException.class)
    public void gameWithOnePlayerShouldNotStart() {
        game.addPlayer(new Player("player1"));
        game.start();
    }

    @Test
    public void gameWithTwoPlayersShouldStart() {
        game.addPlayer(new Player("player1"));
        game.addPlayer(new Player("player2"));
        game.start();

        assertEquals("Started game should be in PLAYING state.", GameState.PLAYING, game.getCurrentState());
    }

    @Test
    public void startedGameShouldHaveCurrentUserTheFirstPlayer() {
        Player player1 = new Player("player1");
        game.addPlayer(player1);
        game.addPlayer(new Player("player2"));
        game.start();

        assertEquals("Started game should have current player as Player 1.", player1, game.getCurrentPlayer());
    }

    @Test
    public void startedGameShouldPlayOneRound() {
        Player player2 = new Player("player2");
        game.addPlayer(new Player("player1"));
        game.addPlayer(player2);
        game.start();
        game.play(new InputNumber(100));

        assertEquals("Played started game should have current player as Player 2.", player2, game.getCurrentPlayer());
        assertEquals("Played started game should be in PLAYING state.", GameState.PLAYING, game.getCurrentState());
    }

    @Test
    public void playingMultipleTimesShouldAlternatePlayers() {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.start();
        game.play(new InputNumber(100));

        assertEquals("When playing once, game should have current player as Player 2.", player2, game.getCurrentPlayer());
        assertEquals("When playing once, game should be in PLAYING state.", GameState.PLAYING, game.getCurrentState());

        game.play(new InputNumber(33));

        assertEquals("When playing twice, game should have current player as Player 1.", player1, game.getCurrentPlayer());
        assertEquals("When playing twice, game should be in PLAYING state.", GameState.PLAYING, game.getCurrentState());

        game.play(new InputNumber(11));

        assertEquals("When playing 3 times, game should have current player as Player 1.", player2, game.getCurrentPlayer());
        assertEquals("When playing 3 times, game should be in PLAYING state.", GameState.PLAYING, game.getCurrentState());
    }

    @Test(expected = IllegalArgumentException.class)
    public void playingWrongFirstInputShouldThrow() {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.start();
        game.play(new InputNumber(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void playingWrongInputShouldThrow() {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.start();
        game.play(new InputNumber(100));
        game.play(new InputNumber(50));
    }

    @Test
    public void gameShouldStopAfterPlayingAWinningRound() {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.start();
        game.play(new InputNumber(3));

        assertEquals("Game state should be FINISHED after playing a winning round.", GameState.FINISHED, game.getCurrentState());
    }

    @Test
    public void gameShouldHoldCurrentPlayerAfterPlayingAWinningRound() {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.start();
        game.play(new InputNumber(3));

        assertEquals("Game current player should be same after playing a winning round.", player1, game.getCurrentPlayer());
    }

    @Test(expected = RuntimeException.class)
    public void gameShouldNotBePlayableAfterPlayingAWinnignRound() {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.start();
        game.play(new InputNumber(3));
        game.play(new InputNumber(1));
    }

    @Test
    public void gameLastRoundResoultShouldBeAWinnerAfterPlayingAWinnignRound() {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.start();
        game.play(new InputNumber(3));

        assertEquals("Game last round result should be a winner after playing a winning round.", true, game.getLastRoundResult().isWinner());
        assertEquals("Game last round result should be 1 after playing a winning round.", new OutputNumber(1), game.getLastRoundResult().getOutputNumber());

    }

}