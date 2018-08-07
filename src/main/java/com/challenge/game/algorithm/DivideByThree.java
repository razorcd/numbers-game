package com.challenge.game.algorithm;

import com.challenge.game.domain.InputNumber;
import com.challenge.game.domain.OutputNumber;

public class DivideByThree implements GameAlgorithm {

    private static final int DIVIDER = 3;
    private static final int[] ADDITION_VALUES = {0, -1, 1};


    /**
     * Apply game logic to input number and generate output number.
     *
     * @param inputNumber the input number of the playing round.
     * @return [OutputNumber] the resulted output number after applying current logic.
     */
    @Override
    public OutputNumber apply(InputNumber inputNumber) {
        return new OutputNumber(getClosestDivisibleValue(inputNumber) / DIVIDER);
    }

    private int getClosestDivisibleValue(final InputNumber inputNumber) {
        int modulo = inputNumber.getValue() % DIVIDER;
        return inputNumber.getValue() + ADDITION_VALUES[modulo];
    }
}
