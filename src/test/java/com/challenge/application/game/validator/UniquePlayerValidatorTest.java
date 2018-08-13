package com.challenge.application.game.validator;

import com.challenge.application.game.GameManager;
import com.challenge.application.game.exception.ValidationException;
import com.challenge.application.game.model.Human;
import com.challenge.application.game.model.IPlayer;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UniquePlayerValidatorTest {

    private UniquePlayerValidator uniquePlayerValidator;

    private GameManager gameManager;
    private IPlayer newPlayer;

    @Before
    public void setUp() throws Exception {
        newPlayer = new Human("id1", "name1");
        gameManager = mock(GameManager.class);

        uniquePlayerValidator = new UniquePlayerValidator(newPlayer);
    }

    @Test
    public void shouldInvalidateWhenPlayerAlreadyExists() {
        when(gameManager.getPlayers())
                .thenReturn(Arrays.asList(new Human("id1", "n1"), new Human("otherId2", "n2")));

        boolean result = uniquePlayerValidator.validate(gameManager);

        assertFalse("Should invalidate when game manager already has the new player.", result);
        assertThat("Should set messages when game manager already has the new player.",
                uniquePlayerValidator.getValidationMessages(),
                containsInAnyOrder(UniquePlayerValidator.NOT_UNIQUE_MSG));
    }

    @Test
    public void shouldValidateWhenPlayerDoesNotAlreadyExist() {
        when(gameManager.getPlayers())
                .thenReturn(Arrays.asList(new Human("otherId1", "n1"), new Human("otherId2", "n2")));

        boolean result = uniquePlayerValidator.validate(gameManager);

        assertTrue("Should invalidate when game manager already has the new player.", result);
        assertThat("Should set messages when game manager already has the new player.",
                uniquePlayerValidator.getValidationMessages(), empty());
    }

    @Test(expected = ValidationException.class)
    public void shouldThrowWhenPlayerAlreadyExists() {
        when(gameManager.getPlayers())
                .thenReturn(Arrays.asList(new Human("id1", "n1"), new Human("otherId2", "n2")));

        uniquePlayerValidator.validateOrThrow(gameManager);

        fail("Should have thrown ValidationException.");
    }
}