package com.challenge.application.gameofthree.gameround;

import com.challenge.application.gameofthree.gameround.domain.GameRoundInput;
import com.challenge.application.gameofthree.gameround.domain.GameRoundResult;
import com.challenge.application.gameofthree.game.domain.InputNumber;
import com.challenge.application.gameofthree.game.domain.OutputNumber;
import com.challenge.application.gameofthree.exception.GameRoundException;
import com.challenge.application.gameofthree.gameround.gamerules.gameplaylogic.DivideByThreeLogic;
import com.challenge.application.gameofthree.gameround.gamerules.gamewinlogic.WinWhenOne;
import com.challenge.application.gameofthree.gameround.gamerules.gameplaylogic.validator.DivideByThreeValidator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameRoundServiceTest {

    private GameRoundService gameRoundService;

    @Before
    public void setUp() throws Exception {
        gameRoundService = new GameRoundService(new DivideByThreeLogic(), new WinWhenOne());
    }

    @Test
    public void playGameWithValidInputShouldReturnValidOutput() {
        GameRoundInput gameRoundInput = new GameRoundInput(new InputNumber(-1), new OutputNumber(10));

        GameRoundResult gameRoundResult = gameRoundService.play(gameRoundInput);

        assertEquals("GameRoundService with round input -1 and 10 should return 3 as output.", new OutputNumber(3), gameRoundResult.getOutputNumber());
    }

    @Test
    public void playGameWithValidInputShouldReturnValidOutput2() {
        GameRoundInput gameRoundInput = new GameRoundInput(new InputNumber(0), new OutputNumber(9));

        GameRoundResult gameRoundResult = gameRoundService.play(gameRoundInput);

        assertEquals("GameRoundService with round input 0 and 9 should return 3 as output.", new OutputNumber(3), gameRoundResult.getOutputNumber());
    }

    @Test
    public void playGameWithValidInputShouldReturnValidOutput3() {
        GameRoundInput gameRoundInput = new GameRoundInput(new InputNumber(1), new OutputNumber(8));

        GameRoundResult gameRoundResult = gameRoundService.play(gameRoundInput);

        assertEquals("GameRoundService with round input 1 and 8 should return 3 as output.", new OutputNumber(3), gameRoundResult.getOutputNumber());
    }

    @Test
    public void playGameWithValidInputShouldReturnValidOutput4() {
        GameRoundInput gameRoundInput = new GameRoundInput(new InputNumber(2), new OutputNumber(7));

        GameRoundResult gameRoundResult = gameRoundService.play(gameRoundInput);

        assertEquals("GameRoundService with round input 2 and 7 should return 3 as output.", new OutputNumber(3), gameRoundResult.getOutputNumber());
    }

    @Test
    public void playGameWithValidInputShouldReturnValidOutput5() {
        GameRoundInput gameRoundInput = new GameRoundInput(new InputNumber(25), new OutputNumber(65));

        GameRoundResult gameRoundResult = gameRoundService.play(gameRoundInput);

        assertEquals("GameRoundService with round input 25 and 65 should return 30 as output.", new OutputNumber(30), gameRoundResult.getOutputNumber());
    }


    @Test
    public void playGameWith1and2AsInputShouldWinGame() {
        GameRoundInput gameRoundInput = new GameRoundInput(new InputNumber(1), new OutputNumber(2));

        GameRoundResult gameRoundResult = gameRoundService.play(gameRoundInput);

        assertTrue("GameRoundService with round input 1 and 2 should win the gameRoundService.", gameRoundResult.isWinner());
        assertEquals("GameRoundService with winning input value should return output value as 1", new OutputNumber(1), gameRoundResult.getOutputNumber());
    }

    @Test
    public void playGameWith0and3AsInputShouldWinGame() {
        GameRoundInput gameRoundInput = new GameRoundInput(new InputNumber(0), new OutputNumber(3));

        GameRoundResult gameRoundResult = gameRoundService.play(gameRoundInput);

        assertTrue("GameRoundService with round input 0 and 3 should win the gameRoundService.", gameRoundResult.isWinner());
        assertEquals("GameRoundService with winning input value should return output value as 1", new OutputNumber(1), gameRoundResult.getOutputNumber());
    }

    @Test
    public void playGameWithNegative1and4AsInputShouldWinGame() {
        GameRoundInput gameRoundInput = new GameRoundInput(new InputNumber(-1), new OutputNumber(4));

        GameRoundResult gameRoundResult = gameRoundService.play(gameRoundInput);

        assertTrue("GameRoundService with round input -1 and 4 should win the gameRoundService.", gameRoundResult.isWinner());
        assertEquals("GameRoundService with winning input value should return output value as 1", new OutputNumber(1), gameRoundResult.getOutputNumber());
    }

    @Test
    public void playGameWith1and5AsInputShouldNotWinGame() {
        GameRoundInput gameRoundInput = new GameRoundInput(new InputNumber(1), new OutputNumber(5));

        GameRoundResult gameRoundResult = gameRoundService.play(gameRoundInput);

        assertFalse("GameRoundService with round input 1 and 5 should NOT win the gameRoundService.", gameRoundResult.isWinner());
        assertNotEquals("GameRoundService with NOT winning input value should NOT return output value as 1", new OutputNumber(1), gameRoundResult.getOutputNumber());
    }


    @Test(expected = GameRoundException.class)
    public void gameWithCustomDivideByThreeValidator_whenPlayGameWithANumberBelowLimitShouldThrow() {
        DivideByThreeLogic divideByThree = new DivideByThreeLogic();
        divideByThree.addValidator(new DivideByThreeValidator());
        gameRoundService = new GameRoundService(divideByThree, new WinWhenOne());
        GameRoundInput gameRoundInput = new GameRoundInput(new InputNumber(-1), new OutputNumber(2));

        gameRoundService.play(gameRoundInput);

        fail("Should have thrown exception when checking input validity of game round service.");
    }

    @Test(expected = GameRoundException.class)
    public void gameWithCustomDivideByThreeValidator_whenPlayGameWithANumberNotDividableBy3() {
        DivideByThreeLogic divideByThree = new DivideByThreeLogic();
        divideByThree.addValidator(new DivideByThreeValidator());
        gameRoundService = new GameRoundService(divideByThree, new WinWhenOne());
        GameRoundInput gameRoundInput = new GameRoundInput(new InputNumber(-1), new OutputNumber(8));

        gameRoundService.play(gameRoundInput);

        fail("Should have thrown exception when checking input validity of game round service.");
    }

    @Test
    public void gameWithCustomDivideByThreeValidator_whenPlayGameWithAValidNumber() {
        DivideByThreeLogic divideByThree = new DivideByThreeLogic();
        divideByThree.addValidator(new DivideByThreeValidator());
        gameRoundService = new GameRoundService(divideByThree, new WinWhenOne());
        GameRoundInput gameRoundInput = new GameRoundInput(new InputNumber(-1), new OutputNumber(10));

        gameRoundService.play(gameRoundInput);
    }

}