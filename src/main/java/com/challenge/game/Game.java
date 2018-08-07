package com.challenge.game;

import java.util.InputMismatchException;

public class Game {

    private static final int[] ADDITION_VALUES = {0, -1, 1};
    private static final int DIVIDER = 3;
    private static final int WINNING_VALUE = 1;

    private final InputNumber inputNumber;

    /**
     * Initialise game with input.
     * @param inputNumber the input number value object.
     */
    public Game(final InputNumber inputNumber) {
        if (!inputNumber.isValid()) throw new InputMismatchException("Input is invalid.");
        this.inputNumber = inputNumber;
    }

    /**
     * Get output value of the game.
     * @return [int] the output value
     */
    public int getOutput() {
        return getClosestDivisibleValue(inputNumber) / DIVIDER;
    }

    /**
     * Check if input value generates a winning output value.
     * @return [boolean] if output is a winning value
     */
    public boolean isWinner() {
        return getClosestDivisibleValue(inputNumber) / DIVIDER == WINNING_VALUE;
    }

    private int getClosestDivisibleValue(final InputNumber inputNumber) {
        int modulo = inputNumber.getValue() % DIVIDER;
        return inputNumber.getValue() + ADDITION_VALUES[modulo];
    }
}
