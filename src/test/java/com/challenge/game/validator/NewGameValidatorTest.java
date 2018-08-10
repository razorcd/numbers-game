package com.challenge.game.validator;

import com.challenge.game.Game;
import com.challenge.game.domain.PlayerAggregate;
import com.challenge.game.exception.ValidationException;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.util.regex.Matcher;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NewGameValidatorTest {

    private NewGameValidator newGameValidator;

    private Game gameMock;
    private PlayerAggregate playerAggregateMock;

    @Before
    public void setUp() throws Exception {
        newGameValidator = new NewGameValidator();

        gameMock = mock(Game.class);
        playerAggregateMock = mock(PlayerAggregate.class);
    }

    @Test
    public void shouldInvalidateInvalidGame() {
        when(gameMock.getPlayerAggregate()).thenReturn(playerAggregateMock);
        when(playerAggregateMock.isValid()).thenReturn(false);

        boolean result = newGameValidator.validate(gameMock);

        assertFalse("Should invalidate game when playerAggregate is not valid.", result);
        assertThat("Should set validation message when playerAggregate is not valid.",
                newGameValidator.getValidationMessages(), Matchers.containsInAnyOrder(NewGameValidator.INVALID_PLAYER_AGGREGATE_MSG));
    }

    @Test
    public void shouldValidateValidGame() {
        when(gameMock.getPlayerAggregate()).thenReturn(playerAggregateMock);
        when(playerAggregateMock.isValid()).thenReturn(true);

        boolean result = newGameValidator.validate(gameMock);

        assertTrue("Should validate game when playerAggregate is valid.", result);
        assertThat("Should not set validation message when playerAggregate is not valid.",
                newGameValidator.getValidationMessages(), empty());
    }

    @Test(expected = ValidationException.class)
    public void shouldThrowWhenInvalidGame() {
        when(gameMock.getPlayerAggregate()).thenReturn(playerAggregateMock);
        when(playerAggregateMock.isValid()).thenReturn(false);

        newGameValidator.validateOrThrow(gameMock);

        fail("Should have thrown ValidationException.");
    }

    @Test
    public void shouldNotThrowWhenValidGame() {
        when(gameMock.getPlayerAggregate()).thenReturn(playerAggregateMock);
        when(playerAggregateMock.isValid()).thenReturn(true);

        newGameValidator.validateOrThrow(gameMock);

        assertTrue("Should not throw exception when validating a new valid game.", true);
    }

    public void validationMessagesAtInitialization() {
        assertTrue("Validation messages should be empty at initialization.", newGameValidator.getValidationMessages().isEmpty());
    }
}