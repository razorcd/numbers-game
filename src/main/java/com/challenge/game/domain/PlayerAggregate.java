package com.challenge.game.domain;

import com.challenge.game.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PlayerAggregate {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerAggregate.class);

    public static final PlayerAggregate NULL = new PlayerAggregate(Collections.emptyList(), -1);
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
        return Optional.of(rootIndex)
                .filter(this::isValidRootIndex)
                .map(players::get)
                .orElse(Player.NULL);
    }

    /**
     * Get new aggregate with root as next player.
     *
     * @return [PlayerAggregate] new aggregate with root as next player.
     */
    public PlayerAggregate getNext() {
        return new PlayerAggregate(players, getNextRootIndex());
    }

    /**
     * Check if current aggregate root is valid.
     *
     * @return [boolean] if current aggregate is valid.
     */
    public boolean isValid() {
        return players.size() == PLAYER_COUNT &&
                isValidRootIndex(rootIndex);
    }

    /**
     * Counts the index of the next root in a circular way.
     *
     * @return next index of root
     */
    private int getNextRootIndex() {
        try {
            return (rootIndex + 1) % players.size();
        } catch(ArithmeticException e) {
            LOGGER.error("Can not divide by zero.");
        }
        return -1;
    }

    private boolean isValidRootIndex(int index) {
        return index >= 0 &&
               index < players.size();
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
        return new StringBuffer("players: ")
                .append(players)
                .append(" and player ")
                .append(rootIndex+1)
                .append(" has next turn.")
                .toString();
    }
}
