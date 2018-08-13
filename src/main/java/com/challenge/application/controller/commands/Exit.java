package com.challenge.application.controller.commands;

import com.challenge.application.gameofthree.game.GameService;
import com.challenge.server.SocketChannel;

public class Exit extends ChainableCommand<String> {

    private GameService gameManager;
    private SocketChannel socketChannel;

    /**
     * Exit command.
     *
     * @param gameManager holds the state of the application.
     * @param socketChannel socket adapter.
     */
    public Exit(GameService gameManager, SocketChannel socketChannel) {
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
        doNext(data);
    }
}
