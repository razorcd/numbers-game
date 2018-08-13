package com.challenge.application.gameofthree.game.validator;

import com.challenge.application.gameofthree.game.Game;
import com.challenge.application.gameofthree.game.domain.PlayerAggregate;
import com.challenge.application.gameofthree.exception.ValidationException;
import com.challenge.application.gameofthree.model.Human;
import com.challenge.application.gameofthree.model.IPlayer;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UniquePlayerValidatorTest {

    private UniquePlayerValidator uniquePlayerValidator;

    private Game gameMock;
    private IPlayer newPlayer;
    private PlayerAggregate playerAggregateMock;

    @Before
    public void setUp() throws Exception {
        newPlayer = new Human("id1", "name1");

        gameMock = mock(Game.class);

        playerAggregateMock = mock(PlayerAggregate.class);
        when(gameMock.getPlayerAggregate()).thenReturn(playerAggregateMock);

        uniquePlayerValidator = new UniquePlayerValidator(newPlayer);
    }

    @Test
    public void shouldInvalidateWhenPlayerAlreadyExists() {
        when(playerAggregateMock.hasPlayer(newPlayer)).thenReturn(true);

        boolean result = uniquePlayerValidator.validate(gameMock);

        assertFalse("Should invalidate when gameMock manager already has the new player.", result);
        assertThat("Should set messages when gameMock manager already has the new player.",
                uniquePlayerValidator.getValidationMessages(),
                containsInAnyOrder(UniquePlayerValidator.NOT_UNIQUE_MSG));
    }

    @Test
    public void shouldValidateWhenPlayerDoesNotAlreadyExist() {
        when(playerAggregateMock.hasPlayer(newPlayer)).thenReturn(false);

        boolean result = uniquePlayerValidator.validate(gameMock);

        assertTrue("Should invalidate when gameMock manager already has the new player.", result);
        assertThat("Should set messages when gameMock manager already has the new player.",
                uniquePlayerValidator.getValidationMessages(), empty());
    }

    @Test(expected = ValidationException.class)
    public void shouldThrowWhenPlayerAlreadyExists() {
        when(playerAggregateMock.hasPlayer(newPlayer)).thenReturn(true);

        uniquePlayerValidator.validateOrThrow(gameMock);

        fail("Should have thrown ValidationException.");
    }
}