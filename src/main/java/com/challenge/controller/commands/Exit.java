package com.challenge.controller.commands;

import com.challenge.GameContext;
import com.challenge.server.Messenger;

public class Exit implements Command<String> {

    private GameContext gameContext;
    private Messenger messenger;

    /**
     * Exit command.
     *
     * @param gameContext holds the state of the application.
     * @param messenger socket adapter.
     */
    public Exit(GameContext gameContext, Messenger messenger) {
        this.gameContext = gameContext;
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
