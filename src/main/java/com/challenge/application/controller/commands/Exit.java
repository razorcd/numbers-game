package com.challenge.application.controller.commands;

import com.challenge.application.gameofthree.game.GameService;
import com.challenge.server.SocketChannel;

public class Exit extends ChainableCommand<String> {

    private GameService gameService;
    private SocketChannel socketChannel;

    /**
     * Exit command.
     *
     * @param gameService service to interact with running game.
     * @param socketChannel socket adapter.
     */
    public Exit(GameService gameService, SocketChannel socketChannel) {
        this.gameService = gameService;
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
