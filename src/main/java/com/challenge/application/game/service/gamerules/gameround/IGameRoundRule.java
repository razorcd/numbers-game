package com.challenge.application.game.service.gamerules.gameround;

import com.challenge.application.game.domain.InputNumber;
import com.challenge.application.game.domain.OutputNumber;

import java.util.function.Function;

@FunctionalInterface
public interface IGameRoundRule extends Function<InputNumber, OutputNumber> {
}
