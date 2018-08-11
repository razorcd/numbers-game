package com.challenge.application.game.service.gamerules.validator;

import com.challenge.application.game.domain.InputNumber;
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

    private DivideByThreeInputValidator divideByThreeInputValidator;

    private InputNumber inputNumberMock;

    @Before
    public void setUp() throws Exception {
        divideByThreeInputValidator = new DivideByThreeInputValidator();
        inputNumberMock = mock(InputNumber.class);
        when(inputNumberMock.toString()).thenReturn("42");
    }

    @Test
    public void shouldInvalidateBelowLowBoundaryInputNumber() {
        when(inputNumberMock.isBiggerOrEqualThan(2)).thenReturn(false);

        boolean result = divideByThreeInputValidator.validate(inputNumberMock);

        assertFalse("Should invalidate below low boundary input number.", result);
        assertThat("Should set messages when invalidated below low boundary number.",
                divideByThreeInputValidator.getValidationMessages(),
                Matchers.containsInAnyOrder("can not play game round, invalid input number 42"));
    }

    @Test
    public void shouldValidateGoodInputNumber() {
        when(inputNumberMock.isBiggerOrEqualThan(2)).thenReturn(true);

        boolean result = divideByThreeInputValidator.validate(inputNumberMock);

        assertTrue("Should validate good input number.", result);
        assertThat("Should not set messages when validated good input number.",
                divideByThreeInputValidator.getValidationMessages(), empty());
    }

    @Test(expected = GameRoundException.class)
    public void shouldThrowWhenValidatingBelowLowBoundaryInputNumber() {
        when(inputNumberMock.isBiggerOrEqualThan(2)).thenReturn(false);

        divideByThreeInputValidator.validateOrThrow(inputNumberMock);

        fail("Should have thrown ValidationException.");
    }
}