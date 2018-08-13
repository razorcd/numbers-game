package com.challenge.application.gameofthree.exception;

/**
 * Exception for cases when not player in turn is calling commands.
 */
public class NotCurrentPlayerException extends GameException {
    public NotCurrentPlayerException(String message) {
        super(message);
    }
}
