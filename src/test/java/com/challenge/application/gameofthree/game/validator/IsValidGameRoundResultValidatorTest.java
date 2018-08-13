package com.challenge.application.gameofthree.game.validator;

import com.challenge.application.gameofthree.exception.ValidationException;
import com.challenge.application.gameofthree.game.Game;
import com.challenge.application.gameofthree.gameround.domain.GameRoundResult;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IsValidGameRoundResultValidatorTest {

    private IsValidGameRoundResultValidator canPlayGameValidator;

    private Game gameMock;
    private GameRoundResult gameRoundResultMock;

    @Before
    public void setUp() throws Exception {
        canPlayGameValidator = new IsValidGameRoundResultValidator();

        gameMock = mock(Game.class);
        gameRoundResultMock = mock(GameRoundResult.class);

        when(gameRoundResultMock.toString()).thenReturn("");
    }

    @Test
    public void shouldInvalidateGameWithInvalidGameRoundResult() {
        when(gameMock.getGameRoundResult()).thenReturn(gameRoundResultMock);
        when(gameRoundResultMock.canPlayAgain()).thenReturn(false);

        boolean result = canPlayGameValidator.validate(gameMock);

        assertFalse("Should invalidate game with invalid game round result.", result);
        assertThat("Should set messages when invalidate game with invalid game round result.",
                canPlayGameValidator.getValidationMessages(),
                Matchers.containsInAnyOrder(IsValidGameRoundResultValidator.INVALID_GAME_ROUND_STATE_MSG));
    }

    @Test
    public void shouldValidateGameWithGoodGameRoundResult() {
        when(gameMock.getGameRoundResult()).thenReturn(gameRoundResultMock);
        when(gameRoundResultMock.canPlayAgain()).thenReturn(true);

        boolean result = canPlayGameValidator.validate(gameMock);

        assertTrue("Should validate game with good game round result.", result);
        assertThat("Should not set messages when game is valid for playing.",
                canPlayGameValidator.getValidationMessages(), empty());
    }

    @Test(expected = ValidationException.class)
    public void shouldThrowWhenValidatingGameWithInvalidGameRoundResult() {
        when(gameMock.getGameRoundResult()).thenReturn(gameRoundResultMock);
        when(gameRoundResultMock.canPlayAgain()).thenReturn(false);

        canPlayGameValidator.validateOrThrow(gameMock);

        fail("Should have thrown ValidationException.");
    }

    @Test
    public void shouldNotThrowWhenGameWithGoodRoundResult() {
        when(gameMock.getGameRoundResult()).thenReturn(gameRoundResultMock);
        when(gameRoundResultMock.canPlayAgain()).thenReturn(true);

        canPlayGameValidator.validateOrThrow(gameMock);

        assertTrue("Should not throw exception when validating a valid game with good game round result.", true);
    }

    public void validationMessagesAtInitialization() {
        assertTrue("Validation messages should be empty at initialization.", canPlayGameValidator.getValidationMessages().isEmpty());
    }
}