package com.challenge.application.controller.commands;

import com.challenge.application.gameofthree.game.IGameService;
import com.challenge.application.gameofthree.model.Machine;
import com.challenge.server.ISocketChannel;

public class AddMachine extends ChainableCommand<String> {

    private IGameService gameService;
    private ISocketChannel socketChannel;

    /**
     * Add new machine player command.
     *
     * @param gameService service to interact with running game.
     * @param socketChannel socket adapter.
     */
    public AddMachine(IGameService gameService, ISocketChannel socketChannel) {
        this.gameService = gameService;
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

        gameService.addPlayer(newPlayer);
        socketChannel.broadcast("Added AI player " + newPlayer.getName() + " to game.");

        doNext(data);
    }
}
