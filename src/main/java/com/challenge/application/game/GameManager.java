package com.challenge.application.game;

import com.challenge.application.game.Game;
import com.challenge.application.game.domain.InputNumber;
import com.challenge.application.game.domain.PlayerAggregate;
import com.challenge.application.game.exception.GameException;
import com.challenge.application.game.model.Player;
import com.challenge.application.game.service.GameRoundService;
import com.challenge.application.game.service.algorithm.DivideByThree;
import com.challenge.application.game.service.algorithm.IWinLogic;
import com.challenge.application.game.service.algorithm.WinWhenOne;
import com.challenge.application.game.service.algorithm.validator.DivideByThreeInputValidator;
import com.challenge.application.game.validator.CanPlayGameValidator;
import com.challenge.application.game.validator.CanPlayInputNumberGameValidator;
import com.challenge.application.game.validator.NewGameValidator;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

/**
 * Context class holds a mutable state of current application.
 */
public class GameManager {

    private Game game;
    private List<Player> players = new CopyOnWriteArrayList<>();

    /**
     * Build application context.
     */
    public GameManager() {
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
