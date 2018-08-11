package com.challenge.application.game.model;

import java.util.Objects;

public class Player {

    public static final Player NULL = new Player("-", "unknown");
    private final String id;
    private final String name;

    public Player(final String id, final String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    /**
     * Check if player has same id with current instance.
     *
     * @param player the player to compare to.
     * @return [boolean] id player has same id with current instance.
     */
    public boolean isSame(Player player) {
        return this.getId().equals(player.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(id, player.id) &&
                Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Player ")
        .append(name);
        return sb.toString();
    }
}
