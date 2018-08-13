package com.challenge.application.game;

import com.challenge.application.game.domain.InputNumber;
import com.challenge.application.game.exception.ValidationException;
import com.challenge.application.game.gameround.gamerules.gameplaylogic.DivideByThreeLogic;
import com.challenge.application.game.gameround.gamerules.gameplaylogic.IGameRoundLogic;
import com.challenge.application.game.gameround.gamerules.gamewinlogic.IGameWinLogic;
import com.challenge.application.game.gameround.gamerules.gamewinlogic.WinWhenOne;
import com.challenge.application.game.gameround.gamerules.validator.DivideByThreeValidator;
import com.challenge.application.game.model.IPlayer;
import com.challenge.application.game.validator.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

/**
 * Context class holds a mutable state of current game and available players.
 */
public class GameManager implements CanValidate<GameManager> {
    private volatile AtomicReference<Game> game;
    private volatile List<IPlayer> players = new CopyOnWriteArrayList<>();

    /**
     * Build application context.
     */
    public GameManager() {
        this.game = new AtomicReference<>(Game.NULL);
    }

    /**
     * Add new player.
     *
     * @param player player to add to player list.
     */
    public void addPlayer(IPlayer player) {
        players.add(player);
    }

    /**
     * Start playing the game with the added players.
     */
    public void startGame() {
        game.set(buildNewGame(players));
    }

    /**
     * Play a number.
     * Validates the input and the game and plays the number.
     * Changes state of manager and adds new game.
     *
     * @param inputNumber the number to play.
     * @param playerInTurn the current authorized player
     */
    public synchronized void play(InputNumber inputNumber, IPlayer playerInTurn) {
        Game newGame = Stream.of(game.get())//TODO: cleanup
            .peek(g -> g.validateOrThrow(new CanPlayGameValidator()))
            .peek(g -> g.validateOrThrow(new IsCurrentPlayerGameValidator(playerInTurn)))
            .peek(g -> g.validateOrThrow(new InputNumberWithinRangeValidator(inputNumber)))
            .map(g -> g.play(inputNumber))
            .findAny()
            .orElseThrow(() -> new RuntimeException("Error while playing number "+inputNumber+" with game "+game));

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

    public List<IPlayer> getPlayers() {
        return players;
    }

    /**
     * Validate current game manager with a validator.
     *
     * @param validator the validator to validate with.
     * @return [boolean] if current game manager is valid by specified validator.
     */
    @Override
    public boolean validate(Validator<GameManager> validator) {
        return validator.validate(this);
    }

    /**
     * Validate current game manager with a validator or throw exception.
     *
     * @param validator the validator to validate with.
     * @throws ValidationException if game is invalid
     */
    @Override
    public void validateOrThrow(Validator<GameManager> validator) {
        validator.validateOrThrow(this);
    }

    private Game buildNewGame(List<IPlayer> players) {
        IGameRoundLogic gameLogic = new DivideByThreeLogic();
        gameLogic.addValidator(new DivideByThreeValidator());

        IGameWinLogic winLogic = new WinWhenOne();

        return new GameFactory(gameLogic, winLogic).buildNewGame(players);
    }
}
