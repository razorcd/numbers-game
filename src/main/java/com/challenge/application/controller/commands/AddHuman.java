package com.challenge.application.controller.commands;

import com.challenge.application.controller.exceptionhandler.GameExceptionHandler;
import com.challenge.application.gameofthree.exception.GameException;
import com.challenge.application.gameofthree.game.GameService;
import com.challenge.application.gameofthree.model.Human;
import com.challenge.server.SocketChannel;

public class AddHuman extends ChainableCommand<String> {

    private GameService gameService;
    private SocketChannel socketChannel;

    /**
     * Add new human player command.
     *
     * @param gameService service to interact with running game.
     * @param socketChannel socket adapter.
     */
    public AddHuman(GameService gameService, SocketChannel socketChannel) {
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
