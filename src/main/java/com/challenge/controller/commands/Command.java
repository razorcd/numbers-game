package com.challenge.controller.commands;

/**
 * Interface to implement a Command class
 */
public interface Command<T> {

    /**
     * Execute user command.
     *
     * @param data the input data of the command.
     */
    void execute(T data);
}