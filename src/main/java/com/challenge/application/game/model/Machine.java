package com.challenge.application.game.model;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Entity representing a AI player.
 */
public class Machine implements IPlayer {

    public static final Machine NULL = new Machine("-", "unknown");
    private static final AtomicInteger nameCounter = new AtomicInteger(1);
    private static final String MACHINE_NAME = "Machine";

    private final String id;
    private final String name;

    public Machine(final String id, final String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Generates a new Machine player.
     *
     * @return [Machine] generated machine player
     */
    public static Machine generate() {
        return new Machine(UUID.randomUUID().toString(), MACHINE_NAME+nameCounter.incrementAndGet());
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
    public boolean isSame(IPlayer player) {
        return this.getId().equals(player.getId());
    }

    @Override
    public boolean isAi() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Machine player = (Machine) o;
        return Objects.equals(id, player.id) &&
                Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("machine ")
        .append(name);
        return sb.toString();
    }
}
