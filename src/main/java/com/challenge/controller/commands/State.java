package com.challenge.controller.commands;

import com.challenge.ServerAppState;
import com.challenge.game.Game;
import com.challenge.game.domain.GameRoundResult;
import com.challenge.game.domain.PlayerAggregate;
import com.challenge.server.Messenger;

public class State implements Command<String> {

    private ServerAppState serverAppState;
    private Messenger<String, String> messenger;

    /**
     * State command.
     *
     * @param serverAppState holds the state of the application.
     * @param messenger socket adapter.
     */
    public State(ServerAppState serverAppState, Messenger<String, String> messenger) {
        this.serverAppState = serverAppState;
        this.messenger = messenger;
    }

    /**
     * Execute state command.
     *
     * @param data the input data.
     */
    @Override
    public void execute(String data) {
        Game currentGame = serverAppState.getGame();

        PlayerAggregate players = currentGame.getPlayers();
        GameRoundResult currentRoundResult = currentGame.getGameRoundResult();

        String finalMessage = buildFinalMessage(players, currentRoundResult);
        messenger.send(finalMessage);
    }

    private String buildFinalMessage(PlayerAggregate players, GameRoundResult currentRoundResult) {
        return String.valueOf(players.getRootPlayer()) +
                " is next. Last " +
                currentRoundResult;
    }
}
