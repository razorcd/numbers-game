package com.challenge.application.controller.commands;

import com.challenge.application.controller.exceptionhandler.GameExceptionHandler;
import com.challenge.application.game.GameService;
import com.challenge.application.game.exception.GameException;
import com.challenge.application.game.model.Human;
import com.challenge.server.SocketChannel;

public class AddHuman extends ChainableCommand<String> {

    private GameService gameManager;
    private SocketChannel socketChannel;

    /**
     * Add new human player command.
     *
     * @param gameManager holds the state of the application.
     * @param socketChannel socket adapter.
     */
    public AddHuman(GameService gameManager, SocketChannel socketChannel) {
        this.gameManager = gameManager;
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
            gameManager.addPlayer(authorizedPlayer);
        } catch (GameException ex) {
            new GameExceptionHandler(socketChannel).handle(ex, authorizedPlayer);
            return;
        }

        socketChannel.broadcast("Added player " + authorizedPlayer.getName() + " to game.");

        this.doNext(name);
    }
}
