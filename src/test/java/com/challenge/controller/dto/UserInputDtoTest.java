package com.challenge.controller.dto;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserInputDtoTest {

    @Test
    public void isValidWhenNull() {
        UserInputDto userInputDto = new UserInputDto(null, null);

        assertFalse("Should not be valid with null command.", userInputDto.isValid());
    }

    @Test
    public void isValidWhenEmptyString() {
        UserInputDto userInputDto = new UserInputDto("", null);

        assertFalse("Should not be valid with empty string command.", userInputDto.isValid());
    }
}