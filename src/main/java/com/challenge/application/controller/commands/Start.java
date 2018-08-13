package com.challenge.application.controller.commands;

import com.challenge.application.controller.exceptionhandler.GameExceptionHandler;
import com.challenge.application.gameofthree.exception.GameException;
import com.challenge.application.gameofthree.game.Game;
import com.challenge.application.gameofthree.game.IGameService;
import com.challenge.application.gameofthree.game.domain.PlayerAggregate;
import com.challenge.application.gameofthree.gameround.domain.GameRoundResult;
import com.challenge.application.gameofthree.model.Human;
import com.challenge.server.ISocketChannel;
import com.challenge.server.SocketChannel;

public class Start extends ChainableCommand<String> {

    private IGameService gameService;
    private ISocketChannel socketChannel;

    /**
     * Start command.
     *
     * @param gameService service to interact with running game.
     * @param socketChannel socket adapter.
     */
    public Start(IGameService gameService, ISocketChannel socketChannel) {
        this.gameService = gameService;
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
            gameService.startGame();
        } catch (GameException e) {
            new GameExceptionHandler(socketChannel).handle(e, authorizedCurrentPlayer);
            return;
        }

        Game gameAfterStart = gameService.getGame();
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
