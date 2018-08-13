package com.challenge.application.controller.commands;

import com.challenge.application.controller.exceptionhandler.GameExceptionHandler;
import com.challenge.application.gameofthree.exception.GameException;
import com.challenge.application.gameofthree.game.IGameService;
import com.challenge.application.gameofthree.model.Human;
import com.challenge.server.ISocketChannel;

public class AddHuman extends ChainableCommand<String> {

    private IGameService gameService;
    private ISocketChannel socketChannel;

    /**
     * Add new human player command.
     *
     * @param gameService service to interact with running game.
     * @param socketChannel socket adapter.
     */
    public AddHuman(IGameService gameService, ISocketChannel socketChannel) {
        this.gameService = gameService;
        this.socketChannel = socketChannel;
    }

    /**
     * Execute add new human player command.
     *
     * @param name name of player.
     */
    @Override
    public void execute(String name) {
        Human authorizedPlayer = new Human(Thread.currentThread().getName(), name);  //inject authorized user

        try {
            gameService.addPlayer(authorizedPlayer);
        } catch (GameException ex) {
            new GameExceptionHandler(socketChannel).handle(ex, authorizedPlayer);
            return;
        }

        socketChannel.broadcast("Added player " + authorizedPlayer.getName() + " to game.");

        this.doNext(name);
    }
}
