package com.challenge.game;

import com.challenge.game.model.Player;
import com.challenge.game.service.GameRoundService;
import com.challenge.game.domain.GameRoundResult;
import com.challenge.game.domain.InputNumber;
import com.challenge.game.domain.PlayerAggregate;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameTest {

    private GameRoundService gameRoundServiceMock;

    @Before
    public void setUp() throws Exception {
        gameRoundServiceMock = mock(GameRoundService.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void gameShouldNotInitializeWithInvalidAggregate() {
        Player player1 = new Player("player1");
        PlayerAggregate playerAggregate = new PlayerAggregate(Arrays.asList(player1), 0);
        new Game(gameRoundServiceMock, playerAggregate);
    }

    @Test
    public void gameShouldInitialize() {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        PlayerAggregate playerAggregate = new PlayerAggregate(Arrays.asList(player1, player2), 0);

        Game game = new Game(gameRoundServiceMock, playerAggregate);

        assertEquals("New game should have NULL round result.", GameRoundResult.NULL, game.getGameRoundResult());
        assertEquals("New game should hold the player aggregate.", playerAggregate, game.getPlayers());
    }

    @Test
    public void gameShouldPlay() {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        PlayerAggregate playerAggregate = new PlayerAggregate(Arrays.asList(player1, player2), 0);
        InputNumber inputNumber = new InputNumber(100);

        GameRoundResult gameRoundResultDummy = mock(GameRoundResult.class);
        when(gameRoundServiceMock.play(inputNumber)).thenReturn(gameRoundResultDummy);

        Game nextGame = new Game(gameRoundServiceMock, playerAggregate).play(inputNumber);

        assertEquals("New game should have a round result.", nextGame.getGameRoundResult(), gameRoundResultDummy);
        assertEquals("New game should hold new player aggregate with next player.", playerAggregate.getNext(), nextGame.getPlayers());
    }

    @Test(expected = IllegalArgumentException.class)
    public void gameShouldNotPlayInvalidInitialInput() {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        PlayerAggregate playerAggregate = new PlayerAggregate(Arrays.asList(player1, player2), 0);
        InputNumber inputNumber = new InputNumber(0);

        when(gameRoundServiceMock.play(inputNumber)).thenThrow(IllegalArgumentException.class);

        Game nextGgame = new Game(gameRoundServiceMock, playerAggregate).play(inputNumber);
    }

    @Test(expected = IllegalStateException.class)
    public void gameShouldNotPlayAfterWinn() {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        PlayerAggregate playerAggregate = new PlayerAggregate(Arrays.asList(player1, player2), 0);
        InputNumber inputNumber = new InputNumber(0);

        GameRoundResult gameRoundResult = new GameRoundResult(null, true);
        when(gameRoundServiceMock.play(inputNumber)).thenReturn(gameRoundResult);

        Game nextGgame = new Game(gameRoundServiceMock, playerAggregate).play(inputNumber);

        nextGgame.play(new InputNumber(5));
    }





//
//    @Test
//    public void shouldInitialiseTheGameInIdleState() {
//        assertEquals("New game should be in IDLE state.", GameState.IDLE, game.getCurrentState());
//    }
//
//    @Test(expected = RuntimeException.class)
//    public void gameWithoutPlayersShouldNotStart() {
//        game.start();
//    }
//
//    @Test(expected = RuntimeException.class)
//    public void gameWithOnePlayerShouldNotStart() {
//        game.addPlayer(new Player("player1"));
//        game.start();
//    }
//
//    @Test
//    public void gameWithTwoPlayersShouldStart() {
//        game.addPlayer(new Player("player1"));
//        game.addPlayer(new Player("player2"));
//        game.start();
//
//        assertEquals("Started game should be in PLAYING state.", GameState.PLAYING, game.getCurrentState());
//    }
//
//    @Test
//    public void startedGameShouldHaveCurrentUserTheFirstPlayer() {
//        Player player1 = new Player("player1");
//        game.addPlayer(player1);
//        game.addPlayer(new Player("player2"));
//        game.start();
//
//        assertEquals("Started game should have current player as Player 1.", player1, game.getCurrentPlayer());
//    }
//
//    @Test
//    public void startedGameShouldPlayOneRound() {
//        Player player2 = new Player("player2");
//        game.addPlayer(new Player("player1"));
//        game.addPlayer(player2);
//        game.start();
//        game.play(new InputNumber(100));
//
//        assertEquals("Played started game should have current player as Player 2.", player2, game.getCurrentPlayer());
//        assertEquals("Played started game should be in PLAYING state.", GameState.PLAYING, game.getCurrentState());
//    }
//
//    @Test
//    public void playingMultipleTimesShouldAlternatePlayers() {
//        Player player1 = new Player("player1");
//        Player player2 = new Player("player2");
//        game.addPlayer(player1);
//        game.addPlayer(player2);
//        game.start();
//        game.play(new InputNumber(100));
//
//        assertEquals("When playing once, game should have current player as Player 2.", player2, game.getCurrentPlayer());
//        assertEquals("When playing once, game should be in PLAYING state.", GameState.PLAYING, game.getCurrentState());
//
//        game.play(new InputNumber(33));
//
//        assertEquals("When playing twice, game should have current player as Player 1.", player1, game.getCurrentPlayer());
//        assertEquals("When playing twice, game should be in PLAYING state.", GameState.PLAYING, game.getCurrentState());
//
//        game.play(new InputNumber(11));
//
//        assertEquals("When playing 3 times, game should have current player as Player 1.", player2, game.getCurrentPlayer());
//        assertEquals("When playing 3 times, game should be in PLAYING state.", GameState.PLAYING, game.getCurrentState());
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void playingWrongFirstInputShouldThrow() {
//        Player player1 = new Player("player1");
//        Player player2 = new Player("player2");
//        game.addPlayer(player1);
//        game.addPlayer(player2);
//        game.start();
//        game.play(new InputNumber(1));
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void playingWrongInputShouldThrow() {
//        Player player1 = new Player("player1");
//        Player player2 = new Player("player2");
//        game.addPlayer(player1);
//        game.addPlayer(player2);
//        game.start();
//        game.play(new InputNumber(100));
//        game.play(new InputNumber(50));
//    }
//
//    @Test
//    public void gameShouldStopAfterPlayingAWinningRound() {
//        Player player1 = new Player("player1");
//        Player player2 = new Player("player2");
//        game.addPlayer(player1);
//        game.addPlayer(player2);
//        game.start();
//        game.play(new InputNumber(3));
//
//        assertEquals("Game state should be FINISHED after playing a winning round.", GameState.FINISHED, game.getCurrentState());
//    }
//
//    @Test
//    public void gameShouldHoldCurrentPlayerAfterPlayingAWinningRound() {
//        Player player1 = new Player("player1");
//        Player player2 = new Player("player2");
//        game.addPlayer(player1);
//        game.addPlayer(player2);
//        game.start();
//        game.play(new InputNumber(3));
//
//        assertEquals("Game current player should be same after playing a winning round.", player1, game.getCurrentPlayer());
//    }
//
//    @Test(expected = RuntimeException.class)
//    public void gameShouldNotBePlayableAfterPlayingAWinningRound() {
//        Player player1 = new Player("player1");
//        Player player2 = new Player("player2");
//        game.addPlayer(player1);
//        game.addPlayer(player2);
//        game.start();
//        game.play(new InputNumber(3));
//        game.play(new InputNumber(1));
//    }
//
//    @Test
//    public void gameLastRoundResoultShouldBeAWinnerAfterPlayingAWinningRound() {
//        Player player1 = new Player("player1");
//        Player player2 = new Player("player2");
//        game.addPlayer(player1);
//        game.addPlayer(player2);
//        game.start();
//        game.play(new InputNumber(16));
//        game.play(new InputNumber(5));
//        game.play(new InputNumber(2));
//
//        assertEquals("Game last round result should be a winner after playing a winning round.", true, game.getGameRoundResult().isWinner());
//        assertEquals("Game last round result should be 1 after playing a winning round.", new OutputNumber(1), game.getGameRoundResult().getOutputNumber());
//
//    }
}