package com.challenge.application.gameofthree.game.validator;

import com.challenge.application.gameofthree.exception.ValidationException;
import com.challenge.application.gameofthree.game.Game;

import java.util.ArrayList;
import java.util.List;

public class IsValidPlayerAggregateValidator implements Validator<Game> {

    static final String INVALID_PLAYER_AGGREGATE_MSG = "can not play game when ";

    private List<String> messages = new ArrayList<>();

    /**
     * Check if game has valid player aggregate.
     * If game is invalid, error messages will be set to current state.
     *
     * @param game the game to validate.
     * @return [boolean] if game is valid for playing.
     */
    @Override
    public boolean validate(Game game) {
        return isValidPlayerAggregate(game) || setInvalidState(INVALID_PLAYER_AGGREGATE_MSG+game.getPlayerAggregate());
    }

    /**
     * Check if game is valid for playing.
     *
     * @param game the game to validate.
     * @throws ValidationException if game is not valid for playing.
     */
    @Override
    public void validateOrThrow(Game game) {
        if (!isValidPlayerAggregate(game)) {
            throw new ValidationException(INVALID_PLAYER_AGGREGATE_MSG + game.getPlayerAggregate());
        }
    }

    @Override
    public List<String> getValidationMessages() {
        return messages;
    }

    private boolean isValidPlayerAggregate(Game game) {
        return game.getPlayerAggregate().isValid();
    }

    private boolean setInvalidState(String message) {
        messages.add(message);
        return false;
    }
}
