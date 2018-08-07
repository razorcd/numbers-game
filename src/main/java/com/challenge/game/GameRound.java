package com.challenge.game;

import com.challenge.game.algorithm.DivideByThree;
import com.challenge.game.domain.InputNumber;
import com.challenge.game.domain.OutputNumber;

import java.util.InputMismatchException;
import java.util.Optional;

public class GameRound {

    private static final OutputNumber WINNING_OUTPUT = new OutputNumber(1);

    private final InputNumber inputNumber;
    private final DivideByThree algorithm;

    /**
     * Initialise game round with input.
     * @param inputNumber the input number value object.
     */
    public GameRound(final InputNumber inputNumber, final DivideByThree algorithm) {
        this.inputNumber = Optional.of(inputNumber)
                .filter(InputNumber::isValid)
                .orElseThrow(InputMismatchException::new);
        this.algorithm = algorithm;
    }

    /**
     * Get output number of this game round.
     * @return [OutputNumber] the output number of game.
     */
    public OutputNumber getOutput() {
        //TODO: memoize outputNumber?
        return algorithm.calculateOutputNumber(inputNumber);
    }

    /**
     * Check if current round is a winning round.
     * @return [boolean] if is a winning round.
     */
    public boolean isWinner() {
        return algorithm.calculateOutputNumber(inputNumber).equals(WINNING_OUTPUT);  //TODO: move to OutputNumber
    }
}
