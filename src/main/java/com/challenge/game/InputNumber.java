package com.challenge.game;


import java.util.Objects;

public class InputNumber {

    private static final int LOW_BOUNDARY = 2;

    private int value;

    /**
     * Initialise a value object for input
     * @param value the value on the input
     */
    public InputNumber(int value) {
        this.value = value;
    }


    /**
     * Validates value is within expected boundaries.
     * @return [boolean] if valid
     */
    public boolean isValid() {
        return value >= LOW_BOUNDARY;
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
        final StringBuffer sb = new StringBuffer("InputNumber{");
        sb.append("value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
