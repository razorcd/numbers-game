package com.challenge.controller.dto;

import com.challenge.controller.commands.Command;

import java.util.Objects;
import java.util.Optional;

public class UserInputDto {

    private String command;
    private String data;
//    private String playerName;
//    private String authToken;


    /**
     * Dto representing the user input.
     *
     * @param command command to execute
     * @param data parameters for the command
     */
    public UserInputDto(String command, String data) {
        this.command = command;
        this.data = data;
    }

    public String getCommand() {
        return command;
    }

    public String getData() {
        return data;
    }

    public boolean isValid() {
        return Optional.ofNullable(command)
                .filter(s -> !s.isEmpty())
                .isPresent();
    }
}
