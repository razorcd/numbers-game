package com.challenge.application.game;

import com.challenge.application.game.domain.GameRoundResult;
import com.challenge.application.game.domain.PlayerAggregate;
import com.challenge.application.game.exception.GameException;
import com.challenge.application.game.gameround.GameRoundService;
import com.challenge.application.game.gameround.gamerules.gameplaylogic.IGameRoundLogic;
import com.challenge.application.game.gameround.gamerules.gamewinlogic.IGameWinLogic;
import com.challenge.application.game.model.IPlayer;
import com.challenge.application.game.validator.NewGameValidator;

import java.util.List;

public class GameFactory {

    private IGameRoundLogic gameLogic;
    private IGameWinLogic winLogic;

    /**
     * Create a new game by defining it's play logic and win logic.
     *
     * @param gameLogic the logic of the game round.
     * @param winLogic the win logic to end game.
     */
    public GameFactory(IGameRoundLogic gameLogic,
                       IGameWinLogic winLogic) {
        this.gameLogic = gameLogic;
        this.winLogic = winLogic;
    }

    /**
     * Build the new game with specified players.
     *
     * @param players a list of human and AI players that will play the game.
     * @return [Game] the new game
     */
    public Game buildNewGame(List<IPlayer> players) {
        GameRoundService gameRoundService = new GameRoundService(gameLogic, winLogic);

        PlayerAggregate playerAggregate = new PlayerAggregate(players, PlayerAggregate.DEFAULT_ROOT_INDEX);

        Game game = new Game(gameRoundService, playerAggregate, GameRoundResult.getInitial());

        if (!game.validate(new NewGameValidator())) {
            throw new GameException("can not create a game with invalid players");
        }

        return game;
    }
}
