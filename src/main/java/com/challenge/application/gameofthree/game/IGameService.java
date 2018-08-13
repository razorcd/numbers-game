package com.challenge.application.gameofthree.game;

import com.challenge.application.gameofthree.exception.GameException;
import com.challenge.application.gameofthree.game.domain.InputNumber;
import com.challenge.application.gameofthree.model.IPlayer;

public interface IGameService {

    /**
     * Add new player to the game.
     *
     * @param player player to add to player list.
     * @throws GameException if not unique player.
     */
    void addPlayer(final IPlayer player);

    /**
     * Remove a player from the game.
     *
     * @param player the player to remove.
     */
    void removePlayer(IPlayer player);

    /**
     * Start playing the game with the added players.
     *
     * @throws GameException if not enough players.
     */
    void startGame();

    /**
     * Stop playing game. Sets game to initial state while keeping the players.
     */
    void stopGame();

    /**
     * Play a number.
     * Validates the input and the game and plays the number.
     * Changes state of manager and adds new game.
     *
     * @param inputNumber the number to play.
     * @param playerInTurn the current authorized player
     * @throws GameException if not enough players or last round result is not valid or invalid input.
     */
    void play(final InputNumber inputNumber, final IPlayer playerInTurn);

    /**
     * Get game in current state.
     *
     * @return [Game] game in current state.
     */
    Game getGame();
}
