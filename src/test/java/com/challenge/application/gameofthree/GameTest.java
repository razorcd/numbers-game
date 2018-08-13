package com.challenge.application.gameofthree;

import com.challenge.application.gameofthree.exception.GameException;
import com.challenge.application.gameofthree.game.Game;
import com.challenge.application.gameofthree.game.domain.InputNumber;
import com.challenge.application.gameofthree.game.domain.OutputNumber;
import com.challenge.application.gameofthree.game.domain.PlayerAggregate;
import com.challenge.application.gameofthree.game.validator.NewGameValidator;
import com.challenge.application.gameofthree.gameround.GameRoundService;
import com.challenge.application.gameofthree.gameround.domain.GameRoundInput;
import com.challenge.application.gameofthree.gameround.domain.GameRoundResult;
import com.challenge.application.gameofthree.model.Human;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class GameTest {

    private Game game;
    private GameRoundService gameRoundServiceMock;

    @Before
    public void setUp() throws Exception {
        gameRoundServiceMock = mock(GameRoundService.class);
    }


    @Test
    public void gameShouldAddPlayers() {
        Human player1 = new Human("1", "player1");

        Game game = new Game(gameRoundServiceMock);

        Game newGame = game.addPlayer(player1);

        assertEquals("Game should add players.", player1, newGame.getPlayerAggregate().getRootPlayer());
    }


    @Test
    public void gameShouldAddMultiplePlayers() {
        Human player1 = new Human("1", "player1");
        Human player2 = new Human("2", "player2");
        Human player3 = new Human("3", "player3");

        Game lastGame = Stream.of(player1, player2, player3)
                .reduce(new Game(gameRoundServiceMock), Game::addPlayer, (a, b) -> null);

        assertEquals("Game should add player1 to game.", player1, lastGame.getPlayerAggregate().getRootPlayer());
        assertEquals("Game should add player2 to game.", player2, lastGame.getPlayerAggregate().next().getRootPlayer());
        assertEquals("Game should add player3 to game.", player3, lastGame.getPlayerAggregate().next().next().getRootPlayer());
    }

    @Test
    public void gameShouldInvalidate_withNewGameValidator_whenGameInitializedWithInvalidAggregate() {
        Human player1 = new Human("1", "player1");

        Game game = new Game(gameRoundServiceMock)
                .addPlayer(player1);

        assertFalse("Game should invalidate with NewGameValidator when game initialized with invalid playerAggregate",
                game.validate(new NewGameValidator()));
    }

    @Test
    public void gameShouldValidate_withNewGameValidator_whenGameInitializedWithValidAggregate() {
        Human player1 = new Human("1", "player1");
        Human player2 = new Human("2", "player2");

        Game game = new Game(gameRoundServiceMock)
                .addPlayer(player1)
                .addPlayer(player2);

        assertTrue("Game should validate with NewGameValidator when game initialized with valid playerAggregate",
                game.validate(new NewGameValidator()));
    }

    @Test
    public void gameShouldStartGameAndHaveOutputNumber() {
        Human player1 = new Human("1", "player1");
        Human player2 = new Human("1", "player2");

        Game game = new Game(gameRoundServiceMock)
                .addPlayer(player1)
                .addPlayer(player2)
                .startGame();

        assertEquals("New game should have a new round result with output.", new OutputNumber(64), game.getGameRoundResult().getOutputNumber());
        assertEquals("New game should hold new player aggregate with next player.", player1, game.getPlayerAggregate().getRootPlayer());
    }

    @Test
    public void gameShouldPlayWithHumans() {
        Human player1 = new Human("1", "player1");
        Human player2 = new Human("1", "player2");

        Game game = new Game(gameRoundServiceMock)
                .addPlayer(player1)
                .addPlayer(player2);
//                .startGame();  //irrelevant while mocking

        PlayerAggregate playerAggregate = game.getPlayerAggregate();
        OutputNumber lastOutputNumber = game.getGameRoundResult().getOutputNumber();
        GameRoundInput gameRoundInput = new GameRoundInput(new InputNumber(1), lastOutputNumber);
        GameRoundResult gameRoundResultDummy = mock(GameRoundResult.class);
        when(gameRoundServiceMock.play(gameRoundInput)).thenReturn(gameRoundResultDummy);

        InputNumber inputNumberToPlay = new InputNumber(1);
        Game nextGame = game.play(inputNumberToPlay);

        assertEquals("New game should have a new round result.", nextGame.getGameRoundResult(), gameRoundResultDummy);
        assertEquals("New game should hold new player aggregate with next player.", playerAggregate.next(), nextGame.getPlayerAggregate());
    }

    @Test(expected = GameException.class)
    public void gameShouldThrowWhenPlayingInvalidInitialInput() {
        Human player1 = new Human("1", "player1");
        Human player2 = new Human("2", "player2");
        InputNumber inputNumberToPlay = new InputNumber(0);

        when(gameRoundServiceMock.play(any())).thenThrow(GameException.class);

        new Game(gameRoundServiceMock)
                .addPlayer(player1)
                .addPlayer(player2)
                .play(inputNumberToPlay);

        fail("Should have thrown exception when playing invalid initial value.");
    }
//
//    @Test
//    public void gameShouldInvalidate_withCanPlayGameValidator_afterWinning() {
//        Human player1 = new Human("1", "player1");
//        Human player2 = new Human("2", "player2");
//        InputNumber inputNumber = new InputNumber(0);
//
//        GameRoundResult gameRoundResult = new GameRoundResult(null, true);
//        when(gameRoundServiceMock.play(any())).thenReturn(gameRoundResult);
//
//        Game nextGame = new Game(gameRoundServiceMock)
//                .addPlayer(player1)
//                .addPlayer(player2)
//                .play(inputNumber);
//
//        boolean validGameForPlaying = nextGame.validate(new IsValidPlayerAggregateGameValidator());
//
//        assertFalse("Should invalidate with CanPlayGameValidator after winning.", validGameForPlaying);
//
//        nextGame.play(new InputNumber(5));
//    }
}