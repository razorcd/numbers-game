package com.challenge.application.gameofthree.game;

import com.challenge.application.gameofthree.exception.GameException;
import com.challenge.application.gameofthree.exception.ValidationException;
import com.challenge.application.gameofthree.game.domain.InputNumber;
import com.challenge.application.gameofthree.game.domain.PlayerAggregate;
import com.challenge.application.gameofthree.game.validator.CanValidate;
import com.challenge.application.gameofthree.game.validator.Validator;
import com.challenge.application.gameofthree.gameround.GameRoundService;
import com.challenge.application.gameofthree.gameround.domain.GameRoundInput;
import com.challenge.application.gameofthree.gameround.domain.GameRoundResult;
import com.challenge.application.gameofthree.model.IPlayer;

import java.util.Objects;

public class Game implements CanValidate<Game> {

//    static final Game NULL = new Game(null, PlayerAggregate.NULL, GameRoundResult.NULL);
    private final GameRoundService gameRoundService;
    private final PlayerAggregate playerAggregate;
    private final GameRoundResult gameRoundResult;

    /**
     * Create a new game.
     * Consider validating game with NewGameValidator after initializing.
     *
     * @param gameRoundService the service for each game round.
     * @throws GameException if invalid playerAggregate.
     */
    public Game(final GameRoundService gameRoundService) {
        this.gameRoundService = gameRoundService;
        this.playerAggregate = PlayerAggregate.NULL;
        this.gameRoundResult = GameRoundResult.NULL;
    }

    /**
     * Private constructor to create a new game object.
     * Consider validating game with NewGameValidator after initializing.
     *
     * @param gameRoundService the service for each game round.
     * @param playerAggregate the player aggregate holding the root as the <b>player that will play next</b>.
     * @param gameRoundResult the result of the played round.
     */
    public Game(final GameRoundService gameRoundService, final PlayerAggregate playerAggregate, final GameRoundResult gameRoundResult) {
        this.gameRoundService = gameRoundService;
        this.playerAggregate = playerAggregate;
        this.gameRoundResult = gameRoundResult;
    }

    /**
     * Start game by creating a new copy of game with a random number.
     *
     * @return [Game] ready to be played.
     */
    public Game startGame() {
        return new Game(gameRoundService, playerAggregate, GameRoundResult.getInitial());
    }

    /**
     * Sets game to a reset state while keeping the players.
     *
     * @return [Game] new game in reset state.
     */
    public Game stopGame() {
        return new Game(gameRoundService, playerAggregate, GameRoundResult.NULL);
    }

    /**
     * Add new player to game. Returns a copy of current game with this player included.
     *
     * @param player the new player to add.
     * @return [Game] the new copied game with specified new player included.
     */
    public Game addPlayer(final IPlayer player) {
        return new Game(gameRoundService, playerAggregate.addPlayer(player), gameRoundResult);
    }

    /**
     * Remove player from game. Returns a copy of current game without this player.
     *
     * @param player the player to remove.
     * @return [Game] the new copied game without specified player.
     */
    public Game removePlayer(IPlayer player) {
        return new Game(gameRoundService, playerAggregate.removePlayer(player), gameRoundResult);
    }

    /**
     * Play a new number.
     * Consider validating game with CanPlayGameValidator before playing.
     *
     * @param inputNumber the input number to play.
     * @return {@link Game} a new Game object that will hold the new state of the game.
     * @throws GameException if can not play input number in current game state.
     */
    public Game play(final InputNumber inputNumber) {
        GameRoundInput gameRoundInput = new GameRoundInput(inputNumber, gameRoundResult.getOutputNumber());
        return new Game(gameRoundService, playerAggregate.next(), gameRoundService.play(gameRoundInput));
    }

    /**
     * Get player aggregate with current player as root.
     *
     * @return [PlayerAggregate] player aggregate with current player as root.
     */
    public PlayerAggregate getPlayerAggregate() {
        return playerAggregate;
    }

    /**
     * Get the results of the played round.
     *
     * @return [GameRoundResult] results of the played round.
     */
    public GameRoundResult getGameRoundResult() {
        return gameRoundResult;
    }

    /**
     * Validate current game with a validator.
     *
     * @param validator the validator to validate with.
     * @return [boolean] if current game is valid for specified validator.
     */
    @Override
    public boolean validate(Validator<Game> validator) {
        return validator.validate(this);
    }

    /**
     * Validate current game with a validator or throw exception.
     *
     * @param validator the validator to validate with.
     * @throws ValidationException if game is invalid
     */
    @Override
    public void validateOrThrow(Validator<Game> validator) {
        validator.validateOrThrow(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(gameRoundService, game.gameRoundService) &&
                Objects.equals(playerAggregate, game.playerAggregate) &&
                Objects.equals(gameRoundResult, game.gameRoundResult);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameRoundService, playerAggregate, gameRoundResult);
    }

    @Override
    public String toString() {
        return playerAggregate +
                " and " +
                gameRoundResult;
    }
}
