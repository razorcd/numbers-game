package com.challenge.game.service;

import com.challenge.game.service.domain.GameRoundResult;
import com.challenge.game.service.domain.InputNumber;
import com.challenge.game.service.domain.Player;

import java.util.ArrayList;
import java.util.List;

// TODO: return a new instance of the Game after each play
// TODO: implement state machine to remove all if's and validations
// TODO: implements global exception handling with custom exceptions.

public class Game {

    private final GameRoundService gameRoundService;

    private List<Player> players = new ArrayList<>();
    private int currentPlayerIndex;

    private GameState currentState;
    private GameRoundResult lastRoundResult;

    public Game(GameRoundService gameRoundService) {
        this.gameRoundService = gameRoundService;
        this.currentState = GameState.IDLE;
    }

    public void addPlayer(Player player) {
        if (currentState == GameState.IDLE) {
            this.players.add(player);
        } else {
            throw new RuntimeException("Can't add player only in IDLE currentState.");
        }
    }

    public void start() {
        if (currentState != GameState.IDLE) throw new RuntimeException("Can't start game only from IDLE state.");
        if (players.size() != 2) throw new RuntimeException("Must have 2 players to play a game.");
        currentPlayerIndex = 0;
        this.currentState = GameState.PLAYING;
    }

    public void play(final InputNumber inputNumber) {
        if (lastRoundResult != null && !inputNumber.hasSameValue(lastRoundResult.getOutputNumber()))
            throw new IllegalArgumentException("Input value must be the output of last round.");

        if (currentState != GameState.PLAYING) throw new RuntimeException("Must be in PLAYING currentState to be able to play.");

        lastRoundResult = gameRoundService.play(inputNumber);

        if (lastRoundResult.isWinner()) {
            currentState = GameState.FINISHED;
        } else {
            currentPlayerIndex = (currentPlayerIndex + 1) % 2;
        }
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public GameRoundResult getLastRoundResult() {
        return lastRoundResult;
    }
}
