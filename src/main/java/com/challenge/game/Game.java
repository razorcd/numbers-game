package com.challenge.game;

import com.challenge.game.service.GameRoundService;
import com.challenge.game.domain.GameRoundResult;
import com.challenge.game.domain.InputNumber;
import com.challenge.game.domain.PlayerAggregate;

import java.util.Optional;

public class Game {

    public static final Game NULL = new Game(null, PlayerAggregate.NULL, GameRoundResult.NULL);
    private final GameRoundService gameRoundService;
    private final PlayerAggregate players;
    private final GameRoundResult gameRoundResult;

    /**
     * Create a new game.
     *
     * @param gameRoundService the service for each game round.
     * @param players the players aggregate holding the root as current player.
     */
    public Game(final GameRoundService gameRoundService, final PlayerAggregate players) {
        this.gameRoundService = gameRoundService;
        this.players = Optional.of(players)
                .filter(PlayerAggregate::isValid)
                .orElseThrow(() -> new IllegalArgumentException("Can not start game with not valid "+players));
        this.gameRoundResult = GameRoundResult.NULL;
    }

    /**
     * Private constructor to create a new game object.
     *
     * @param gameRoundService the service for each game round.
     * @param players the players aggregate holding the root as current player.
     * @param gameRoundResult the result of the played round.
     */
    private Game(final GameRoundService gameRoundService, final PlayerAggregate players, final GameRoundResult gameRoundResult) {
        this.gameRoundService = gameRoundService;
        this.players = players;
        this.gameRoundResult = gameRoundResult;
    }

    /**
     * Play a new number.
     *
     * @param inputNumber the input number to play.
     * @return [Game] a new Game object that will hold the new state of the game.
     */
    public Game play(final InputNumber inputNumber) {
        Optional.of(gameRoundResult)
                .filter(GameRoundResult::canPlayNext)
                .orElseThrow(() -> new IllegalStateException("Can not play after "+gameRoundResult));

        Optional.of(players)
                .filter(PlayerAggregate::isValid)
                .orElseThrow(() -> new IllegalStateException("Can not play game when "+players));

        return Optional.of(inputNumber)
                .filter(input -> input.canPlayNumberAfter(gameRoundResult))
                .map(validInput -> new Game(gameRoundService, players.getNext(), gameRoundService.play(validInput)))
                .orElseThrow(() -> new IllegalStateException("Can not play "+inputNumber+" after "+gameRoundResult));
    }

    /**
     * Get player aggregate with current player as root.
     *
     * @return [PlayerAggregate] player aggregate with current player as root.
     */
    public PlayerAggregate getPlayers() {
        return players;
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
