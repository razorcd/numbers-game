package com.challenge;

import com.challenge.game.Game;
import com.challenge.game.domain.InputNumber;
import com.challenge.game.domain.PlayerAggregate;
import com.challenge.game.exception.GameException;
import com.challenge.game.model.Player;
import com.challenge.game.service.GameRoundService;
import com.challenge.game.service.algorithm.DivideByThree;
import com.challenge.game.service.algorithm.IWinLogic;
import com.challenge.game.service.algorithm.WinWhenOne;
import com.challenge.game.service.algorithm.validator.DivideByThreeInputValidator;
import com.challenge.game.validator.CanPlayGameValidator;
import com.challenge.game.validator.CanPlayInputNumberGameValidator;
import com.challenge.game.validator.NewGameValidator;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

/**
 * Context class holds a mutable state of current application.
 */
public class GameContext {

    private Game game;
    private List<Player> players = new CopyOnWriteArrayList<>();

    /**
     * Build application context.
     */
    public GameContext() {
        this.game = Game.NULL;
    }

    public void addPlayer(String playerName) {
        players.add(new Player(playerName));
    }

    public void startGame() {
        game = buildNewGame(players);
    }

    public void play(int number) {
        game = Stream.of(game)
                .peek(g -> g.validateOrThrow(new CanPlayGameValidator()))
                .peek(g -> g.validateOrThrow(new CanPlayInputNumberGameValidator(new InputNumber(number))))
                .map(g -> g.play(new InputNumber(number)))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Error while playing number "+number+" with game "+game));
    }

    /**
     * Get game in current state.
     *
     * @return [Game] game in current state.
     */
    public Game getGame() {
        return game;
    }

    private Game buildNewGame(List<Player> players) {
        DivideByThree gameAlgorithm = new DivideByThree();
        gameAlgorithm.addValidator(new DivideByThreeInputValidator());

        IWinLogic winLogic = new WinWhenOne();

        GameRoundService gameRoundService = new GameRoundService(gameAlgorithm, winLogic);

        PlayerAggregate playerAggregate = new PlayerAggregate(players, PlayerAggregate.DEFAULT_ROOT_INDEX);

        return Optional.of(new Game(gameRoundService, playerAggregate))
                .filter(g -> g.validate(new NewGameValidator()))
                .orElseThrow(() -> new GameException("can not create a game with invalid players"));
    }
}
