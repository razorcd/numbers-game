package com.challenge.application.gameofthree.model;

/**
 * Entity interface representing a player.
 */
public interface IPlayer {

    /**
     * Identifier of the player.
     *
     * @return [String] player ID.
     */
    String getId();

    /**
     * Get name of player that would be displayed.
     *
     * @return [String] the name of the player.
     */
    String getName();

    /**
     * Check if player has same id with current instance.
     *
     * @param player the player to compare to.
     * @return [boolean] id player has same id with current instance.
     */
    boolean isSame(IPlayer player);

    /**
     * Defines is current player is machine logic
     * or played by human trough console.
     *
     * @return [boolean] if current player is artificial.
     */
    boolean isAi();
}
