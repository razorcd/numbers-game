package com.challenge.game.algorithm;

import com.challenge.game.domain.OutputNumber;

public class WinWhenOne {

    private static final OutputNumber WINNING_VALUE = new OutputNumber(1);

    /**
     * Checks if output is a winning value.
     * @param outputNumber output number of game
     * @return [boolean] if output is a winning value.
     */
    public boolean isWinner(OutputNumber outputNumber) {
        return outputNumber.equals(WINNING_VALUE);
    }
}
