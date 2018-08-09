package com.challenge.controller.commands;

import com.challenge.ServerAppState;
import com.challenge.server.Messenger;

public class Unknown implements Command<String> {

    private ServerAppState serverAppState;
    private Messenger<String, String> messenger;

    /**
     * Unknown command.
     *
     * @param serverAppState holds the state of the application.
     * @param messenger socket adapter.
     */
    public Unknown(ServerAppState serverAppState, Messenger<String, String> messenger) {
        this.serverAppState = serverAppState;
        this.messenger = messenger;
    }

    /**
     * Execute unknown command.
     *
     * @param data the input data.
     */
    @Override
    public void execute(String data) {
        messenger.send("unknown command. Available commands are: ADD_NEW_PLAYER:player_name, START, PLAY:number, STATE, EXIT.");
    }
}
