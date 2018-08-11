package com.challenge.controller.commands;

import com.challenge.GameContext;
import com.challenge.game.Game;
import com.challenge.game.domain.GameRoundResult;
import com.challenge.game.domain.PlayerAggregate;
import com.challenge.server.SocketChannel;

public class State implements Command<String> {

    private GameContext gameContext;
    private SocketChannel socketChannel;

    /**
     * State command.
     *
     * @param gameContext holds the state of the application.
     * @param socketChannel socket adapter.
     */
    public State(GameContext gameContext, SocketChannel socketChannel) {
        this.gameContext = gameContext;
        this.socketChannel = socketChannel;
    }

    /**
     * Execute state command.
     *
     * @param data the input data.
     */
    @Override
    public void execute(String data) {
        Game currentGame = gameContext.getGame();

        PlayerAggregate players = currentGame.getPlayerAggregate();
        GameRoundResult currentRoundResult = currentGame.getGameRoundResult();

        String finalMessage = buildFinalMessage(players, currentRoundResult);
        socketChannel.send(finalMessage);
    }

    private String buildFinalMessage(PlayerAggregate players, GameRoundResult currentRoundResult) {
        return String.valueOf(players.getRootPlayer()) +
                " is next. Last " +
                currentRoundResult;
    }
}
