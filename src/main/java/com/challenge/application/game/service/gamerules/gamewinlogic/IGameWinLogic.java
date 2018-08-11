package com.challenge.application.game.service.gamerules.gamewinlogic;

import com.challenge.application.game.domain.OutputNumber;

import java.util.function.Function;

@FunctionalInterface
public interface IGameWinLogic extends Function<OutputNumber, Boolean> {
}
