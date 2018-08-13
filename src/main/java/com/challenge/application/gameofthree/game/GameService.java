package com.challenge.application.gameofthree.game;

import com.challenge.application.gameofthree.exception.GameException;
import com.challenge.application.gameofthree.game.domain.InputNumber;
import com.challenge.application.gameofthree.game.validator.*;
import com.challenge.application.gameofthree.model.IPlayer;

import java.util.concurrent.atomic.AtomicReference;

public class GameService {
    private final AtomicReference<Game> game;

    /**
     * Build application context.
     */
    public GameService(Game game) {
        this.game = new AtomicReference<>(game);
    }

    /**
     * Add new player.
     *
     * @param player player to add to player list.
     * @throws GameException if not unique player.
     */
    public void addPlayer(final IPlayer player) {
        new UniquePlayerValidator(player).validateOrThrow(game.get());
        game.set(game.get().addPlayer(player));
    }

    public void removePlayer(IPlayer player) {
        game.set(game.get().removePlayer(player));
    }

    /**
     * Start playing the game with the added players.
     *
     * @throws GameException if not enough players.
     */
    public void startGame() {
        game.get().validateOrThrow(new NewGameValidator());
        game.set(game.get().startGame());
    }

    /**
     * Stop playing game. Sets game to initial state while keeping the players.
     */
    public void stopGame() {
        game.set(game.get().stopGame());
    }

    /**
     * Play a number.
     * Validates the input and the game and plays the number.
     * Changes state of manager and adds new game.
     *
     * @param inputNumber the number to play.
     * @param playerInTurn the current authorized player
     * @throws GameException if not enough players or last round result is not valid or invalid input.
     */
    public void play(final InputNumber inputNumber, final IPlayer playerInTurn) {
        game.get().validateOrThrow(new CanPlayGameValidator());
        game.get().validateOrThrow(new IsCurrentPlayerGameValidator(playerInTurn));
        game.get().validateOrThrow(new InputNumberWithinRangeValidator(inputNumber));

        Game newGame = game.get().play(inputNumber);
        game.set(newGame);
    }

    /**
     * Get game in current state.
     *
     * @return [Game] game in current state.
     */
    public Game getGame() {
        return game.get();
    }
}
