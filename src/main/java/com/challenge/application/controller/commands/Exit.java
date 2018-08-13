package com.challenge.application.controller.commands;

import com.challenge.application.game.GameManager;
import com.challenge.server.SocketChannel;

public class Exit extends ChainableCommand<String> {

    private GameManager gameManager;
    private SocketChannel socketChannel;

    /**
     * Exit command.
     *
     * @param gameManager holds the state of the application.
     * @param socketChannel socket adapter.
     */
    public Exit(GameManager gameManager, SocketChannel socketChannel) {
        this.gameManager = gameManager;
        this.socketChannel = socketChannel;
    }

    /**
     * Execute exit command.
     *
     * @param data the input data.
     */
    @Override
    public void execute(String data) {
        socketChannel.broadcast("Goodbye.");
//TODO: remove yourself from game and reset game
        doNext(data);
    }
}