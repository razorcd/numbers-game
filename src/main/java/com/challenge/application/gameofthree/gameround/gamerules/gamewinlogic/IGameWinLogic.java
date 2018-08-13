package com.challenge.application.gameofthree.gameround.gamerules.gamewinlogic;

import com.challenge.application.gameofthree.game.domain.OutputNumber;

import java.util.function.Function;

@FunctionalInterface
public interface IGameWinLogic extends Function<OutputNumber, Boolean> {
}
