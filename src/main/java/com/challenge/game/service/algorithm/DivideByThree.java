package com.challenge.game.service.algorithm;

import com.challenge.game.domain.InputNumber;
import com.challenge.game.domain.OutputNumber;

import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulated logic that determines the number that is divisible by {@link #DIVIDER}
 * by adding one of {@link #ADDITION_VALUES} to <b>input</b> and then divides this number by {@link #DIVIDER}.
 *
 * E.g. For input 16, it will find that by adding -1 will result in 15 and then it will divide 15 by 3 returning 5.
 */
public class DivideByThree implements GameAlgorithm {

    /**
     * The divider value.
     */
    private static final int DIVIDER = 3;

    /**
     * The values that can be added to <b>input</b> to find the number that is divisible by {@link #DIVIDER}.
     * index is the reminder after <b>input</b> % {@link #DIVIDER}
     * value is the number that has to be added to input so it is divisible by {@link #DIVIDER}.
     */
    private static final Map<Integer, Integer> ADDITION_VALUES;
    static {
        ADDITION_VALUES = new HashMap<>();
        ADDITION_VALUES.put(0, 0);
        ADDITION_VALUES.put(1, -1);
        ADDITION_VALUES.put(2, 1);
    }

    /**
     * Apply game logic to input number and generate output number.
     *
     * @param inputNumber the input number of the playing round.
     * @return [OutputNumber] the resulted output number after applying current logic.
     */
    @Override
    public OutputNumber apply(final InputNumber inputNumber) {
        return new OutputNumber(getClosestDivisibleValue(inputNumber) / DIVIDER);
    }

    private int getClosestDivisibleValue(final InputNumber inputNumber) {
        int remainder = inputNumber.getValue() % DIVIDER;
        return inputNumber.getValue() + ADDITION_VALUES.get(remainder);
    }
}
