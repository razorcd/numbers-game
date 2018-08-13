package com.challenge.application.controller.commands;

import com.challenge.application.gameofthree.game.GameService;
import com.challenge.server.SocketChannel;

public class Unknown extends ChainableCommand<String> {

    private GameService gameManager;
    private SocketChannel socketChannel;

    /**
     * Unknown command.
     *
     * @param gameManager holds the state of the application.
     * @param socketChannel socket adapter.
     */
    public Unknown(GameService gameManager, SocketChannel socketChannel) {
        this.gameManager = gameManager;
        this.socketChannel = socketChannel;
    }

    /**
     * Execute unknown command.
     *
     * @param data the input data.
     */
    @Override
    public void execute(String data) {
        socketChannel.send("unknown command. Available commands are: ADD_PLAYER:player_name, ADD_MACHINE, START, PLAY:number, STATE, EXIT.");
        doNext(data);
    }
}
