package com.challenge.application.gameofthree.game.validator;

import com.challenge.application.gameofthree.game.Game;
import com.challenge.application.gameofthree.game.domain.PlayerAggregate;
import com.challenge.application.gameofthree.exception.NotCurrentPlayerException;
import com.challenge.application.gameofthree.model.Human;
import com.challenge.application.gameofthree.model.IPlayer;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IsCurrentPlayerGameValidatorTest {

    private IsCurrentPlayerGameValidator isCurrentPlayerGameValidator;
    private IPlayer player;
    private Game game;

    @Before
    public void setUp() throws Exception {
        isCurrentPlayerGameValidator = mock(IsCurrentPlayerGameValidator.class);
        player = new Human("id1", "some name");
        game = mock(Game.class);

        isCurrentPlayerGameValidator = new IsCurrentPlayerGameValidator(player);
    }

    @Test
    public void shouldInvalidateWhenPlayerHasDifferentId() {
        PlayerAggregate playerAggregate = new PlayerAggregate(Arrays.asList(new Human("other id1", "p1"), new Human("other id2", "p2")), 0);
        when(game.getPlayerAggregate()).thenReturn(playerAggregate);

        boolean result = isCurrentPlayerGameValidator.validate(game);

        assertFalse("Should invalidate player when has not same id as the player next in turn.", result);
        assertThat("Should set messages when player has not same id as player next in turn.",
                isCurrentPlayerGameValidator.getValidationMessages(),
                containsInAnyOrder(IsCurrentPlayerGameValidator.INVALID_INPUT_FOR_GAME_MSG));
    }

    @Test
    public void shouldInvalidateWhenPlayerHasSameIdWithNotNextInTurnPlayer() {
        PlayerAggregate playerAggregate = new PlayerAggregate(
                Arrays.asList(new Human("other id2", "p2"), new Human("id1", "p1")), 0);
        when(game.getPlayerAggregate()).thenReturn(playerAggregate);

        boolean result = isCurrentPlayerGameValidator.validate(game);

        assertFalse("Should invalidate player when has same id as the player NOT next in turn.", result);
        assertThat("Should set messages when player has same id as player NOT next in turn.",
                isCurrentPlayerGameValidator.getValidationMessages(),
                containsInAnyOrder(IsCurrentPlayerGameValidator.INVALID_INPUT_FOR_GAME_MSG));
    }

    @Test
    public void shouldValidateWhenPlayerHasSameIdWithNextInTurnPlayer() {
        PlayerAggregate playerAggregate = new PlayerAggregate(
                Arrays.asList(new Human("id1", "p1"), new Human("other id2", "p2")), 0);
        when(game.getPlayerAggregate()).thenReturn(playerAggregate);

        boolean result = isCurrentPlayerGameValidator.validate(game);

        assertTrue("Should invalidate player when has same id as the player next in turn.", result);
        assertThat("Should not set messages when has same id as the player next in turn.",
                isCurrentPlayerGameValidator.getValidationMessages(), empty());
    }

    @Test(expected = NotCurrentPlayerException.class)
    public void shouldThrowWhenPlayerHasSameIdWithNotNextInTurnPlayer() {
        PlayerAggregate playerAggregate = new PlayerAggregate(
                Arrays.asList(new Human("other id2", "p2"), new Human("id1", "p1")), 0);
        when(game.getPlayerAggregate()).thenReturn(playerAggregate);

        isCurrentPlayerGameValidator.validateOrThrow(game);

        fail("Should have thrown ValidationException.");
    }
}