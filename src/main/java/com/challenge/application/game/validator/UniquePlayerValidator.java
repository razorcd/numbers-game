package com.challenge.application.game.validator;

import com.challenge.application.game.Game;
import com.challenge.application.game.domain.PlayerAggregate;
import com.challenge.application.game.exception.ValidationException;
import com.challenge.application.game.model.IPlayer;

import java.util.ArrayList;
import java.util.List;

public class UniquePlayerValidator implements Validator<Game> {

    static final String NOT_UNIQUE_MSG = "can not add another player.";

    private List<String> messages = new ArrayList<>();
    private IPlayer newPlayer;

    public UniquePlayerValidator(IPlayer newPlayer) {
        this.newPlayer = newPlayer;
    }

    /**
     * Check if game is valid for adding a new player.
     * If game is invalid, error messages will be set to current state.
     *
     * @param game the game to validate.
     * @return [boolean] if game can add the new player.
     */
    @Override
    public boolean validate(Game game) {
        return isUniqueNewPlayer(game.getPlayerAggregate(), newPlayer)
                || setInvalidState(NOT_UNIQUE_MSG);
    }

    /**
     * Check if game manager is valid for adding a new player or throw exception.
     *
     * @param game the game manager to validate.
     * @throws ValidationException if game manager can not add the new player.
     */
    @Override
    public void validateOrThrow(Game game) {
        if (!isUniqueNewPlayer(game.getPlayerAggregate(), newPlayer)) {
            throw new ValidationException(NOT_UNIQUE_MSG);
        }
    }

    @Override
    public List<String> getValidationMessages() {
        return messages;
    }

    private boolean isUniqueNewPlayer(PlayerAggregate playerAggregate, IPlayer newPlayer) {
        return !playerAggregate.hasPlayer(newPlayer);
//        return !players.stream()
//                .anyMatch(player -> player.isSame(newPlayer));
    }

    private boolean setInvalidState(String message) {
        messages.add(message);
        return false;
    }
}
