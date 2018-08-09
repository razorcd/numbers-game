package com.challenge.controller.commands;

import com.challenge.GameContext;
import com.challenge.server.Messenger;

public class AddPlayer implements Command<String> {

    private GameContext gameContext;
    private Messenger messenger;

    /**
     * Add new player command.
     *
     * @param gameContext holds the state of the application.
     * @param messenger socket adapter.
     */
    public AddPlayer(GameContext gameContext, Messenger messenger) {
        this.gameContext = gameContext;
        this.messenger = messenger;
    }

    /**
     * Execute add new player command.
     *
     * @param name name of player.
     */
    @Override
    public void execute(String name) {
        gameContext.addPlayer(name);
        messenger.send("Added player " + name + " to game.");
    }
}
