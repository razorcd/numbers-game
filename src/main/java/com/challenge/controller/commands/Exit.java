package com.challenge.controller.commands;

import com.challenge.GameContext;
import com.challenge.server.SocketChannel;

public class Exit implements Command<String> {

    private GameContext gameContext;
    private SocketChannel socketChannel;

    /**
     * Exit command.
     *
     * @param gameContext holds the state of the application.
     * @param socketChannel socket adapter.
     */
    public Exit(GameContext gameContext, SocketChannel socketChannel) {
        this.gameContext = gameContext;
        this.socketChannel = socketChannel;
    }

    /**
     * Execute exit command.
     *
     * @param data the input data.
     */
    @Override
    public void execute(String data) {
        socketChannel.send("server socket shutdown");
    }
}
