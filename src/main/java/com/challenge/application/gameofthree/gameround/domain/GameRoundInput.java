package com.challenge.application.gameofthree.gameround.domain;

import com.challenge.application.gameofthree.game.domain.InputNumber;
import com.challenge.application.gameofthree.game.domain.OutputNumber;

import java.util.Objects;

public class GameRoundInput {

    private final InputNumber inputNumber;
    private final OutputNumber lastOutputNumber;

    /**
     * Game round input representing the added input to last round output.
     *
     * @param inputNumber the input number
     * @param lastOutputNumber the last round output number
     */
    public GameRoundInput(InputNumber inputNumber, OutputNumber lastOutputNumber) {
        this.inputNumber = inputNumber;
        this.lastOutputNumber = lastOutputNumber;
    }

    /**
     * Get the raw value of the game round input.
     *
     * @return [int] raw value of the input
     */
    public int getValue() {
        return inputNumber.getValue() + lastOutputNumber.getValue();
    }

    /**
     * Check if value of current game round input is bigger than a boundary.
     *
     * @param lowBoundary the boundary to compare current value to.
     * @return [boolean] if current value is bugger or equal than the specified boundary.
     */
    public boolean isBiggerOrEqualThan(int lowBoundary) {
        return getValue() >= lowBoundary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameRoundInput that = (GameRoundInput) o;
        return Objects.equals(inputNumber, that.inputNumber) &&
                Objects.equals(lastOutputNumber, that.lastOutputNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inputNumber, lastOutputNumber);
    }

    @Override
    public String toString() {
        return "last round output was " +
                lastOutputNumber +
                " and your input was " +
                inputNumber;
    }
}
