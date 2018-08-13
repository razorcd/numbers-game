package com.challenge.application.gameofthree.game.domain;


import java.util.Objects;

public class InputNumber {

    private final int value;

    /**
     * Initialise a value object for game round input.
     *
     * @param value the value on the input number.
     */
    public InputNumber(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
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
