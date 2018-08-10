package com.challenge.game.service.algorithm;

import com.challenge.game.domain.InputNumber;
import com.challenge.game.domain.OutputNumber;

import java.util.function.Function;

@FunctionalInterface
public interface IGameAlgorithm extends Function<InputNumber, OutputNumber> {
}
