package com.challenge.application.game.gameround.gamerules.validator;

import com.challenge.application.game.domain.GameRoundInput;
import com.challenge.application.game.exception.GameRoundException;
import com.challenge.application.game.validator.Validator;
import com.challenge.application.utils.PropertiesConfigLoader;

import java.util.ArrayList;
import java.util.List;

public class DivideByThreeValidator implements Validator<GameRoundInput> {

    private static final String INVALID_INPUT_MSG = "could not play this round because of invalid input ";
    private static final int LOW_BOUNDARY = 2;
    private static final int DIVIDER = Integer.parseInt(PropertiesConfigLoader.getProperties()
            .getProperty("com.challenge.application.game.divider"));

    private List<String> messages = new ArrayList<>();

    /**
     * Check if input number is valid for divide by 3 algorithm.
     * It checks if number is divisible by 3.
     * If game is invalid, error messages will be set to current state.
     *
     * @param gameRoundInput the game round input to validate.
     * @return [boolean] if game round input is valid for running against divide by 3 game logic.
     */
    @Override
    public boolean validate(GameRoundInput gameRoundInput) {
        return isValid(gameRoundInput) ||
                setInvalidState(INVALID_INPUT_MSG + gameRoundInput);
    }

    /**
     * Check if input number is valid for divide by 3 algorithm.
     * It checks if number is divisible by 3.
     * If game is invalid, error messages will be set to current state.
     *
     * @param gameRoundInput the game round input to validate.
     * @throws GameRoundException if game round input is NOT valid for running against divide by 3 game logic.
     */
    @Override
    public void validateOrThrow(GameRoundInput gameRoundInput) {
        if (!isValid(gameRoundInput)) {
            throw new GameRoundException(INVALID_INPUT_MSG + gameRoundInput);
        }
    }

    @Override
    public List<String> getValidationMessages() {
        return messages;
    }

    //TODO: split in multiple validators
    private boolean isValid(GameRoundInput gameRoundInput) {
        return isBiggerThanLowBoundary(gameRoundInput) &&
               isDividableBy(gameRoundInput);
    }

    private boolean isBiggerThanLowBoundary(GameRoundInput gameRoundInput) {
        return gameRoundInput.isBiggerOrEqualThan(LOW_BOUNDARY);
    }

    private boolean isDividableBy(GameRoundInput gameRoundInput) {
        return gameRoundInput.getValue() % DIVIDER == 0;
    }

    private boolean setInvalidState(String message) {
        messages.add(message);
        return false;
    }
}
