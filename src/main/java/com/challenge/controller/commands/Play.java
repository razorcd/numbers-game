package com.challenge.controller.commands;

import com.challenge.GameContext;
import com.challenge.game.Game;
import com.challenge.game.domain.GameRoundResult;
import com.challenge.game.domain.PlayerAggregate;
import com.challenge.game.exception.GameException;
import com.challenge.server.Messenger;

public class Play implements Command<String> {

    private GameContext gameContext;
    private Messenger messenger;

    /**
     * Play command.
     *
     * @param gameContext holds the state of the application.
     * @param messenger socket adapter.
     */
    public Play(GameContext gameContext, Messenger messenger) {
        this.gameContext = gameContext;
        this.messenger = messenger;
    }

    /**
     * Execute play command.
     *
     * @param inputNumber the played input number.
     */
    @Override
    public void execute(String inputNumber) {
        Game gameBeforePlay = gameContext.getGame();
        PlayerAggregate playersBeforePlay = gameBeforePlay.getPlayerAggregate();

        try {
            gameContext.play(Integer.parseInt(inputNumber));
        } catch (GameException | NumberFormatException ex) {
            String errMessage = buildErrorMessage(playersBeforePlay, ex);
            messenger.send(errMessage);
            return;
        }

        Game gameAfterPlay = gameContext.getGame();
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