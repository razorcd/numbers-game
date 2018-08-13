
package com.challenge.application.gameofthree.game.validator;

import com.challenge.application.gameofthree.game.Game;
import com.challenge.application.gameofthree.gameround.domain.GameRoundResult;
import com.challenge.application.gameofthree.game.domain.PlayerAggregate;
import com.challenge.application.gameofthree.exception.ValidationException;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CanPlayGameValidatorTest {

    private CanPlayGameValidator canPlayGameValidator;

    private Game gameMock;
    private GameRoundResult gameRoundResultMock;
    private PlayerAggregate playerAggregateMock;

    @Before
    public void setUp() throws Exception {
        canPlayGameValidator = new CanPlayGameValidator();

        gameMock = mock(Game.class);
        gameRoundResultMock = mock(GameRoundResult.class);
        playerAggregateMock = mock(PlayerAggregate.class);

        when(gameRoundResultMock.toString()).thenReturn("");
        when(playerAggregateMock.toString()).thenReturn("");
    }

    @Test
    public void shouldInvalidateGameWithInvalidGameRoundResultAndInvalidPlayerAggregate() {
        when(gameMock.getGameRoundResult()).thenReturn(gameRoundResultMock);
        when(gameMock.getPlayerAggregate()).thenReturn(playerAggregateMock);
        when(gameRoundResultMock.canPlayAgain()).thenReturn(false);
        when(playerAggregateMock.isValid()).thenReturn(false);

        boolean result = canPlayGameValidator.validate(gameMock);

        assertFalse("Should invalidate game with invalid game round result and invalid player aggregate.", result);
        assertThat("Should set messages when invalidate game with invalid game round result and invalid player aggregate",
                canPlayGameValidator.getValidationMessages(),
                Matchers.containsInAnyOrder(CanPlayGameValidator.INVALID_GAME_ROUND_STATE_MSG, CanPlayGameValidator.INVALID_PLAYER_AGGREGATE_MSG));
    }

    @Test
    public void shouldInvalidateGameWithInvalidGameRoundResult() {
        when(gameMock.getGameRoundResult()).thenReturn(gameRoundResultMock);
        when(gameMock.getPlayerAggregate()).thenReturn(playerAggregateMock);
        when(gameRoundResultMock.canPlayAgain()).thenReturn(false);
        when(playerAggregateMock.isValid()).thenReturn(true);

        boolean result = canPlayGameValidator.validate(gameMock);

        assertFalse("Should invalidate game with invalid game round result.", result);
        assertThat("Should set messages when invalidate game with invalid game round result",
                canPlayGameValidator.getValidationMessages(),
                Matchers.containsInAnyOrder(CanPlayGameValidator.INVALID_GAME_ROUND_STATE_MSG));
    }

    @Test
    public void shouldInvalidateGameWithInvalidPlayerAggregate() {
        when(gameMock.getGameRoundResult()).thenReturn(gameRoundResultMock);
        when(gameMock.getPlayerAggregate()).thenReturn(playerAggregateMock);
        when(gameRoundResultMock.canPlayAgain()).thenReturn(true);
        when(playerAggregateMock.isValid()).thenReturn(false);

        boolean result = canPlayGameValidator.validate(gameMock);

        assertFalse("Should invalidate game with invalid player aggregate.", result);
        assertThat("Should set messages when invalid player aggregate",
                canPlayGameValidator.getValidationMessages(),
                Matchers.containsInAnyOrder(CanPlayGameValidator.INVALID_PLAYER_AGGREGATE_MSG));
    }

    @Test
    public void shouldValidateGameForPlaying() {
        when(gameMock.getGameRoundResult()).thenReturn(gameRoundResultMock);
        when(gameMock.getPlayerAggregate()).thenReturn(playerAggregateMock);
        when(gameRoundResultMock.canPlayAgain()).thenReturn(true);
        when(playerAggregateMock.isValid()).thenReturn(true);

        boolean result = canPlayGameValidator.validate(gameMock);

        assertTrue("Should validate game for playing.", result);
        assertThat("Should not set messages when game is valid for playing.",
                canPlayGameValidator.getValidationMessages(), empty());
    }

    @Test(expected = ValidationException.class)
    public void shouldThrowWhenValidateingGameWithInvalidGameRoundResultAndInvalidPlayerAggregate() {
        when(gameMock.getGameRoundResult()).thenReturn(gameRoundResultMock);
        when(gameMock.getPlayerAggregate()).thenReturn(playerAggregateMock);
        when(gameRoundResultMock.canPlayAgain()).thenReturn(false);
        when(playerAggregateMock.isValid()).thenReturn(false);

        canPlayGameValidator.validateOrThrow(gameMock);

        fail("Should have thrown ValidationException.");
    }

    @Test
    public void shouldNotThrowWhenGameIsValidForPlaying() {
        when(gameMock.getGameRoundResult()).thenReturn(gameRoundResultMock);
        when(gameMock.getPlayerAggregate()).thenReturn(playerAggregateMock);
        when(gameRoundResultMock.canPlayAgain()).thenReturn(true);
        when(playerAggregateMock.isValid()).thenReturn(true);

        canPlayGameValidator.validateOrThrow(gameMock);

        assertTrue("Should not throw exception when validating a valid game for playing.", true);
    }

    public void validationMessagesAtInitialization() {
        assertTrue("Validation messages should be empty at initialization.", canPlayGameValidator.getValidationMessages().isEmpty());
    }
}