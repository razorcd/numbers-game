package com.challenge.game.service.algorithm;

import com.challenge.game.service.domain.InputNumber;
import com.challenge.game.service.domain.OutputNumber;

import java.util.function.Function;

@FunctionalInterface
public interface GameAlgorithm extends Function<InputNumber, OutputNumber> {
}
