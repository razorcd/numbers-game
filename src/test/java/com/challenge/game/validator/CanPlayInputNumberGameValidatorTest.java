package com.challenge.game.validator;

import com.challenge.game.Game;
import com.challenge.game.domain.GameRoundResult;
import com.challenge.game.domain.InputNumber;
import com.challenge.game.exception.ValidationException;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CanPlayInputNumberGameValidatorTest {

    private CanPlayInputNumberGameValidator canPlayInputNumberGameValidator;
    private InputNumber inputNumber;
    private GameRoundResult gameRoundResult;
    private Game game;

    @Before
    public void setUp() throws Exception {
        inputNumber = mock(InputNumber.class);
        gameRoundResult = mock(GameRoundResult.class);
        game = mock(Game.class);

        canPlayInputNumberGameValidator = new CanPlayInputNumberGameValidator(inputNumber);

        when(inputNumber.toString()).thenReturn("42");
        when(gameRoundResult.toString()).thenReturn("game round X");
    }

    @Test
    public void shouldInvalidateWhenNumberCanNotBePlayedForGame() {
        when(game.getGameRoundResult()).thenReturn(gameRoundResult);
        when(inputNumber.canPlayNumberAfter(gameRoundResult)).thenReturn(false);

        boolean result = canPlayInputNumberGameValidator.validate(game);

        assertFalse("Should invalidate input number when number can not be played against the game.", result);
        assertThat("Should set messages when number can not be played against the game.",
                canPlayInputNumberGameValidator.getValidationMessages(),
                containsInAnyOrder("can not play 42 after game round X"));
    }

    @Test
    public void shouldValidateWhenNumberCanBePlayedForGame() {
        when(game.getGameRoundResult()).thenReturn(gameRoundResult);
        when(inputNumber.canPlayNumberAfter(gameRoundResult)).thenReturn(true);

        boolean result = canPlayInputNumberGameValidator.validate(game);

        assertTrue("Should validate input number when number can be played against the game.", result);
        assertThat("Should not set messages when number can be played against the game.",
                canPlayInputNumberGameValidator.getValidationMessages(), empty());
    }

    @Test(expected = ValidationException.class)
    public void shouldThrowWhenNumberCanNotBePlayedForGame() {
        when(game.getGameRoundResult()).thenReturn(gameRoundResult);
        when(inputNumber.canPlayNumberAfter(gameRoundResult)).thenReturn(false);

        canPlayInputNumberGameValidator.validateOrThrow(game);

        fail("Should have thrown ValidationException.");
    }

    public void validationMessagesAtInitialization() {
        assertTrue("Validation messages should be empty at initialization.",
                canPlayInputNumberGameValidator.getValidationMessages().isEmpty());
    }
}