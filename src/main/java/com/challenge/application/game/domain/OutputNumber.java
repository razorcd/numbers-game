package com.challenge.application.game.domain;

import com.challenge.application.utils.PropertiesConfigLoader;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class OutputNumber {

    private static final String FIXED_INPUT_NUMBER = PropertiesConfigLoader.getProperties().
            getProperty("com.challenge.application.game.random_start_input_number", "64");
    private static final String MIN_POSSIBLE_INPUT_NUMBER = PropertiesConfigLoader.getProperties().
            getProperty("com.challenge.application.game.min_possible_start_input_number", "10");
    private static final String MAX_POSSIBLE_INPUT_NUMBER = PropertiesConfigLoader.getProperties().
            getProperty("com.challenge.application.game.max_possible_start_input_number", "100");

    private final int value;

    /**
     * Get game starting number. This can be configured otherwise is random.
     *
     * @return [OutputNumber] game starting number.
     */
    public static OutputNumber getStartNumber() {
        int minPossibleInputNumber = Integer.parseInt(MIN_POSSIBLE_INPUT_NUMBER);
        int maxPossibleInputNumber = Integer.parseInt(MAX_POSSIBLE_INPUT_NUMBER);

        int inputNumberValue = Optional.ofNullable(FIXED_INPUT_NUMBER)
                .map(Integer::parseInt)
                .orElse(getRondomIntBetween(minPossibleInputNumber, maxPossibleInputNumber));
        return new OutputNumber(inputNumberValue);
    }

    /**
     * Initialise a value object for game round output.
     *
     * @param value the value of the output number.
     */
    public OutputNumber(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public InputNumber toInputNumber() {
        return new InputNumber(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OutputNumber that = (OutputNumber) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    private static int getRondomIntBetween(int minPossibleInputNumber, int maxPossibleInputNumber) {
        return new Random().nextInt(maxPossibleInputNumber-minPossibleInputNumber) + minPossibleInputNumber;
    }
}
