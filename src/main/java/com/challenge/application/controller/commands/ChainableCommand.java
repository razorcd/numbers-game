package com.challenge.application.controller.commands;

import java.util.Optional;

public abstract class ChainableCommand<T> {

    /**
     * Holds the next command that will be called after this one is processed.
     * Link list command structure.
     */
    protected Optional<ChainableCommand<T>> successor = Optional.empty();

    /**
     * Execute command.
     *
     * @param data the input data of the command.
     */
    public abstract void execute(T data);

    /**
     * Add a successor command to be executed after this one.
     *
     * @param successor the next command.
     */
    public void setSuccessor(ChainableCommand<T> successor) {
        this.successor = Optional.of(successor);
    }

    protected void doNext(T data) {
        successor.ifPresent(s -> s.execute(data));
    }
}