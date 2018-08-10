package com.challenge.controller.commands;

import com.challenge.GameContext;
import com.challenge.game.Game;
import com.challenge.game.domain.GameRoundResult;
import com.challenge.game.domain.PlayerAggregate;
import com.challenge.server.Messenger;

public class State implements Command<String> {

    private GameContext gameContext;
    private Messenger messenger;

    /**
     * State command.
     *
     * @param gameContext holds the state of the application.
     * @param messenger socket adapter.
     */
    public State(GameContext gameContext, Messenger messenger) {
        this.gameContext = gameContext;
        this.messenger = messenger;
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
        messenger.send(finalMessage);
    }

    private String buildFinalMessage(PlayerAggregate players, GameRoundResult currentRoundResult) {
        return String.valueOf(players.getRootPlayer()) +
                " is next. Last " +
                currentRoundResult;
    }
}
