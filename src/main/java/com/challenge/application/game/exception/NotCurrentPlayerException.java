package com.challenge.application.game.exception;

/**
 * Exception when not current player what's to take action.
 */
public class NotCurrentPlayerException extends GameException {
    public NotCurrentPlayerException(String message) {
        super(message);
    }
}
