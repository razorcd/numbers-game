package com.challenge.application.game.validator;

import com.challenge.application.game.Game;
import com.challenge.application.game.exception.ValidationException;

import java.util.ArrayList;
import java.util.List;

public class NewGameValidator implements Validator<Game> {

    static final String INVALID_PLAYER_AGGREGATE_MSG = "Game initialization failed due to invalid players.";

    private List<String> messages = new ArrayList<>();

    /**
     * Check if game initialization is valid by checking there are enough players.
     * If game is invalid, error messages will be set to current state.
     *
     * @param game the game to validate.
     * @return [boolean] if game is valid for initialization.
     */
    @Override
    public boolean validate(Game game) {
        return isValidPlayerAggregate(game) ||
                setInvalidState(INVALID_PLAYER_AGGREGATE_MSG);
    }

    /**
     * Check if game initialization is valid or throw exception.
     * Game initialization is valid if there are enough players.
     *
     * @param game the game to validate.
     * @throws ValidationException if game initialization failed.
     */
    @Override
    public void validateOrThrow(Game game) {
        if (!isValidPlayerAggregate(game)) {
            throw new ValidationException(INVALID_PLAYER_AGGREGATE_MSG);
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
