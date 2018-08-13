package com.challenge.application.controller.commands;

import com.challenge.application.gameofthree.game.IGameService;
import com.challenge.server.ISocketChannel;
import com.challenge.server.SocketChannel;

public class Unknown extends ChainableCommand<String> {

    private IGameService gameService;
    private ISocketChannel socketChannel;

    /**
     * Unknown command.
     *
     * @param gameService service to interact with running game.
     * @param socketChannel socket adapter.
     */
    public Unknown(IGameService gameService, ISocketChannel socketChannel) {
        this.gameService = gameService;
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
