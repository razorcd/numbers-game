package com.challenge.application.game;

import com.challenge.application.game.domain.GameRoundResult;
import com.challenge.application.game.domain.InputNumber;
import com.challenge.application.game.domain.PlayerAggregate;
import com.challenge.application.game.exception.GameException;
import com.challenge.application.game.exception.ValidationException;
import com.challenge.application.game.model.IPlayer;
import com.challenge.application.game.service.GameRoundService;
import com.challenge.application.game.service.gamerules.gameround.DivideByThree;
import com.challenge.application.game.service.gamerules.gamewinlogic.IGameWinLogic;
import com.challenge.application.game.service.gamerules.gamewinlogic.WinWhenOne;
import com.challenge.application.game.service.gamerules.validator.DivideByThreeInputValidator;
import com.challenge.application.game.validator.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

/**
 * Context class holds a mutable state of current application.
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
     *
     * @param inputNumber the number to play.
     * @param playerInTurn the current authorized player
     */
    public synchronized void play(InputNumber inputNumber, IPlayer playerInTurn) {
        System.out.println(playerInTurn.getId() + " =a?n= " + game.get().getPlayerAggregate().getNext().getRootPlayer().getId()+game.get().getPlayerAggregate().getNext().getRootPlayer().getName());
        Game newGame = Stream.of(game.get())
            .peek(g -> g.validateOrThrow(new CanPlayGameValidator()))
            .peek(g -> g.validateOrThrow(new IsCurrentPlayerGameValidator(playerInTurn)))
            .peek(g -> g.validateOrThrow(new CanPlayInputNumberGameValidator(inputNumber)))
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
        DivideByThree gameAlgorithm = new DivideByThree();
        gameAlgorithm.addValidator(new DivideByThreeInputValidator());
        IGameWinLogic winLogic = new WinWhenOne();

        GameRoundService gameRoundService = new GameRoundService(gameAlgorithm, winLogic);

        PlayerAggregate playerAggregate = new PlayerAggregate(players, PlayerAggregate.DEFAULT_ROOT_INDEX);

        return Optional.of(new Game(gameRoundService, playerAggregate, GameRoundResult.INITIAL))
                .filter(g -> g.validate(new NewGameValidator()))
                .orElseThrow(() -> new GameException("can not create a game with invalid players"));
    }
}
