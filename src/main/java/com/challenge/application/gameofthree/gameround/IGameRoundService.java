package com.challenge.application.gameofthree.gameround;

import com.challenge.application.gameofthree.exception.GameRoundException;
import com.challenge.application.gameofthree.gameround.domain.GameRoundInput;
import com.challenge.application.gameofthree.gameround.domain.GameRoundResult;

/**
 * Service interface to interact with a game round (a turn).
 */
public interface IGameRoundService {

    /**
     * Play one round based on an input value.
     *
     * @param gameRoundInput the game round input representing user input and last round output.
     * @return [GameRoundResult] the result of the played round.
     * @throws GameRoundException if invalid input number.
     */
    GameRoundResult play(final GameRoundInput gameRoundInput);
}
