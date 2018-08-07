package com.challenge.game.service.algorithm;

import com.challenge.game.service.domain.OutputNumber;

import java.util.function.Function;

@FunctionalInterface
public interface WinLogic extends Function<OutputNumber, Boolean> {
}