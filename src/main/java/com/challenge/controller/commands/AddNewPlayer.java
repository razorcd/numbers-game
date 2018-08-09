package com.challenge.controller.commands;

import com.challenge.ServerAppState;
import com.challenge.server.Messenger;

public class AddNewPlayer implements Command<String> {

    private ServerAppState serverAppState;
    private Messenger messenger;

    /**
     * Add new player command.
     *
     * @param serverAppState holds the state of the application.
     * @param messenger socket adapter.
     */
    public AddNewPlayer(ServerAppState serverAppState, Messenger<String, String> messenger) {
        this.serverAppState = serverAppState;
        this.messenger = messenger;
    }

    /**
     * Execute add new player command.
     *
     * @param name name of player.
     */
    @Override
    public void execute(String name) {
        serverAppState.addPlayer(name);
        messenger.send("Added player " + name + " to game.");
    }
}
