package com.challenge.game;

import java.util.InputMismatchException;

public class Game {
    private InputNumber inputNumber;

    public Game(InputNumber inputNumber) {
        if (!inputNumber.isValid()) throw new InputMismatchException("input is invalid");
        this.inputNumber = inputNumber;
    }

}
