package com.challenge.game.service;

import com.challenge.game.domain.GameRoundResult;
import com.challenge.game.domain.InputNumber;
import com.challenge.game.domain.OutputNumber;
import com.challenge.game.exception.GameRoundException;
import com.challenge.game.service.algorithm.IGameAlgorithm;
import com.challenge.game.service.algorithm.IWinLogic;

public class GameRoundService {
    private final IGameAlgorithm gameAlgorithm;
    private final IWinLogic winLogic;

    /**
     * Initialize game round.
     *
     * @param gameAlgorithm the algorithm that will generate the game output of the played round.
     * @param winLogic the logic that determines if played round is a win.
     */
    public GameRoundService(final IGameAlgorithm gameAlgorithm, final IWinLogic winLogic) {
        this.gameAlgorithm = gameAlgorithm;
        this.winLogic = winLogic;
    }

    /**
     * Play one round based on an input value.
     *
     * @return [GameRoundResult] the result of the played round.
     * @throws GameRoundException if invalid input number.
     */
    public GameRoundResult play(final InputNumber inputNumber) {
        OutputNumber outputNumber = gameAlgorithm.apply(inputNumber);
        boolean winner = winLogic.apply(outputNumber);

        return new GameRoundResult(outputNumber, winner);
    }
}
