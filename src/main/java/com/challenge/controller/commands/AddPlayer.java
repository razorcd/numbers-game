package com.challenge.controller.commands;

import com.challenge.GameContext;
import com.challenge.server.SocketChannel;

public class AddPlayer implements Command<String> {

    private GameContext gameContext;
    private SocketChannel socketChannel;

    /**
     * Add new player command.
     *
     * @param gameContext holds the state of the application.
     * @param socketChannel socket adapter.
     */
    public AddPlayer(GameContext gameContext, SocketChannel socketChannel) {
        this.gameContext = gameContext;
        this.socketChannel = socketChannel;
    }

    /**
     * Execute add new player command.
     *
     * @param name name of player.
     */
    @Override
    public void execute(String name) {
        gameContext.addPlayer(name);
        socketChannel.send("Added player " + name + " to game.");
    }
}
