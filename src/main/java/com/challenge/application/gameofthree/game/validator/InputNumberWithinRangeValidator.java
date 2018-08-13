package com.challenge.application.gameofthree.game.validator;

import com.challenge.application.gameofthree.exception.ValidationException;
import com.challenge.application.gameofthree.game.Game;
import com.challenge.application.gameofthree.game.domain.InputNumber;
import com.challenge.application.utils.PropertiesConfigLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

public class InputNumberWithinRangeValidator implements Validator<Game> {

    private static final List<InputNumber> VALID_INPUT_NUMBERS = new CopyOnWriteArrayList<>();
    static {
        String[] additions = PropertiesConfigLoader.getProperties()
                .getProperty("com.challenge.application.game.input_numbers")
                .split("[/s,.]");
        Arrays.stream(additions).forEach(addition -> {
            int additionInt = Integer.parseInt(addition);
            VALID_INPUT_NUMBERS.add(new InputNumber(additionInt));
        });
    }

    private static final Function<InputNumber,String> INVALID_INPUT_FOR_GAME_MSG =
            number -> "can not play game because "+number+" is not within "+VALID_INPUT_NUMBERS;

    private List<String> messages = new ArrayList<>();
    private final InputNumber inputNumber;

    public InputNumberWithinRangeValidator(InputNumber inputNumber) {
        this.inputNumber = inputNumber;
    }

    /**
     * Check if input number is valid for playing the game.
     * If input is invalid, error messages will be set to current state.
     *
     * @param game the game to validate
     * @return [boolean] if input number is valid for playing with input number.
     */
    @Override
    public boolean validate(Game game) {
        return isValidInputNumber(inputNumber) ||
                setInvalidState(INVALID_INPUT_FOR_GAME_MSG.apply(inputNumber));
    }

    /**
     * Check if game is valid for playing with input number.
     *
     * @param game the game to validate.
     * @throws ValidationException if game is not valid for playing with input number.
     */
    @Override
    public void validateOrThrow(Game game) {
        if (!isValidInputNumber(inputNumber)) {
            throw new ValidationException(INVALID_INPUT_FOR_GAME_MSG.apply(inputNumber));
        }
    }

    @Override
    public List<String> getValidationMessages() {
        return messages;
    }

    private boolean isValidInputNumber(InputNumber inputNumber) {
        return VALID_INPUT_NUMBERS.contains(inputNumber);
    }

    private boolean setInvalidState(String message) {
        messages.add(message);
        return false;
    }
}
