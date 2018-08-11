package com.challenge.application.controller.commands;

import com.challenge.application.controller.exceptionhandler.GameExceptionHandler;
import com.challenge.application.game.GameManager;
import com.challenge.application.game.Game;
import com.challenge.application.game.domain.GameRoundResult;
import com.challenge.application.game.domain.InputNumber;
import com.challenge.application.game.domain.PlayerAggregate;
import com.challenge.application.game.exception.GameException;
import com.challenge.application.game.model.Player;
import com.challenge.server.SocketChannel;

public class Start implements Command<String> {

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
        Player authorizedCurrentPlayer = new Player(Thread.currentThread().getName(), "");
        try {
            gameManager.startGame();
        } catch (GameException e) {
            new GameExceptionHandler(socketChannel).handle(e, authorizedCurrentPlayer);
            return;
        }

        Game gameBeforePlay = gameManager.getGame();
        InputNumber beginningInputNumber = InputNumber.getStartNumber();
        try {
            gameManager.play(beginningInputNumber, authorizedCurrentPlayer);
        } catch (GameException ex) {
            new GameExceptionHandler(socketChannel).handle(ex, authorizedCurrentPlayer);
        }

        Game gameAfterPlay = gameManager.getGame();
        String finalMessage = buildFinalMessage(gameBeforePlay, gameAfterPlay, beginningInputNumber);

        socketChannel.broadcast(finalMessage);
    }

    private String buildFinalMessage(Game beforePlay, Game afterPlay, InputNumber beginningInputNumber) {
        PlayerAggregate playersBeforePlay = beforePlay.getPlayerAggregate();
        GameRoundResult gameRoundEndResult = afterPlay.getGameRoundResult();

        return playersBeforePlay.getRootPlayer() +
                " started game and played a random number " +
                beginningInputNumber +
                ". The result is " +
                gameRoundEndResult;
    }
}
