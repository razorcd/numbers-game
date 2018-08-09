package com.challenge.game.model;

import java.util.Objects;

public class Player {

    public static final Player NULL = new Player("unknown");
    private final String name;

    public Player(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Player ")
        .append(name);
        return sb.toString();
    }
}
