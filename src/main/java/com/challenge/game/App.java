package com.challenge.game;

import com.challenge.game.service.GameRoundService;
import com.challenge.game.service.algorithm.DivideByThree;
import com.challenge.game.service.algorithm.GameAlgorithm;
import com.challenge.game.service.algorithm.WinLogic;
import com.challenge.game.service.algorithm.WinWhenOne;

import java.io.IOException;

public class App {
    public static void main( String[] args ) throws IOException {

        GameAlgorithm gameAlgorithm = new DivideByThree();
        WinLogic winLogic = new WinWhenOne();
        GameRoundService gameRoundService = new GameRoundService(gameAlgorithm, winLogic);
    }
}

