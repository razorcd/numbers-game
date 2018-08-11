package com.challenge.application.controller.commands;

import com.challenge.application.controller.exceptionhandler.GameExceptionHandler;
import com.challenge.application.game.Game;
import com.challenge.application.game.GameManager;
import com.challenge.application.game.domain.GameRoundResult;
import com.challenge.application.game.domain.PlayerAggregate;
import com.challenge.application.game.exception.GameException;
import com.challenge.application.game.model.Human;
import com.challenge.server.SocketChannel;

public class Start extends ChainableCommand<String> {

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
        Human authorizedCurrentPlayer = new Human(Thread.currentThread().getName(), "");
        try {
            gameManager.startGame();
        } catch (GameException e) {
            new GameExceptionHandler(socketChannel).handle(e, authorizedCurrentPlayer);
            return;
        }

        Game gameAfterStart = gameManager.getGame();
        String finalMessage = buildFinalMessage(gameAfterStart);

        socketChannel.broadcast(finalMessage);

        GameRoundResult gameRoundResultAfterStart = gameAfterStart.getGameRoundResult();
        doNext(String.valueOf(gameRoundResultAfterStart.getOutputNumber().getValue()));
    }

    private String buildFinalMessage(Game gameAfterStart) {
        PlayerAggregate playerAfterStart = gameAfterStart.getPlayerAggregate();
        GameRoundResult gameRoundEndResult = gameAfterStart.getGameRoundResult();

        return "Game started. The starting number is " +
                gameRoundEndResult.getOutputNumber() +
                "." +
                " Next to play is " + playerAfterStart.getRootPlayer() +
                ".";
    }
}
