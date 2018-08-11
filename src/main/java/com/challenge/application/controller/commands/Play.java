package com.challenge.application.controller.commands;

import com.challenge.application.game.GameManager;
import com.challenge.application.game.Game;
import com.challenge.application.game.domain.GameRoundResult;
import com.challenge.application.game.domain.PlayerAggregate;
import com.challenge.application.game.exception.GameException;
import com.challenge.server.SocketChannel;

public class Play implements Command<String> {

    private GameManager gameManager;
    private SocketChannel socketChannel;

    /**
     * Play command.
     *
     * @param gameManager holds the state of the application.
     * @param socketChannel socket adapter.
     */
    public Play(GameManager gameManager, SocketChannel socketChannel) {
        this.gameManager = gameManager;
        this.socketChannel = socketChannel;
    }

    /**
     * Execute play command.
     *
     * @param inputNumber the played input number.
     */
    @Override
    public void execute(String inputNumber) {
        Game gameBeforePlay = gameManager.getGame();
        PlayerAggregate playersBeforePlay = gameBeforePlay.getPlayerAggregate();

        try {
            gameManager.play(Integer.parseInt(inputNumber));
        } catch (GameException | NumberFormatException ex) {
            String errMessage = buildErrorMessage(playersBeforePlay, ex);
            socketChannel.send(errMessage);
            return;
        }

        Game gameAfterPlay = gameManager.getGame();
        GameRoundResult playingRoundResult = gameAfterPlay.getGameRoundResult();

        String message = buildFinalMessage(playersBeforePlay, playingRoundResult, inputNumber);
        socketChannel.send(message);
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