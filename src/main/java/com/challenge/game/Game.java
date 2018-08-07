package com.challenge.game;

import com.challenge.game.algorithm.DivideByThree;
import com.challenge.game.algorithm.WinWhenOne;
import com.challenge.game.domain.InputNumber;
import com.challenge.game.domain.OutputNumber;
import com.challenge.game.domain.GameRoundResult;

import java.util.stream.Stream;

public class Game {
    private final DivideByThree gameAlgorithm;
    private final WinWhenOne winLogic;

    /**
     * Initialize game.
     *
     * @param gameAlgorithm the algorithm that will generate the game output of the played round.
     * @param winLogic the logic that determines if played round is a win.
     */
    public Game(final DivideByThree gameAlgorithm, final WinWhenOne winLogic) {
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
                .map(gameAlgorithm::calculateOutputNumber)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        boolean winner = winLogic.isWinner(outputNumber);

        return new GameRoundResult(outputNumber, winner);
    }
}
