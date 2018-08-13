package com.challenge.application.game.gameround.gamerules.validator;

import com.challenge.application.game.domain.GameRoundInput;
import com.challenge.application.game.exception.GameRoundException;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class DivideByThreeInputValidatorTest {

    private DivideByThreeValidator divideByThreeInputValidator;

    private GameRoundInput gameRoundInputMock;

    @Before
    public void setUp() throws Exception {
        divideByThreeInputValidator = new DivideByThreeValidator();
        gameRoundInputMock = mock(GameRoundInput.class);
    }

    @Test
    public void shouldInvalidateBelowLowBoundaryGameRoundInput() {
        when(gameRoundInputMock.getValue()).thenReturn(1);
        when(gameRoundInputMock.isBiggerOrEqualThan(2)).thenReturn(false);
        when(gameRoundInputMock.toString()).thenReturn("1");

        boolean result = divideByThreeInputValidator.validate(gameRoundInputMock);

        assertFalse("Should invalidate below low boundary input.", result);
        assertThat("Should set messages when invalidated below low boundary input.",
                divideByThreeInputValidator.getValidationMessages(),
                Matchers.containsInAnyOrder("could not play this round because of invalid input 1"));
    }

    @Test
    public void shouldInvalidateNotDivisibleByThreeGameRoundInput() {
        when(gameRoundInputMock.getValue()).thenReturn(7);
        when(gameRoundInputMock.isBiggerOrEqualThan(2)).thenReturn(true);
        when(gameRoundInputMock.toString()).thenReturn("7");


        boolean result = divideByThreeInputValidator.validate(gameRoundInputMock);

        assertFalse("Should invalidate not divisible by 3 input.", result);
        assertThat("Should set messages when invalid game input number.",
                divideByThreeInputValidator.getValidationMessages(),
                Matchers.containsInAnyOrder("could not play this round because of invalid input 7"));
    }

    @Test
    public void shouldValidateGoodInputNumber() {
        when(gameRoundInputMock.getValue()).thenReturn(9);
        when(gameRoundInputMock.isBiggerOrEqualThan(2)).thenReturn(true);
        when(gameRoundInputMock.toString()).thenReturn("9");

        boolean result = divideByThreeInputValidator.validate(gameRoundInputMock);

        assertTrue("Should validate good input number.", result);
        assertThat("Should not set messages when validated good input number.",
                divideByThreeInputValidator.getValidationMessages(), empty());
    }

    @Test(expected = GameRoundException.class)
    public void shouldThrowWhenValidatingBelowLowBoundaryGameRoundInput() {
        when(gameRoundInputMock.getValue()).thenReturn(9);
        when(gameRoundInputMock.isBiggerOrEqualThan(2)).thenReturn(false);

        divideByThreeInputValidator.validateOrThrow(gameRoundInputMock);

        fail("Should have thrown ValidationException.");
    }

    @Test(expected = GameRoundException.class)
    public void shouldThrowWhenValidatingNotDivisibleByThreeGameRoundInput() {
        when(gameRoundInputMock.getValue()).thenReturn(7);
        when(gameRoundInputMock.isBiggerOrEqualThan(2)).thenReturn(true);

        divideByThreeInputValidator.validateOrThrow(gameRoundInputMock);

        fail("Should have thrown ValidationException.");
    }
}