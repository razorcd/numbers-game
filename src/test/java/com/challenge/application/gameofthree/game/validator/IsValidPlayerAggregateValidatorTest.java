package com.challenge.application.gameofthree.game.validator;

import com.challenge.application.gameofthree.exception.ValidationException;
import com.challenge.application.gameofthree.game.Game;
import com.challenge.application.gameofthree.game.domain.PlayerAggregate;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.support.membermodification.MemberMatcher;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor({"com.challenge.application.gameofthree.game.domain.PlayerAggregate"})
public class IsValidPlayerAggregateValidatorTest {

    private IsValidPlayerAggregateValidator isValidPlayerAggregateValidator;

    private Game gameMock;
    private PlayerAggregate playerAggregateMock;

    @Before
    public void setUp() throws Exception {
        suppress(MemberMatcher.constructor(PlayerAggregate.class));

        isValidPlayerAggregateValidator = new IsValidPlayerAggregateValidator();

        gameMock = mock(Game.class);
        playerAggregateMock = mock(PlayerAggregate.class);
        when(playerAggregateMock.toString()).thenReturn("");
    }

    @Test
    public void shouldInvalidateGameWithInvalidPlayerAggregate() {
        when(gameMock.getPlayerAggregate()).thenReturn(playerAggregateMock);
        when(playerAggregateMock.isValid()).thenReturn(false);

        boolean result = isValidPlayerAggregateValidator.validate(gameMock);

        assertFalse("Should invalidate game with invalid player aggregate.", result);
        assertThat("Should set messages when invalidate game with player aggregate",
                isValidPlayerAggregateValidator.getValidationMessages(),
                Matchers.containsInAnyOrder(IsValidPlayerAggregateValidator.INVALID_PLAYER_AGGREGATE_MSG));
    }

    @Test
    public void shouldValidateWithValidPlayerAggregate() {
        when(gameMock.getPlayerAggregate()).thenReturn(playerAggregateMock);
        when(playerAggregateMock.isValid()).thenReturn(true);

        boolean result = isValidPlayerAggregateValidator.validate(gameMock);

        assertTrue("Should validate game for playing.", result);
        assertThat("Should not set messages when game is valid for playing.",
                isValidPlayerAggregateValidator.getValidationMessages(), empty());
    }

    @Test(expected = ValidationException.class)
    public void shouldThrowWhenValidatingGameWithInvalidPlayerAggregate() {
        when(gameMock.getPlayerAggregate()).thenReturn(playerAggregateMock);
        when(playerAggregateMock.isValid()).thenReturn(false);

        isValidPlayerAggregateValidator.validateOrThrow(gameMock);

        fail("Should have thrown ValidationException.");
    }

    @Test
    public void shouldNotThrowWhenPlayerAggregateIsValid() {
        when(gameMock.getPlayerAggregate()).thenReturn(playerAggregateMock);
        when(playerAggregateMock.isValid()).thenReturn(true);

        isValidPlayerAggregateValidator.validateOrThrow(gameMock);

        assertTrue("Should not throw exception when validating a valid game for playing.", true);
    }

    public void validationMessagesAtInitialization() {
        assertTrue("Validation messages should be empty at initialization.", isValidPlayerAggregateValidator.getValidationMessages().isEmpty());
    }
}