package com.challenge.game.domain;

import java.util.Objects;

public class GameRoundResult {

    public static final GameRoundResult NULL = new GameRoundResult(null, false);

    private final OutputNumber outputNumber;
    private final boolean winner;

    /**
     * Initialize a new round result of a played game.
     *
     * @param outputNumber the resulted output number of the played game.
     * @param winner if current result is a winning one.
     */
    public GameRoundResult(final OutputNumber outputNumber, final boolean winner) {
        this.outputNumber = outputNumber;
        this.winner = winner;
    }

    public OutputNumber getOutputNumber() {
        return outputNumber;
    }

    public boolean isWinner() {
        return winner;
    }

    /**
     * Check if can play another round after current one.
     *
      * @return [boolean] if can play next round.
     */
    public boolean canPlayNext() {
        return !isWinner();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameRoundResult that = (GameRoundResult) o;
        return winner == that.winner &&
                Objects.equals(outputNumber, that.outputNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(outputNumber, winner);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Round result: ");
        sb.append("outputNumber ").append(outputNumber);
        sb.append(", winner ").append(winner);
        sb.append('.');
        return sb.toString();
    }
}