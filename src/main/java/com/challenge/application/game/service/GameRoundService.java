package com.challenge.application.game.service;

import com.challenge.application.game.domain.GameRoundResult;
import com.challenge.application.game.domain.InputNumber;
import com.challenge.application.game.domain.OutputNumber;
import com.challenge.application.game.exception.GameRoundException;
import com.challenge.application.game.service.gamerules.gameround.IGameRoundRule;
import com.challenge.application.game.service.gamerules.gamewinlogic.IGameWinLogic;

public class GameRoundService {
    private final IGameRoundRule gameRoundRule;
    private final IGameWinLogic winLogic;

    /**
     * Initialize game round.
     *
     * @param gameRoundRule the algorithm that will generate the game output of the played round.
     * @param winLogic the logic that determines if played round is a win.
     */
    public GameRoundService(final IGameRoundRule gameRoundRule, final IGameWinLogic winLogic) {
        this.gameRoundRule = gameRoundRule;
        this.winLogic = winLogic;
    }

    /**
     * Play one round based on an input value.
     *
     * @return [GameRoundResult] the result of the played round.
     * @throws GameRoundException if invalid input number.
     */
    public GameRoundResult play(final InputNumber inputNumber) {
        OutputNumber outputNumber = gameRoundRule.apply(inputNumber);
        boolean winner = winLogic.apply(outputNumber);

        return new GameRoundResult(outputNumber, winner);
    }
}
