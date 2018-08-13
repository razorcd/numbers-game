package com.challenge.application.game;

import com.challenge.application.game.domain.*;
import com.challenge.application.game.exception.GameException;
import com.challenge.application.game.gameround.GameRoundService;
import com.challenge.application.game.model.Human;
import com.challenge.application.game.validator.CanPlayGameValidator;
import com.challenge.application.game.validator.NewGameValidator;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

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
    public void gameShouldInitialize() {
        Human player1 = new Human("1", "player1");
        Human player2 = new Human("2", "player2");
        PlayerAggregate playerAggregate = new PlayerAggregate(Arrays.asList(player1, player2), 0);

        Game game = new Game(gameRoundServiceMock, playerAggregate);

        assertEquals("New game should have NULL round result.", GameRoundResult.NULL, game.getGameRoundResult());
        assertEquals("New game should hold the player aggregate.", playerAggregate, game.getPlayerAggregate());
    }

    @Test
    public void gameShouldInvalidate_withNewGameValidator_whenGameInitializedWithInvalidAggregate() {
        Human player1 = new Human("1", "player1");
        PlayerAggregate playerAggregate = new PlayerAggregate(Arrays.asList(player1), 0);
        Game game = new Game(gameRoundServiceMock, playerAggregate);
        assertFalse("Game should invalidate with NewGameValidator when game initialized with invalid playerAggregate",
                game.validate(new NewGameValidator()));
    }

    @Test
    public void gameShouldPlayWithHumans() {
        Human player1 = new Human("1", "player1");
        Human player2 = new Human("1", "player2");
        PlayerAggregate playerAggregate = new PlayerAggregate(Arrays.asList(player1, player2), 0);

        InputNumber inputNumberToPlay = new InputNumber(1);
        game = new Game(gameRoundServiceMock, playerAggregate);
        OutputNumber lastOutputNumber = game.getGameRoundResult().getOutputNumber();
        GameRoundInput gameRoundInput = new GameRoundInput(new InputNumber(1), lastOutputNumber);

        GameRoundResult gameRoundResultDummy = mock(GameRoundResult.class);
        when(gameRoundServiceMock.play(gameRoundInput)).thenReturn(gameRoundResultDummy);

        Game nextGame = game.play(inputNumberToPlay);

        assertEquals("New game should have a new round result.", nextGame.getGameRoundResult(), gameRoundResultDummy);
        assertEquals("New game should hold new player aggregate with next player.", playerAggregate.getNext(), nextGame.getPlayerAggregate());
    }

    @Test(expected = GameException.class)
    public void gameShouldThrowWhenPlayingInvalidInitialInput() {
        Human player1 = new Human("1", "player1");
        Human player2 = new Human("2", "player2");
        PlayerAggregate playerAggregate = new PlayerAggregate(Arrays.asList(player1, player2), 0);
        InputNumber inputNumberToPlay = new InputNumber(0);

        when(gameRoundServiceMock.play(any())).thenThrow(GameException.class);

        new Game(gameRoundServiceMock, playerAggregate).play(inputNumberToPlay);

        fail("Should have thrown exception when playing invalid initial value.");
    }

    @Test
    public void gameShouldInvalidate_withCanPlayGameValidator_afterWinning() {
        Human player1 = new Human("1", "player1");
        Human player2 = new Human("2", "player2");
        PlayerAggregate playerAggregate = new PlayerAggregate(Arrays.asList(player1, player2), 0);
        InputNumber inputNumber = new InputNumber(0);

        GameRoundResult gameRoundResult = new GameRoundResult(null, true);
        when(gameRoundServiceMock.play(any())).thenReturn(gameRoundResult);

        Game nextGame = new Game(gameRoundServiceMock, playerAggregate).play(inputNumber);

        boolean validGameForPlaying = nextGame.validate(new CanPlayGameValidator());

        assertFalse("Should invalidate with CanPlayGameValidator after winning.", validGameForPlaying);

        nextGame.play(new InputNumber(5));
    }
}