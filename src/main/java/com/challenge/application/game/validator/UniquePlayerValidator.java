package com.challenge.application.game.validator;

import com.challenge.application.game.GameManager;
import com.challenge.application.game.exception.ValidationException;
import com.challenge.application.game.model.IPlayer;

import java.util.ArrayList;
import java.util.List;

public class UniquePlayerValidator implements Validator<GameManager> {

    static final String NOT_UNIQUE_MSG = "can not add another player.";

    private List<String> messages = new ArrayList<>();
    private IPlayer newPlayer;

    public UniquePlayerValidator(IPlayer newPlayer) {
        this.newPlayer = newPlayer;
    }

    /**
     * Check if game manager is valid for adding a new player.
     * If game is invalid, error messages will be set to current state.
     *
     * @param gameManager the game manager to validate.
     * @return [boolean] if game manager can add the new player.
     */
    @Override
    public boolean validate(GameManager gameManager) {
        return isUniqueNewPlayer(gameManager.getPlayers(), newPlayer)
                || setInvalidState(NOT_UNIQUE_MSG);
    }

    /**
     * Check if game manager is valid for adding a new player or throw exception.
     *
     * @param gameManager the game manager to validate.
     * @throws ValidationException if game manager can not add the new player.
     */
    @Override
    public void validateOrThrow(GameManager gameManager) {
        if (!isUniqueNewPlayer(gameManager.getPlayers(), newPlayer)) {
            throw new ValidationException(NOT_UNIQUE_MSG);
        }
    }

    @Override
    public List<String> getValidationMessages() {
        return messages;
    }

    private boolean isUniqueNewPlayer(List<IPlayer> players, IPlayer newPlayer) {
        return !players.stream()
                .anyMatch(player -> player.isSame(newPlayer));
    }

    private boolean setInvalidState(String message) {
        messages.add(message);
        return false;
    }
}