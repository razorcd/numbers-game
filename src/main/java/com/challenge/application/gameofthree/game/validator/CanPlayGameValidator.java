package com.challenge.application.gameofthree.game.validator;

import com.challenge.application.gameofthree.game.Game;
import com.challenge.application.gameofthree.exception.GameException;
import com.challenge.application.gameofthree.exception.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CanPlayGameValidator implements Validator<Game> {

    static final String INVALID_GAME_ROUND_STATE_MSG = "can not play game when ";
    static final String INVALID_PLAYER_AGGREGATE_MSG = "can not play game when ";

    private List<String> messages = new ArrayList<>();

    /**
     * Check if game is valid for playing.
     * If game is invalid, error messages will be set to current state.
     *
     * @param game the game to validate.
     * @return [boolean] if game is valid for playing.
     */
    @Override
    public boolean validate(Game game) {
        return Stream.of(
            isValidGameRoundResult(game) || setInvalidState(INVALID_GAME_ROUND_STATE_MSG+game.getGameRoundResult()),
            isValidPlayerAggregate(game) || setInvalidState(INVALID_PLAYER_AGGREGATE_MSG+game.getPlayerAggregate())
        ).allMatch(Boolean::booleanValue);
    }

    /**
     * Check if game is valid for playing.
     *
     * @param game the game to validate.
     * @throws ValidationException if game is not valid for playing.
     */
    @Override
    public void validateOrThrow(Game game) {
        if (!isValidGameRoundResult(game)) {
            throw new ValidationException(INVALID_GAME_ROUND_STATE_MSG+game.getGameRoundResult());
        }

        if (!isValidPlayerAggregate(game)) {
            throw new GameException(INVALID_PLAYER_AGGREGATE_MSG + game.getPlayerAggregate());
        }
    }

    @Override
    public List<String> getValidationMessages() {
        return messages;
    }

    private boolean isValidGameRoundResult(Game game) {
        return game.getGameRoundResult().canPlayAgain();
    }

    private boolean isValidPlayerAggregate(Game game) {
        return game.getPlayerAggregate().isValid();
    }

    private boolean setInvalidState(String message) {
        messages.add(message);
        return false;
    }
}
