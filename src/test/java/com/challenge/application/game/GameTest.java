package com.challenge.application.game;

import com.challenge.application.game.domain.GameRoundResult;
import com.challenge.application.game.domain.InputNumber;
import com.challenge.application.game.domain.PlayerAggregate;
import com.challenge.application.game.exception.GameException;
import com.challenge.application.game.model.Player;
import com.challenge.application.game.service.GameRoundService;
import com.challenge.application.game.validator.CanPlayGameValidator;
import com.challenge.application.game.validator.NewGameValidator;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameTest {

    private GameRoundService gameRoundServiceMock;

    @Before
    public void setUp() throws Exception {
        gameRoundServiceMock = mock(GameRoundService.class);
    }

    @Test
    public void gameShouldInitialize() {
        Player player1 = new Player("1", "player1");
        Player player2 = new Player("2", "player2");
        PlayerAggregate playerAggregate = new PlayerAggregate(Arrays.asList(player1, player2), 0);

        Game game = new Game(gameRoundServiceMock, playerAggregate);

        assertEquals("New game should have NULL round result.", GameRoundResult.NULL, game.getGameRoundResult());
        assertEquals("New game should hold the player aggregate.", playerAggregate, game.getPlayerAggregate());
    }

    @Test
    public void gameShouldInvalidate_withNewGameValidator_whenGameInitializedWithInvalidAggregate() {
        Player player1 = new Player("1", "player1");
        PlayerAggregate playerAggregate = new PlayerAggregate(Arrays.asList(player1), 0);
        Game game = new Game(gameRoundServiceMock, playerAggregate);
        assertFalse("Game should invalidate with NewGameValidator when game initialized with invalid playerAggregate",
                game.validate(new NewGameValidator()));
    }

    @Test
    public void gameShouldPlay() {
        Player player1 = new Player("1", "player1");
        Player player2 = new Player("1", "player2");
        PlayerAggregate playerAggregate = new PlayerAggregate(Arrays.asList(player1, player2), 0);
        InputNumber inputNumber = new InputNumber(100);

        GameRoundResult gameRoundResultDummy = mock(GameRoundResult.class);
        when(gameRoundServiceMock.play(inputNumber)).thenReturn(gameRoundResultDummy);

        Game nextGame = new Game(gameRoundServiceMock, playerAggregate).play(inputNumber);

        assertEquals("New game should have a round result.", nextGame.getGameRoundResult(), gameRoundResultDummy);
        assertEquals("New game should hold new player aggregate with next player.", playerAggregate.getNext(), nextGame.getPlayerAggregate());
    }

    @Test(expected = GameException.class)
    public void gameShouldNotPlayInvalidInitialInput() {
        Player player1 = new Player("1", "player1");
        Player player2 = new Player("2", "player2");
        PlayerAggregate playerAggregate = new PlayerAggregate(Arrays.asList(player1, player2), 0);
        InputNumber inputNumber = new InputNumber(0);

        when(gameRoundServiceMock.play(inputNumber)).thenThrow(GameException.class);

        Game nextGgame = new Game(gameRoundServiceMock, playerAggregate).play(inputNumber);
    }

    @Test
    public void gameShouldInvalidate_withCanPlayGameValidator_afterWinning() {
        Player player1 = new Player("1", "player1");
        Player player2 = new Player("2", "player2");
        PlayerAggregate playerAggregate = new PlayerAggregate(Arrays.asList(player1, player2), 0);
        InputNumber inputNumber = new InputNumber(0);

        GameRoundResult gameRoundResult = new GameRoundResult(null, true);
        when(gameRoundServiceMock.play(inputNumber)).thenReturn(gameRoundResult);

        Game nextGgame = new Game(gameRoundServiceMock, playerAggregate).play(inputNumber);

        boolean validGameForPlaying = nextGgame.validate(new CanPlayGameValidator());

        assertFalse("Should invalidate with CanPlayGameValidator after winning.", validGameForPlaying);

        nextGgame.play(new InputNumber(5));
    }
}