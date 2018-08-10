package com.challenge.game;

import com.challenge.game.domain.GameRoundResult;
import com.challenge.game.domain.InputNumber;
import com.challenge.game.domain.PlayerAggregate;
import com.challenge.game.exception.GameException;
import com.challenge.game.model.Player;
import com.challenge.game.service.GameRoundService;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameTest {

    private GameRoundService gameRoundServiceMock;

    @Before
    public void setUp() throws Exception {
        gameRoundServiceMock = mock(GameRoundService.class);
    }

    @Test(expected = GameException.class)
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
        assertEquals("New game should hold the player aggregate.", playerAggregate, game.getPlayerAggregate());
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
        assertEquals("New game should hold new player aggregate with next player.", playerAggregate.getNext(), nextGame.getPlayerAggregate());
    }

    @Test(expected = GameException.class)
    public void gameShouldNotPlayInvalidInitialInput() {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        PlayerAggregate playerAggregate = new PlayerAggregate(Arrays.asList(player1, player2), 0);
        InputNumber inputNumber = new InputNumber(0);

        when(gameRoundServiceMock.play(inputNumber)).thenThrow(GameException.class);


        Game nextGgame = new Game(gameRoundServiceMock, playerAggregate).play(inputNumber);
    }

    @Test(expected = GameException.class)
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
}