package com.challenge.application.game.service;

import com.challenge.application.game.domain.GameRoundResult;
import com.challenge.application.game.domain.InputNumber;
import com.challenge.application.game.domain.OutputNumber;
import com.challenge.application.game.exception.GameRoundException;
import com.challenge.application.game.service.algorithm.IGameAlgorithm;
import com.challenge.application.game.service.algorithm.IWinLogic;

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
