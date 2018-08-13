package com.challenge.application.controller.commands;

import com.challenge.application.controller.exceptionhandler.GameExceptionHandler;
import com.challenge.application.game.GameManager;
import com.challenge.application.game.exception.ValidationException;
import com.challenge.application.game.model.Human;
import com.challenge.application.game.validator.UniquePlayerValidator;
import com.challenge.server.SocketChannel;

public class AddHuman extends ChainableCommand<String> {

    private GameManager gameManager;
    private SocketChannel socketChannel;

    /**
     * Add new human player command.
     *
     * @param gameManager holds the state of the application.
     * @param socketChannel socket adapter.
     */
    public AddHuman(GameManager gameManager, SocketChannel socketChannel) {
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
        Human newPlayer = new Human(Thread.currentThread().getName(), name);  //inject authorized user

        if (!validateGameManagerForAddingNewPlayer(newPlayer)) return;

        gameManager.addPlayer(newPlayer);
        socketChannel.broadcast("Added player " + newPlayer.getName() + " to game.");

        this.doNext(name);
    }

    private boolean validateGameManagerForAddingNewPlayer(Human newPlayer) {
        try {
            gameManager.validateOrThrow(new UniquePlayerValidator(newPlayer));
        } catch (ValidationException ex) {
            new GameExceptionHandler(socketChannel).handle(ex, newPlayer);
            return false;
        }
        return true;
    }
}