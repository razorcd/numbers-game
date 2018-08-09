package com.challenge.controller.commands;

import com.challenge.ServerAppState;
import com.challenge.game.Game;
import com.challenge.game.domain.GameRoundResult;
import com.challenge.game.domain.PlayerAggregate;
import com.challenge.server.Messenger;

public class Play implements Command<String> {

    private ServerAppState serverAppState;
    private Messenger<String, String> messenger;

    /**
     * Play command.
     *
     * @param serverAppState holds the state of the application.
     * @param messenger socket adapter.
     */
    public Play(ServerAppState serverAppState, Messenger<String, String> messenger) {
        this.serverAppState = serverAppState;
        this.messenger = messenger;
    }

    /**
     * Execute play command.
     *
     * @param inputNumber the played input number.
     */
    @Override
    public void execute(String inputNumber) {
        Game gameBeforePlay = serverAppState.getGame();
        PlayerAggregate playersBeforePlay = gameBeforePlay.getPlayers();

        try {
            serverAppState.play(Integer.parseInt(inputNumber));
        } catch (NumberFormatException | IllegalStateException ex) {
            String errMessage = buildErrorMessage(playersBeforePlay, ex);
            messenger.send(errMessage);
            return;
        }

        Game gameAfterPlay = serverAppState.getGame();
        GameRoundResult playingRoundResult = gameAfterPlay.getGameRoundResult();

        String message = buildFinalMessage(playersBeforePlay, playingRoundResult, inputNumber);
        messenger.send(message);
    }

    private String buildFinalMessage(PlayerAggregate playersBeforePlay, GameRoundResult playingRoundResult, String inputNumber) {
        return String.valueOf(playersBeforePlay.getRootPlayer()) +
                " played number " +
                inputNumber +
                ". The result is " +
                playingRoundResult;
    }

    private String buildErrorMessage(PlayerAggregate playersBeforePlay, RuntimeException ex) {
        return "ERROR: " +
                playersBeforePlay.getRootPlayer() +
                ": " +
                ex.getMessage();
    }
}