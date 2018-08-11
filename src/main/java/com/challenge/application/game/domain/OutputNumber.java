package com.challenge.application.game.domain;

import java.util.Objects;

public class OutputNumber {

    private final int value;

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
}
