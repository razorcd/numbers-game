package com.challenge.application.controller.commands;

import com.challenge.application.gameofthree.game.Game;
import com.challenge.application.gameofthree.game.GameService;
import com.challenge.application.gameofthree.game.domain.PlayerAggregate;
import com.challenge.application.gameofthree.gameround.domain.GameRoundResult;
import com.challenge.server.SocketChannel;

public class State extends ChainableCommand<String> {

    private GameService gameService;
    private SocketChannel socketChannel;

    /**
     * State command.
     *
     * @param gameService service to interact with running game.
     * @param socketChannel socket adapter.
     */
    public State(GameService gameService, SocketChannel socketChannel) {
        this.gameService = gameService;
        this.socketChannel = socketChannel;
    }

    /**
     * Execute state command.
     *
     * @param data the input data.
     */
    @Override
    public void execute(String data) {
        Game currentGame = gameService.getGame();

        PlayerAggregate players = currentGame.getPlayerAggregate();
        GameRoundResult currentRoundResult = currentGame.getGameRoundResult();

        String finalMessage = buildFinalMessage(players, currentRoundResult);
        socketChannel.send(finalMessage);

        doNext(data);
    }

    private String buildFinalMessage(PlayerAggregate players, GameRoundResult currentRoundResult) {
        return players.getRootPlayer() +
                " is next. Last " +
                currentRoundResult;
    }
}
