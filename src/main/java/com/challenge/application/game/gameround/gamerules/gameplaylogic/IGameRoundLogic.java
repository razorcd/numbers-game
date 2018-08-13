package com.challenge.application.game.gameround.gamerules.gameplaylogic;

import com.challenge.application.game.domain.GameRoundInput;
import com.challenge.application.game.domain.OutputNumber;
import com.challenge.application.game.gameround.gamerules.validator.Validatable;

import java.util.function.Function;

/**
 * Interface to implement to define main game round logic.
 */
public interface IGameRoundLogic extends Function<GameRoundInput, OutputNumber>, Validatable<GameRoundInput> {
}
