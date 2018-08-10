package com.challenge.game;

import com.challenge.game.exception.GameException;
import com.challenge.game.service.GameRoundService;
import com.challenge.game.domain.GameRoundResult;
import com.challenge.game.domain.InputNumber;
import com.challenge.game.domain.PlayerAggregate;

import java.util.Optional;

public class Game {

    public static final Game NULL = new Game(null, PlayerAggregate.NULL, GameRoundResult.NULL);
    private final GameRoundService gameRoundService;
    private final PlayerAggregate playerAggregate;
    private final GameRoundResult gameRoundResult;

    /**
     * Create a new game.
     *
     * @param gameRoundService the service for each game round.
     * @param playerAggregate the playerAggregate aggregate holding the root as current player.
     * @throws GameException if invalid playerAggregate.
     */
    public Game(final GameRoundService gameRoundService, final PlayerAggregate playerAggregate) {
        this.gameRoundService = gameRoundService;
        this.playerAggregate = Optional.of(playerAggregate)
                .filter(PlayerAggregate::isValid)
                .orElseThrow(() -> new GameException("can not create a game with invalid players"));
        this.gameRoundResult = GameRoundResult.NULL;
    }

    /**
     * Private constructor to create a new game object.
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
     *
     * @param inputNumber the input number to play.
     * @return {@link Game} a new Game object that will hold the new state of the game.
     * @throws GameException if can not play input number in current game state.
     */
    public Game play(final InputNumber inputNumber) {
        //TODO: replace with state pattern
        Optional.of(gameRoundResult)
                .filter(GameRoundResult::canPlayAgain)
                .orElseThrow(() -> new GameException("can not play after "+gameRoundResult));

        Optional.of(playerAggregate)
                .filter(PlayerAggregate::isValid)
                .orElseThrow(() -> new GameException("can not play game when "+ playerAggregate));

        return Optional.of(inputNumber)
                .filter(input -> input.canPlayNumberAfter(gameRoundResult))
                .map(validInput -> new Game(gameRoundService, playerAggregate.getNext(), gameRoundService.play(validInput)))
                .orElseThrow(() -> new GameException("can not play "+inputNumber+" after "+gameRoundResult));
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
}
