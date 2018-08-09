package com.challenge.controller.commands;

import com.challenge.GameContext;
import com.challenge.server.Messenger;

public class Unknown implements Command<String> {

    private GameContext gameContext;
    private Messenger messenger;

    /**
     * Unknown command.
     *
     * @param gameContext holds the state of the application.
     * @param messenger socket adapter.
     */
    public Unknown(GameContext gameContext, Messenger messenger) {
        this.gameContext = gameContext;
        this.messenger = messenger;
    }

    /**
     * Execute unknown command.
     *
     * @param data the input data.
     */
    @Override
    public void execute(String data) {
        messenger.send("unknown command. Available commands are: ADD_PLAYER:player_name, START, PLAY:number, STATE, EXIT.");
    }
}
