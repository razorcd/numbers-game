package com.challenge.controller.mapper;

import com.challenge.controller.dto.UserInputDto;

import java.util.function.Function;

public class UserInputDeserializer implements Function<String, UserInputDto> {

    /**
     * Raw user input to UserInputDto deserializer.
     *
     * @param rawUserInput raw user input.
     * @return [UserInputDto] parsed user input dto.
     */
    @Override
    public UserInputDto apply(String rawUserInput) {
        String[] rawUserInputArray = rawUserInput.split(":", 2);
        String command = rawUserInputArray[0];
        String data = rawUserInputArray.length > 1 ? rawUserInputArray[1] : "";

        return new UserInputDto(command, data);
    }
}
