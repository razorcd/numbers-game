package com.challenge.application.controller.commands;

import java.util.Arrays;

public enum CommandType {
    ADD_PLAYER, ADD_MACHINE, START, PLAY, STATE, EXIT, UNKNOWN;

    /**
     * Get value of enum for the input string or {@link #UNKNOWN} as default.
     * @param commandTypeStr command type as string
     * @return {@link CommandType}
     */
    public static CommandType valueOfString(final String commandTypeStr) {
        return Arrays.stream(CommandType.values())
                .filter(commandType -> commandType.toString().equals(commandTypeStr))
                .findFirst()
                .orElse(UNKNOWN);
    }



}
