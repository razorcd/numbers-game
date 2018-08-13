package com.challenge.application.gameofthree.ai;

import com.challenge.application.gameofthree.game.domain.InputNumber;
import com.challenge.application.gameofthree.game.domain.OutputNumber;
import com.challenge.application.utils.PropertiesConfigLoader;

import java.util.HashMap;
import java.util.Map;

public class DivideByThreeAi implements IGameRoundAi {

    /**
     * The divider value.
     */
    private static final int DIVIDER = Integer.parseInt(PropertiesConfigLoader.getProperties()
            .getProperty("com.challenge.application.game.divider"));

    /**
     * The values that can be added to <b>input</b> to find the number that is divisible by {@link #DIVIDER}.
     * index is the reminder after <b>input</b> % {@link #DIVIDER}
     * value is the number that has to be added to input so it is divisible by {@link #DIVIDER}.
     * E.g.: if last output is 7, index: 5%3=2 and added value: ADDITION_VALUES[2]=1  =>  5 + 1 = 6, the closest value dividable by 3.
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
     * Calculate the addition value to get to closest dividable by 3 number.
     * E.g. for 5 it will calculate 1 because  5 + 1 % 3 = 0
     * E.g. for 9 it will calculate 0 because  9 + 0 % 3 = 0
     * E.g. for 13 it will calculate -1 because  13 + (-1) % 3 = 0
     *
     * @param outputNumber the last round outputNumber
     * @return [InputNumber] next calculated input number
     */
    @Override
    public InputNumber calculateNextInputNumberFor(final OutputNumber outputNumber) {
        return getAdditionForClosestDivisibleValue(outputNumber);
    }

    private InputNumber getAdditionForClosestDivisibleValue(final OutputNumber outputNumber) {
        int remainder = outputNumber.getValue() % DIVIDER;
        int addition = ADDITION_VALUES.get(remainder);
        return new InputNumber(addition);
    }
}
