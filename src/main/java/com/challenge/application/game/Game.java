package com.challenge.application.game;

import com.challenge.application.game.domain.GameRoundResult;
import com.challenge.application.game.domain.InputNumber;
import com.challenge.application.game.domain.PlayerAggregate;
import com.challenge.application.game.exception.GameException;
import com.challenge.application.game.service.GameRoundService;
import com.challenge.application.game.validator.CanValidate;
import com.challenge.application.game.validator.Validator;

import java.util.Objects;

public class Game implements CanValidate<Game> {

    public static final Game NULL = new Game(null, PlayerAggregate.NULL, GameRoundResult.NULL);
    private final GameRoundService gameRoundService;
    private final PlayerAggregate playerAggregate;
    private final GameRoundResult gameRoundResult;

    /**
     * Create a new game.
     * Consider validating game with NewGameValidator after initializing.
     *
     * @param gameRoundService the service for each game round.
     * @param playerAggregate the playerAggregate aggregate holding the root as current player.
     * @throws GameException if invalid playerAggregate.
     */
    public Game(final GameRoundService gameRoundService, final PlayerAggregate playerAggregate) {
        this.gameRoundService = gameRoundService;
        this.playerAggregate = playerAggregate;
        this.gameRoundResult = GameRoundResult.NULL;
    }

    /**
     * Private constructor to create a new game object.
     * Consider validating game with NewGameValidator after initializing.
     *
     * @param gameRoundService the service for each game round.
     * @param playerAggregate the playerAggregate aggregate holding the root as current player.
     * @param gameRoundResult the result of the played round.
     */
    private Game(final GameRoundService gameRoundService, final PlayerAggregate playerAggregate, final GameRoundResult gameRoundResult) {
        this.gameRoundService = gameRoundService;
        this.playerAggregate = playerAggregate;
        this.gameRoundResult = gameRoundResult;
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
        return new Game(gameRoundService, playerAggregate.getNext(), gameRoundService.play(inputNumber));
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
     * @throws com.challenge.application.game.exception.ValidationException if game is invalid
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
