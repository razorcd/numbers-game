package com.challenge.game;

import org.junit.Test;

import java.util.InputMismatchException;

import static org.junit.Assert.*;

public class GameTest {

    @Test(expected = InputMismatchException.class)
    public void initialiseGameWithANumberBelowLimitShouldThrow() {
        new Game(new InputNumber(1));
    }

    @Test(expected = InputMismatchException.class)
    public void initialiseGameWithANumberBelow0ShouldThrow() {
        new Game(new InputNumber(-1));
    }
}