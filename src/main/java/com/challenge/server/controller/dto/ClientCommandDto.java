package com.challenge.server.controller.dto;

import com.challenge.game.domain.InputNumber;

public class ClientCommandDto {

    private String command;
    private String playerName;
    private String authToken;
    private InputNumber inputNumber;


    public ClientCommandDto() {
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public InputNumber getInputNumber() {
        return inputNumber;
    }

    public void setInputNumber(InputNumber inputNumber) {
        this.inputNumber = inputNumber;
    }
}
