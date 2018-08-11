package com.challenge.application.controller.commands;

import com.challenge.application.game.GameManager;
import com.challenge.application.game.model.Machine;
import com.challenge.server.SocketChannel;

public class AddMachine extends ChainableCommand<String> {

    private GameManager gameManager;
    private SocketChannel socketChannel;

    /**
     * Add new machine player command.
     *
     * @param gameManager holds the state of the application.
     * @param socketChannel socket adapter.
     */
    public AddMachine(GameManager gameManager, SocketChannel socketChannel) {
        this.gameManager = gameManager;
        this.socketChannel = socketChannel;
    }

    /**
     * Execute add new machine player command.
     *
     * @param data the input data.
     */
    @Override
    public void execute(String data) {
        Machine newPlayer = Machine.generate();

        gameManager.addPlayer(newPlayer);
        socketChannel.broadcast("Added AI player " + newPlayer.getName() + " to game.");

        doNext(data);
    }
}
