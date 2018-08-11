package com.challenge.application.controller.commands;

import com.challenge.application.game.GameManager;
import com.challenge.server.SocketChannel;

public class AddPlayer implements Command<String> {

    private GameManager gameManager;
    private SocketChannel socketChannel;

    /**
     * Add new player command.
     *
     * @param gameManager holds the state of the application.
     * @param socketChannel socket adapter.
     */
    public AddPlayer(GameManager gameManager, SocketChannel socketChannel) {
        this.gameManager = gameManager;
        this.socketChannel = socketChannel;
    }

    /**
     * Execute add new player command.
     *
     * @param name name of player.
     */
    @Override
    public void execute(String name) {
        gameManager.addPlayer(name);
        socketChannel.send("Added player " + name + " to game.");
    }
}
