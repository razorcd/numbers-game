package com.challenge.application.gameofthree.game.validator;

import com.challenge.application.gameofthree.exception.ValidationException;
import com.challenge.application.gameofthree.game.Game;

import java.util.ArrayList;
import java.util.List;

public class IsValidGameRoundResultValidator implements Validator<Game> {

    static final String INVALID_GAME_ROUND_STATE_MSG = "can not play game when ";

    private List<String> messages = new ArrayList<>();

    /**
     * Check if game has valid last round result.
     * If game is invalid, error messages will be set to current state.
     *
     * @param game the game to validate.
     * @return [boolean] if game is valid for playing.
     */
    @Override
    public boolean validate(Game game) {
        return isValidGameRoundResult(game) || setInvalidState(INVALID_GAME_ROUND_STATE_MSG+game.getGameRoundResult());
    }

    /**
     * Check if game has valid last round result.
     *
     * @param game the game to validate.
     * @throws ValidationException if game is not valid for playing.
     */
    @Override
    public void validateOrThrow(Game game) {
        if (!isValidGameRoundResult(game)) {
            throw new ValidationException(INVALID_GAME_ROUND_STATE_MSG+game.getGameRoundResult());
        }
    }

    @Override
    public List<String> getValidationMessages() {
        return messages;
    }

    private boolean isValidGameRoundResult(Game game) {
        return game.getGameRoundResult().canPlayAgain();
    }

    private boolean setInvalidState(String message) {
        messages.add(message);
        return false;
    }
}
