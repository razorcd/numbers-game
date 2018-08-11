package com.challenge.application.controller.commands;

import com.challenge.application.game.GameManager;
import com.challenge.application.game.Game;
import com.challenge.application.game.domain.GameRoundResult;
import com.challenge.application.game.domain.PlayerAggregate;
import com.challenge.application.game.exception.GameException;
import com.challenge.server.SocketChannel;
import com.challenge.application.utils.PropertiesConfigLoader;

import java.util.Optional;
import java.util.Random;

public class Start implements Command<String> {

    private static final String RANDOM_INPUT = PropertiesConfigLoader.getProperties().
            getProperty("com.challenge.application.game.random_start_input");

    private GameManager gameManager;
    private SocketChannel socketChannel;

    /**
     * Start command.
     *
     * @param gameManager holds the state of the application.
     * @param socketChannel socket adapter.
     */
    public Start(GameManager gameManager, SocketChannel socketChannel) {
        this.gameManager = gameManager;
        this.socketChannel = socketChannel;
    }

    /**
     * Execute start command.
     *
     * @param data input data.
     */
    @Override
    public void execute(String data) {
        try {
            gameManager.startGame();
        } catch (GameException e) {
            socketChannel.send(buildGameExceptionMessage(e));
            return;
        }

        Game gameBeforePlay = gameManager.getGame();

        int inputNumber = getStartInputOrRandom();
        gameManager.play(inputNumber);

        Game gameAfterPlay = gameManager.getGame();

        String finalMessage = buildFinalMessage(gameBeforePlay, gameAfterPlay, inputNumber);
        socketChannel.send(finalMessage);
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
