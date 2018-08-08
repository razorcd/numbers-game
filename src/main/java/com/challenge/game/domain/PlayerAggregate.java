package com.challenge.game.domain;

import com.challenge.game.model.Player;

import java.util.List;
import java.util.Objects;

public class PlayerAggregate {

    private static final int PLAYER_COUNT = 2;

    private final List<Player> players;
    private final int rootIndex;

    /**
     * Create a new players aggregate with a root player a current player.
     *
     * @param players list of available players.
     * @param rootIndex the index of the root player from the players list representing current player.
     */
    public PlayerAggregate(List<Player> players, int rootIndex) {
        this.players = players;
        this.rootIndex = rootIndex;
    }

    /**
     * Get root player of aggregate.
     *
     * @return [Player] root player.
     */
    public Player getRootPlayer() {
        return players.get(rootIndex);
    }

    public PlayerAggregate getNext() {
        return new PlayerAggregate(players, getNextRootIndex());
    }

    /**
     * Check if current aggregate root is valid.
     * @return [boolean] if current aggregate is valid.
     */
    public boolean isValid() {
        return players.size() == PLAYER_COUNT &&
                rootIndex >= 0 &&
                rootIndex < players.size();
    }

    /**
     * Counts the index of the next root in a circular way.
     *
     * @return next index of root
     */
    private int getNextRootIndex() {
        return (rootIndex + 1) % players.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerAggregate that = (PlayerAggregate) o;
        return rootIndex == that.rootIndex &&
                Objects.equals(players, that.players);
    }

    @Override
    public int hashCode() {
        return Objects.hash(players, rootIndex);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PlayerAggregate{");
        sb.append("players=").append(players);
        sb.append(", rootIndex=").append(rootIndex);
        sb.append('}');
        return sb.toString();
    }
}
