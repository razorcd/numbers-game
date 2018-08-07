package com.challenge.game.service.domain;

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
        final StringBuffer sb = new StringBuffer("OutputNumber{");
        sb.append("value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
