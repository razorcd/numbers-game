package com.challenge.server.controller.dto;

import com.challenge.game.domain.GameRoundResult;
import com.challenge.game.model.Player;

public class ServerResponseDto {

    private GameRoundResult lastGameRoundResult;
    private Player currentPlayer;

    public ServerResponseDto() {
    }

    public ServerResponseDto(GameRoundResult lastGameRoundResult, Player currentPlayer) {
        this.lastGameRoundResult = lastGameRoundResult;
        this.currentPlayer = currentPlayer;
    }

    public GameRoundResult getLastGameRoundResult() {
        return lastGameRoundResult;
    }

    public void setLastGameRoundResult(GameRoundResult lastGameRoundResult) {
        this.lastGameRoundResult = lastGameRoundResult;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
