package com.challenge.application.game.service.algorithm;

import com.challenge.application.game.domain.OutputNumber;

import java.util.function.Function;

@FunctionalInterface
public interface IWinLogic extends Function<OutputNumber, Boolean> {
}
