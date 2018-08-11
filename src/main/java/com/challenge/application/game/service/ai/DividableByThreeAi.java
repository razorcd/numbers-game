package com.challenge.application.game.service.ai;

import com.challenge.application.game.domain.InputNumber;
import com.challenge.application.game.domain.OutputNumber;

import java.util.HashMap;
import java.util.Map;

public class DividableByThreeAi implements IGameRoundAi {

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
     * Calculate the inputNumber of next round based on the last round outputNumber.
     *
     * @param outputNumber the last round outputNumber
     * @return [InputNumber] next calculated input number
     */
    @Override
    public InputNumber calculateNextInputNumberFor(final OutputNumber outputNumber) {
        return outputNumber.toInputNumber();
//        return getClosestDivisibleValue(outputNumber);
    }

    private InputNumber getClosestDivisibleValue(final OutputNumber outputNumber) {
        int remainder = outputNumber.getValue() % DIVIDER;
        int result =  outputNumber.getValue() + ADDITION_VALUES.get(remainder);
        return new InputNumber(result);
    }
}
