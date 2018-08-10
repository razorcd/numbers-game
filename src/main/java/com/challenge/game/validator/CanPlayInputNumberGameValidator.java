package com.challenge.game.validator;

import com.challenge.game.Game;
import com.challenge.game.domain.GameRoundResult;
import com.challenge.game.domain.InputNumber;
import com.challenge.game.exception.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class CanPlayInputNumberGameValidator implements Validator<Game> {

    public static final Function<InputNumber,String> INVALID_INPUT_FOR_GAME_MSG = number -> "can not play "+number+" after ";

    private List<String> messages = new ArrayList<>();

    private InputNumber inputNumber;

    /**
     * Create validator to validate the input number is valid to play for a game state.
     *
     * @param inputNumber the input number to validate against a game.
     */
    public CanPlayInputNumberGameValidator(InputNumber inputNumber) {
        this.inputNumber = inputNumber;
    }

    /**
     * Check if game is valid for playing with input number.
     * If game is invalid, error messages will be set to current state.
     *
     * @param game the game to validate.
     * @return [boolean] if game is valid for playing with input number.
     */
    @Override
    public boolean validate(Game game) {
        return getValidGameRoundResult(game).isPresent() ||
                setInvalidState(INVALID_INPUT_FOR_GAME_MSG.apply(inputNumber)+game.getGameRoundResult());
    }

    /**
     * Check if game is valid for playing with input number.
     *
     * @param game the game to validate.
     * @throws ValidationException if game is not valid for playing with input number.
     */
    @Override
    public void validateOrThrow(Game game) {
        getValidGameRoundResult(game)
                .orElseThrow(() -> new ValidationException(INVALID_INPUT_FOR_GAME_MSG.apply(inputNumber)+game.getGameRoundResult()));
    }

    @Override
    public List<String> getValidationMessages() {
        return messages;
    }

    private Optional<GameRoundResult> getValidGameRoundResult(Game game) {
        return Optional.of(game.getGameRoundResult())
                .filter(inputNumber::canPlayNumberAfter);
    }

    private boolean setInvalidState(String message) {
        messages.add(message);
        return false;
    }
}
