package com.challenge.application.gameofthree.model;

import java.util.Objects;

/**
 * Entity representing a human player.
 */
public class Human implements IPlayer {

    public static final Human NULL = new Human("-", "unknown");
    private final String id;
    private final String name;

    public Human(final String id, final String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /**
     * Check if player has same id with current instance.
     *
     * @param player the player to compare to.
     * @return [boolean] id player has same id with current instance.
     */
    public boolean isSame(IPlayer player) {
        return this.getId().equals(player.getId());
    }

    @Override
    public boolean isAi() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Human player = (Human) o;
        return Objects.equals(id, player.id) &&
                Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("player ")
        .append(name);
        return sb.toString();
    }
}
