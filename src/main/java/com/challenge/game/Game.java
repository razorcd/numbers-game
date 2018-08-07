package com.challenge.game;

import java.util.InputMismatchException;

public class Game {

    private static final int[] ADDITION_VALUES = {0, -1, 1};
    private static final int DIVIDER = 3;
    private static final OutputNumber WINNING_OUTPUT = new OutputNumber(1);

    private final InputNumber inputNumber;
    private final OutputNumber outputNumber;

    /**
     * Initialise game with input.
     * @param inputNumber the input number value object.
     */
    public Game(final InputNumber inputNumber) {
        if (!inputNumber.isValid()) throw new InputMismatchException("Input is invalid.");
        this.inputNumber = inputNumber;
        this.outputNumber = calculateOutputNumber(inputNumber);
    }

    /**
     * Get input of game.
     * @return [InputNumber] the input number of game.
     */
    public InputNumber getInputNumber() {
        return inputNumber;
    }

    /**
     * Get output number of the game.
     * @return [OutputNumber] the output number of game.
     */
    public OutputNumber getOutput() {
        return outputNumber;
    }

    /**
     * Check if input value generates a winning output value.
     * @return [boolean] if output is a winning value
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
