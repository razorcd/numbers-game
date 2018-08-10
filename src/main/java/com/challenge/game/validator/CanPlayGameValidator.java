package com.challenge.game.validator;

import com.challenge.game.Game;
import com.challenge.game.domain.GameRoundResult;
import com.challenge.game.domain.PlayerAggregate;
import com.challenge.game.exception.GameException;
import com.challenge.game.exception.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class CanPlayGameValidator implements Validator<Game> {

    public static final String INVALID_GAME_ROUND_STATE_MSG = "can not play game when ";
    public static final String INVALID_PLAYER_AGGREGATE_MSG = "can not play game when ";

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
            getValidGameRoundResult(game).isPresent() || setInvalidState(INVALID_GAME_ROUND_STATE_MSG+game.getGameRoundResult()),
            getVlalidPlayerAggregate(game).isPresent() || setInvalidState(INVALID_PLAYER_AGGREGATE_MSG+game.getPlayerAggregate())
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
        getValidGameRoundResult(game)
                .orElseThrow(() -> new ValidationException(INVALID_GAME_ROUND_STATE_MSG+game.getGameRoundResult()));

        getVlalidPlayerAggregate(game)
                .orElseThrow(() -> new GameException(INVALID_PLAYER_AGGREGATE_MSG+game.getPlayerAggregate()));
    }

    @Override
    public List<String> getValidationMessages() {
        return messages;
    }

    private Optional<GameRoundResult> getValidGameRoundResult(Game game) {
        return Optional.of(game.getGameRoundResult())
                .filter(GameRoundResult::canPlayAgain);
    }

    private Optional<PlayerAggregate> getVlalidPlayerAggregate(Game game) {
        return Optional.of(game.getPlayerAggregate())
                .filter(PlayerAggregate::isValid);
    }

    private boolean setInvalidState(String message) {
        messages.add(message);
        return false;
    }
}
