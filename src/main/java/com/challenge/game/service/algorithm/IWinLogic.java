package com.challenge.game.service.algorithm;

import com.challenge.game.domain.OutputNumber;

import java.util.function.Function;

@FunctionalInterface
public interface IWinLogic extends Function<OutputNumber, Boolean> {
}
