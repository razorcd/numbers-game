package com.challenge.controller.commands;

import com.challenge.ServerAppState;
import com.challenge.game.Game;
import com.challenge.game.domain.GameRoundResult;
import com.challenge.game.domain.PlayerAggregate;
import com.challenge.server.Messenger;
import com.challenge.utils.PropertiesConfigLoader;

import java.util.Optional;
import java.util.Random;

public class Start implements Command<String> {

    private static final String RANDOM_INPUT = PropertiesConfigLoader.getProperties().getProperty("com.challenge.random_start_input");

    private ServerAppState serverAppState;
    private Messenger messenger;

    /**
     * Start command.
     *
     * @param serverAppState holds the state of the application.
     * @param messenger socket adapter.
     */
    public Start(ServerAppState serverAppState, Messenger<String, String> messenger) {
        this.serverAppState = serverAppState;
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
            serverAppState.startGame();
        } catch (IllegalArgumentException e) {
            messenger.send(buildIllegalArgumentErrorMessage(e));
            return;
        }

        Game gameBeforePlay = serverAppState.getGame();

        int inputNumber = getStartInputOrRandom();
        serverAppState.play(inputNumber);

        Game gameAfterPlay = serverAppState.getGame();

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
        PlayerAggregate playersBeforePlay = beforePlay.getPlayers();
        GameRoundResult gameRoundEndResult = afterPlay.getGameRoundResult();

        return String.valueOf(playersBeforePlay.getRootPlayer()) +
                " started game and played a random number " +
                inputNumber +
                ". The result is " +
                gameRoundEndResult;
    }

    private String buildIllegalArgumentErrorMessage(IllegalArgumentException e) {
        return "ERROR: " + e.getMessage();
    }
}
