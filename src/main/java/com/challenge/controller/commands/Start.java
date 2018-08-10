package com.challenge.controller.commands;

import com.challenge.GameContext;
import com.challenge.game.Game;
import com.challenge.game.domain.GameRoundResult;
import com.challenge.game.domain.PlayerAggregate;
import com.challenge.game.exception.GameException;
import com.challenge.server.Messenger;
import com.challenge.utils.PropertiesConfigLoader;

import java.util.Optional;
import java.util.Random;

public class Start implements Command<String> {

    private static final String RANDOM_INPUT = PropertiesConfigLoader.getProperties().
            getProperty("com.challenge.game.random_start_input");

    private GameContext gameContext;
    private Messenger messenger;

    /**
     * Start command.
     *
     * @param gameContext holds the state of the application.
     * @param messenger socket adapter.
     */
    public Start(GameContext gameContext, Messenger messenger) {
        this.gameContext = gameContext;
        this.messenger = messenger;
    }

    /**
     * Execute start command.
     *
     * @param data input data.
     */
    @Override
    public void execute(String data) {
        try {
            gameContext.startGame();
        } catch (GameException e) {
            messenger.send(buildGameExceptionMessage(e));
            return;
        }

        Game gameBeforePlay = gameContext.getGame();

        int inputNumber = getStartInputOrRandom();
        gameContext.play(inputNumber);

        Game gameAfterPlay = gameContext.getGame();

        String finalMessage = buildFinalMessage(gameBeforePlay, gameAfterPlay, inputNumber);
        messenger.send(finalMessage);
    }

    /**
     * Read start input number from properties or generate random.
     *
     * @return int the start input number of the game
     */
    private int getStartInputOrRandom() {
        return Optional.ofNullable((RANDOM_INPUT))
                .map(Integer::parseInt)
                .orElse(new Random().nextInt(150) + 10);
    }

    private String buildFinalMessage(Game beforePlay, Game afterPlay, int inputNumber) {
        PlayerAggregate playersBeforePlay = beforePlay.getPlayerAggregate();
        GameRoundResult gameRoundEndResult = afterPlay.getGameRoundResult();

        return String.valueOf(playersBeforePlay.getRootPlayer()) +
                " started game and played a random number " +
                inputNumber +
                ". The result is " +
                gameRoundEndResult;
    }

    private String buildGameExceptionMessage(GameException e) {
        return "ERROR: " + e.getMessage();
    }
}
