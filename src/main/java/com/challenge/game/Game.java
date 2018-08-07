package com.challenge.game;

import java.util.InputMismatchException;

public class Game {

    private static final int[] ADDITION_VALUES = {0, -1, 1};
    private static final int DIVIDER = 3;

    private final InputNumber inputNumber;

    /**
     * Initialise game with input.
     * @param inputNumber the input number value object.
     */
    public Game(final InputNumber inputNumber) {
        if (!inputNumber.isValid()) throw new InputMismatchException("input is invalid");
        this.inputNumber = inputNumber;
    }

    /**
     * Get output value of the game.
     * @return [int] the output value
     */
    public int getOutput() {
        return getClosestDivisibleValue(inputNumber)/3;
    }

    private int getClosestDivisibleValue(final InputNumber inputNumber) {
        int modulo = inputNumber.getValue() % DIVIDER;
        return inputNumber.getValue() + ADDITION_VALUES[modulo];
    }
}
