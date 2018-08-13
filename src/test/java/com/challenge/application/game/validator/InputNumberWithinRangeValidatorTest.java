package com.challenge.application.game.validator;

import com.challenge.application.game.Game;
import com.challenge.application.game.domain.InputNumber;
import com.challenge.application.game.exception.ValidationException;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class InputNumberWithinRangeValidatorTest {

    private InputNumberWithinRangeValidator canPlayInputNumberGameValidator;
    private InputNumber inputNumber;
    private Game game;

    @Before
    public void setUp() throws Exception {
        inputNumber = mock(InputNumber.class);
        game = mock(Game.class);
    }

    @Test
    public void shouldInvalidateWhenInputNumberNotWithinRange() {
        canPlayInputNumberGameValidator = new InputNumberWithinRangeValidator(new InputNumber(-100));
        boolean result = canPlayInputNumberGameValidator.validate(game);

        assertFalse("Should invalidate input number when number not within range.", result);
        assertThat("Should set messages when number when number not within range.",
                canPlayInputNumberGameValidator.getValidationMessages(),
                containsInAnyOrder("can not play game because -100 is not within [-1, 0, 1]"));
    }

    @Test
    public void shouldValidateWhenInputNumberIsWithinRange() {
        Stream.of(-1,0,1).forEach(i -> {
            canPlayInputNumberGameValidator = new InputNumberWithinRangeValidator(new InputNumber(i));
            boolean result = canPlayInputNumberGameValidator.validate(game);

            assertTrue("Should validate input number when number is within range.", result);
            assertThat("Should set messages when number is within range.",
                    canPlayInputNumberGameValidator.getValidationMessages(), empty());
        });

    }

    @Test(expected = ValidationException.class)
    public void shouldThrowWhenInputNumberNotWithinRange() {
        canPlayInputNumberGameValidator = new InputNumberWithinRangeValidator(new InputNumber(345));

        canPlayInputNumberGameValidator.validateOrThrow(game);

        fail("Should have thrown ValidationException.");
    }
}