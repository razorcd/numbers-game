package com.challenge.game.algorithm;

import com.challenge.game.domain.OutputNumber;

import java.util.function.Function;

@FunctionalInterface
public interface WinLogic extends Function<OutputNumber, Boolean> {
}
