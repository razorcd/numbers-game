package com.challenge.game.domain;


import java.util.Objects;
import java.util.stream.Stream;

public class InputNumber {

    private static final int LOW_BOUNDARY = 2;

    private final int value;

    /**
     * Initialise a value object for game round input.
     *
     * @param value the value on the input number.
     */
    public InputNumber(final int value) {
        this.value = value;
    }


    /**
     * Validates value is within expected boundaries.
     *
     * @return [boolean] if valid value.
     */
    public boolean isValid() {
        return value >= LOW_BOUNDARY;
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
}
