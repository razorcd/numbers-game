package com.challenge.application.game.service.algorithm;

import com.challenge.application.game.domain.InputNumber;
import com.challenge.application.game.domain.OutputNumber;

import java.util.function.Function;

@FunctionalInterface
public interface IGameAlgorithm extends Function<InputNumber, OutputNumber> {
}
