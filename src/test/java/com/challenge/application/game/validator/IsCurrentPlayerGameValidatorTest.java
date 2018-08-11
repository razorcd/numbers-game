package com.challenge.application.game.validator;

import com.challenge.application.game.Game;
import com.challenge.application.game.domain.GameRoundResult;
import com.challenge.application.game.domain.InputNumber;
import com.challenge.application.game.domain.PlayerAggregate;
import com.challenge.application.game.exception.GameException;
import com.challenge.application.game.exception.NotCurrentPlayerException;
import com.challenge.application.game.model.Player;
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
    private Player player;
    private Game game;

    @Before
    public void setUp() throws Exception {
        isCurrentPlayerGameValidator = mock(IsCurrentPlayerGameValidator.class);
        player = new Player("id1", "some name");
        game = mock(Game.class);

        isCurrentPlayerGameValidator = new IsCurrentPlayerGameValidator(player);
    }

    @Test
    public void shouldInvalidateWhenPlayerHasDifferentId() {
        PlayerAggregate playerAggregate = new PlayerAggregate(Arrays.asList(new Player("other id1", "p1"), new Player("other id2", "p2")), 0);
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
                Arrays.asList(new Player("other id2", "p2"), new Player("id1", "p1")), 0);
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
                Arrays.asList(new Player("id1", "p1"), new Player("other id2", "p2")), 0);
        when(game.getPlayerAggregate()).thenReturn(playerAggregate);

        boolean result = isCurrentPlayerGameValidator.validate(game);

        assertTrue("Should invalidate player when has same id as the player next in turn.", result);
        assertThat("Should not set messages when has same id as the player next in turn.",
                isCurrentPlayerGameValidator.getValidationMessages(), empty());
    }

    @Test(expected = NotCurrentPlayerException.class)
    public void shouldThrowWhenPlayerHasSameIdWithNotNextInTurnPlayer() {
        PlayerAggregate playerAggregate = new PlayerAggregate(
                Arrays.asList(new Player("other id2", "p2"), new Player("id1", "p1")), 0);
        when(game.getPlayerAggregate()).thenReturn(playerAggregate);

        isCurrentPlayerGameValidator.validateOrThrow(game);

        fail("Should have thrown ValidationException.");
    }
}