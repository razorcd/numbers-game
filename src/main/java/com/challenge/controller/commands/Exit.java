package com.challenge.controller.commands;

import com.challenge.ServerAppState;
import com.challenge.server.Messenger;

public class Exit implements Command<String> {

    private ServerAppState serverAppState;
    private Messenger<String, String> messenger;

    /**
     * Exit command.
     *
     * @param serverAppState holds the state of the application.
     * @param messenger socket adapter.
     */
    public Exit(ServerAppState serverAppState, Messenger<String, String> messenger) {
        this.serverAppState = serverAppState;
        this.messenger = messenger;
    }

    /**
     * Execute exit command.
     *
     * @param data the input data.
     */
    @Override
    public void execute(String data) {
        messenger.send("server socket shutdown");
    }
}
