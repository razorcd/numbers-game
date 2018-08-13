package com.challenge.application.gameofthree.game.validator;

import com.challenge.application.gameofthree.exception.ValidationException;
import com.challenge.application.gameofthree.game.Game;

import java.util.ArrayList;
import java.util.List;

public class IsOpenPlayerAggregateValidator implements Validator<Game> {

    static final String IS_OPEN_PLAYER_AGGREGATE_MSG = "limit reached, can not add more ";

    private List<String> messages = new ArrayList<>();

    /**
     * Check if game has open player aggregate.
     * If game is invalid, error messages will be set to current state.
     *
     * @param game the game to validate.
     * @return [boolean] if game is valid for playing.
     */
    @Override
    public boolean validate(Game game) {
        return isOpenPlayerAggregate(game) || setInvalidState(IS_OPEN_PLAYER_AGGREGATE_MSG +game.getPlayerAggregate());
    }

    /**
     * Check if game is valid for playing.
     *
     * @param game the game to validate.
     * @throws ValidationException if game is not valid for playing.
     */
    @Override
    public void validateOrThrow(Game game) {
        if (!isOpenPlayerAggregate(game)) {
            throw new ValidationException(IS_OPEN_PLAYER_AGGREGATE_MSG + game.getPlayerAggregate());
        }
    }

    @Override
    public List<String> getValidationMessages() {
        return messages;
    }

    private boolean isOpenPlayerAggregate(Game game) {
        return game.getPlayerAggregate().acceptsMorePlayers();
    }

    private boolean setInvalidState(String message) {
        messages.add(message);
        return false;
    }
}
