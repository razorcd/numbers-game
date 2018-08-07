package com.challenge.game.algorithm;

import com.challenge.game.domain.InputNumber;
import com.challenge.game.domain.OutputNumber;

public class DivideByThree {
    private static final int[] ADDITION_VALUES = {0, -1, 1};
    private static final int DIVIDER = 3;

    public OutputNumber calculateOutputNumber(InputNumber inputNumber) {
        return new OutputNumber(getClosestDivisibleValue(inputNumber) / DIVIDER);
    }

    private int getClosestDivisibleValue(final InputNumber inputNumber) {
        int modulo = inputNumber.getValue() % DIVIDER;
        return inputNumber.getValue() + ADDITION_VALUES[modulo];
    }
}
