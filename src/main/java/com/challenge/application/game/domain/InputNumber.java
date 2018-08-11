package com.challenge.application.game.domain;


import com.challenge.application.utils.PropertiesConfigLoader;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

public class InputNumber {

    private static final String FIXED_INPUT_NUMBER = PropertiesConfigLoader.getProperties().
            getProperty("com.challenge.application.game.random_start_input_number");
    private static final String MIN_POSSIBLE_INPUT_NUMBER = PropertiesConfigLoader.getProperties().
            getProperty("com.challenge.application.game.min_possible_start_input_number");
    private static final String MAX_POSSIBLE_INPUT_NUMBER = PropertiesConfigLoader.getProperties().
            getProperty("com.challenge.application.game.max_possible_start_input_number");

    private final int value;

    /**
     * Initialise a value object for game round input.
     *
     * @param value the value on the input number.
     */
    public InputNumber(final int value) {
        this.value = value;
    }

    public static InputNumber getStartNumber() {
        int minPossibleInputNumber = Integer.parseInt(MIN_POSSIBLE_INPUT_NUMBER);
        int maxPossibleInputNumber = Integer.parseInt(MAX_POSSIBLE_INPUT_NUMBER);

        int inputNumberValue = Optional.ofNullable(FIXED_INPUT_NUMBER)
                .map(Integer::parseInt)
                .orElse(getRondomIntBetween(minPossibleInputNumber, maxPossibleInputNumber));
        return new InputNumber(inputNumberValue);
    }

    /**
     * Validate current input number is playable after specified game round result.
     *
     * @param gameRoundResult the previous game round result.
     * @return [boolean] if can play current input after specified game round result.
     */
    public boolean canPlayNumberAfter(GameRoundResult gameRoundResult) {
        return Stream.of(gameRoundResult)
                .allMatch(roundResult ->
                        Objects.isNull(roundResult.getOutputNumber()) ||
                        hasSameValue(roundResult.getOutputNumber()));
    }

    public int getValue() {
        return value;
    }

    public boolean isBiggerOrEqualThan(int lowBoundary) {
        return value >= lowBoundary;
    }

    public boolean hasSameValue(OutputNumber outputNumber) {
        return this.value == outputNumber.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InputNumber that = (InputNumber) o;
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
