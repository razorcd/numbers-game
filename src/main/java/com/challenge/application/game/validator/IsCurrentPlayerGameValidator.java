package com.challenge.application.game.validator;

import com.challenge.application.game.Game;
import com.challenge.application.game.exception.NotCurrentPlayerException;
import com.challenge.application.game.exception.ValidationException;
import com.challenge.application.game.model.IPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IsCurrentPlayerGameValidator implements Validator<Game> {

    static final String INVALID_INPUT_FOR_GAME_MSG = "It is not your turn to play.";

    private List<String> messages = new ArrayList<>();

    private IPlayer expectedPlayer;

    /**
     * Create validator to validate if current player is next to play.
     *
     * @param expectedPlayer the expected current player
     */
    public IsCurrentPlayerGameValidator(IPlayer expectedPlayer) {
        this.expectedPlayer = expectedPlayer;
    }

    /**
     * Check if game is valid for playing by specified player.
     * If game is invalid, error messages will be set to current state.
     *
     * @param game the game to validate.
     * @return [boolean] if game is valid for playing by specified player.
     */
    @Override
    public boolean validate(Game game) {
        return isNextTurnPlayer(game, expectedPlayer) ||
                setInvalidState(INVALID_INPUT_FOR_GAME_MSG);
    }

    /**
     * Check if game is valid for playing by specified player.
     *
     * @param game the game to validate.
     * @throws ValidationException if game is not valid for playing by specified player.
     */
    @Override
    public void validateOrThrow(Game game) {
        if (!isNextTurnPlayer(game, expectedPlayer)) {
            throw new NotCurrentPlayerException(INVALID_INPUT_FOR_GAME_MSG);
        }
    }

    @Override
    public List<String> getValidationMessages() {
        return messages;
    }

    private boolean isNextTurnPlayer(Game game, IPlayer expectedCurrentPlayer) {
        IPlayer gameCurrentPlayer = game.getPlayerAggregate().getRootPlayer();
        return gameCurrentPlayer.isSame(expectedCurrentPlayer);
    }

    private boolean setInvalidState(String message) {
        messages.add(message);
        return false;
    }
}
