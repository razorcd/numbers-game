package com.challenge;

import com.challenge.game.Game;
import com.challenge.game.domain.InputNumber;
import com.challenge.game.domain.PlayerAggregate;
import com.challenge.game.model.Player;
import com.challenge.game.service.GameRoundService;
import com.challenge.game.service.algorithm.DivideByThree;
import com.challenge.game.service.algorithm.GameAlgorithm;
import com.challenge.game.service.algorithm.WinLogic;
import com.challenge.game.service.algorithm.WinWhenOne;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Singleton class to hold mutable state of current application.
 */
public class ServerAppState {

    private static final ServerAppState INSTANCE = new ServerAppState();

    private Game game;
    private List<Player> players = new CopyOnWriteArrayList<>();

    private ServerAppState() {
        this.game = Game.NULL;
    }

    public static ServerAppState getInstance() {
        return INSTANCE;
    }

    public void addPlayer(String playerName) {
        players.add(new Player(playerName));
    }

    public void startGame() {
        game = buildGame(players);
    }

    public void play(int number) {
        game = game.play(new InputNumber(number));
    }

    /**
     * Returns game in current state.
     * @return [Game] game in current state.
     */
    public Game getGame() {
        return game;
    }

    private Game buildGame(List<Player> players) {
        GameAlgorithm gameAlgorithm = new DivideByThree();
        WinLogic winLogic = new WinWhenOne();
        GameRoundService gameRoundService = new GameRoundService(gameAlgorithm, winLogic);

        PlayerAggregate playerAggregate = new PlayerAggregate(players, 0);

        return new Game(gameRoundService, playerAggregate);
    }
}
