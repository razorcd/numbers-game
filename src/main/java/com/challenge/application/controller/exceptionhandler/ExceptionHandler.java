package com.challenge.application.controller.exceptionhandler;

import com.challenge.application.game.model.IPlayer;

public interface ExceptionHandler<E extends RuntimeException> {

    /**
     * Handle exceptions for current player.
     *
     * @param ex the exception to handle
     * @param currentPlayer the current player
     */
    void handle(E ex, IPlayer currentPlayer);
}
