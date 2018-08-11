package com.challenge.controller.commands;

import com.challenge.GameContext;
import com.challenge.server.SocketChannel;

public class Unknown implements Command<String> {

    private GameContext gameContext;
    private SocketChannel socketChannel;

    /**
     * Unknown command.
     *
     * @param gameContext holds the state of the application.
     * @param socketChannel socket adapter.
     */
    public Unknown(GameContext gameContext, SocketChannel socketChannel) {
        this.gameContext = gameContext;
        this.socketChannel = socketChannel;
    }

    /**
     * Execute unknown command.
     *
     * @param data the input data.
     */
    @Override
    public void execute(String data) {
        socketChannel.send("unknown command. Available commands are: ADD_PLAYER:player_name, START, PLAY:number, STATE, EXIT.");
    }
}
