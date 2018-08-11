package com.challenge.application.game.service.gamerules.validator;

import com.challenge.application.game.domain.InputNumber;
import com.challenge.application.game.exception.GameRoundException;
import com.challenge.application.game.validator.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DivideByThreeInputValidator implements Validator<InputNumber> {

    public static final String INVALID_INPUT_NUMBER_MSG = "can not play game round, invalid input number ";
    private static final int LOW_BOUNDARY = 2;

    private List<String> messages = new ArrayList<>();

    /**
     * Check if input number is valid for divide by 3 algorithm.
     * If game is invalid, error messages will be set to current state.
     *
     * @param inputNumber the input number to validate.
     * @return [boolean] if input number is valid for running against divide by 3 algorithm.
     */
    @Override
    public boolean validate(InputNumber inputNumber) {
        return isValidInputNumber(inputNumber).isPresent() ||
                setInvalidState(INVALID_INPUT_NUMBER_MSG+inputNumber);
    }

    @Override
    public void validateOrThrow(InputNumber inputNumber) {
        isValidInputNumber(inputNumber)
                .orElseThrow(() -> new GameRoundException(INVALID_INPUT_NUMBER_MSG+inputNumber));
    }

    @Override
    public List<String> getValidationMessages() {
        return messages;
    }

    private Optional<InputNumber> isValidInputNumber(InputNumber inputNumber) {
        return Optional.of(inputNumber)
                .filter(number -> number.isBiggerOrEqualThan(LOW_BOUNDARY));
    }

    private boolean setInvalidState(String message) {
        messages.add(message);
        return false;
    }
}
