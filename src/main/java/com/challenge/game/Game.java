package com.challenge.game;

import java.util.InputMismatchException;

public class Game {

    private InputNumber inputNumber;

    /**
     * Initialise game with input.
     * @param inputNumber the input number value object.
     */
    public Game(InputNumber inputNumber) {
        if (!inputNumber.isValid()) throw new InputMismatchException("input is invalid");
        this.inputNumber = inputNumber;
    }

    /**
     * Get output value of the game.
     * @return [int] the output value
     */
    public int getOutput() {
        int modulo = inputNumber.getValue()%3;
        int[] additions = {0, -1, 1};

        return (inputNumber.getValue()+additions[modulo])/3;
    }
}
