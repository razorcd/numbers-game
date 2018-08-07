package com.challenge.game;

import com.challenge.game.algorithm.GameAlgorithm;
import com.challenge.game.algorithm.WinLogic;
import com.challenge.game.domain.GameRoundResult;
import com.challenge.game.domain.InputNumber;
import com.challenge.game.domain.OutputNumber;

import java.util.Optional;
import java.util.stream.Stream;

public class Game {
    private final GameAlgorithm gameAlgorithm;
    private final WinLogic winLogic;

    /**
     * Initialize game.
     *
     * @param gameAlgorithm the algorithm that will generate the game output of the played round.
     * @param winLogic the logic that determines if played round is a win.
     */
    public Game(final GameAlgorithm gameAlgorithm, final WinLogic winLogic) {
        this.gameAlgorithm = gameAlgorithm;
        this.winLogic = winLogic;
    }

    /**
     * Play one round based on an input value.
     *
     * @return [GameRoundResult] the result of the played round.
     */
    public GameRoundResult play(InputNumber inputNumber) {
        OutputNumber outputNumber = Stream.of(inputNumber)
                .filter(InputNumber::isValid)
                .map(gameAlgorithm)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        boolean winner = Optional.of(outputNumber)
                .map(winLogic)
                .orElse(false);

        return new GameRoundResult(outputNumber, winner);
    }
}
