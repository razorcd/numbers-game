package com.challenge.game.algorithm;

import com.challenge.game.domain.OutputNumber;

public class WinWhenOne implements WinLogic {

    private static final OutputNumber WINNING_VALUE = new OutputNumber(1);

    /**
     * Checks if output is a winning value.
     *
     * @param outputNumber output number of game
     * @return [boolean] if output is a winning value.
     */
    public Boolean apply(final OutputNumber outputNumber) {
        return outputNumber.equals(WINNING_VALUE);
    }
}
