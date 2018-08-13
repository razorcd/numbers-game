package com.challenge.application.controller.commands;

import com.challenge.application.gameofthree.game.IGameService;
import com.challenge.application.gameofthree.model.Human;
import com.challenge.application.gameofthree.model.IPlayer;
import com.challenge.server.ISocketChannel;

public class Exit extends ChainableCommand<String> {

    private IGameService gameService;
    private ISocketChannel socketChannel;

    /**
     * Exit command.
     *
     * @param gameService service to interact with running game.
     * @param socketChannel socket adapter.
     */
    public Exit(IGameService gameService, ISocketChannel socketChannel) {
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
        IPlayer authorizedPlayer = new Human(Thread.currentThread().getName(), "");  //inject authorized user
        gameService.removePlayer(authorizedPlayer);
        gameService.stopGame();

        socketChannel.send("Goodbye.");
        doNext(data);
    }
}
