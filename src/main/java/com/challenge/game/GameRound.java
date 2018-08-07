package com.challenge.game;

import com.challenge.game.domain.InputNumber;
import com.challenge.game.domain.OutputNumber;

import java.util.InputMismatchException;

public class GameRound {

    private static final int[] ADDITION_VALUES = {0, -1, 1};
    private static final int DIVIDER = 3;
    private static final OutputNumber WINNING_OUTPUT = new OutputNumber(1);

    private final InputNumber inputNumber;  //TODO: remove?
    private final OutputNumber outputNumber;

    /**
     * Initialise game round with input.
     * @param inputNumber the input number value object.
     */
    public GameRound(final InputNumber inputNumber) {
        if (!inputNumber.isValid()) throw new InputMismatchException("Input is invalid.");
        this.inputNumber = inputNumber;
        this.outputNumber = calculateOutputNumber(inputNumber);
    }

    /**
     * Get input of this game round.
     * @return [InputNumber] the input number of game.
     */
    public InputNumber getInputNumber() {
        return inputNumber;
    }

    /**
     * Get output number of this game round.
     * @return [OutputNumber] the output number of game.
     */
    public OutputNumber getOutput() {
        return outputNumber;
    }

    /**
     * Check if current round is a winning round.
     * @return [boolean] if is a winning round.
     */
    public boolean isWinner() {
        return outputNumber.equals(WINNING_OUTPUT);
    }


    private OutputNumber calculateOutputNumber(InputNumber inputNumber) {
        return new OutputNumber(getClosestDivisibleValue(inputNumber) / DIVIDER);
    }

    private int getClosestDivisibleValue(final InputNumber inputNumber) {
        int modulo = inputNumber.getValue() % DIVIDER;
        return inputNumber.getValue() + ADDITION_VALUES[modulo];
    }
}
