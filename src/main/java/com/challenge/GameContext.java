package com.challenge;

import com.challenge.game.Game;
import com.challenge.game.domain.InputNumber;
import com.challenge.game.domain.PlayerAggregate;
import com.challenge.game.exception.GameException;
import com.challenge.game.model.Player;
import com.challenge.game.service.GameRoundService;
import com.challenge.game.service.algorithm.DivideByThree;
import com.challenge.game.service.algorithm.IGameAlgorithm;
import com.challenge.game.service.algorithm.IWinLogic;
import com.challenge.game.service.algorithm.WinWhenOne;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
        game = game.play(new InputNumber(number));
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
        IGameAlgorithm IGameAlgorithm = new DivideByThree();
        IWinLogic IWinLogic = new WinWhenOne();
        GameRoundService gameRoundService = new GameRoundService(IGameAlgorithm, IWinLogic);

        PlayerAggregate playerAggregate = new PlayerAggregate(players, PlayerAggregate.DEFAULT_ROOT_INDEX);

//        if (!playerAggregate.isValid()) throw new GameException("can not create a game with invalid players");
        return new Game(gameRoundService, playerAggregate);
    }
}
