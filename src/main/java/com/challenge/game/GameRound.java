package com.challenge.game;

import com.challenge.game.algorithm.DivideByThree;
import com.challenge.game.domain.InputNumber;
import com.challenge.game.domain.OutputNumber;

import java.util.InputMismatchException;

public class GameRound {

    private static final OutputNumber WINNING_OUTPUT = new OutputNumber(1);

    private final InputNumber inputNumber;  //TODO: remove?
    private final OutputNumber outputNumber;
    private final DivideByThree algorithm;

    /**
     * Initialise game round with input.
     * @param inputNumber the input number value object.
     */
    public GameRound(final InputNumber inputNumber, final DivideByThree algorithm) {
        if (!inputNumber.isValid()) throw new InputMismatchException("Input is invalid.");
        this.inputNumber = inputNumber;
        this.algorithm = algorithm;
        this.outputNumber = algorithm.calculateOutputNumber(inputNumber);
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
}
