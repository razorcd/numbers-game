package com.challenge.controller;

import com.challenge.ServerAppState;
import com.challenge.controller.commands.*;
import com.challenge.controller.dto.UserInputDto;
import com.challenge.controller.mapper.UserInputDeserializer;
import com.challenge.server.Messenger;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class CommandController implements Consumer<String> {

    private ServerAppState serverAppState;
    private Messenger<String, String> messenger;
    private UserInputDeserializer userInputDeserializer;

    /**
     * Controller to handle user input commands.
     *
     * @param serverAppState holds the state of the application.
     * @param messenger socket adapter.
     */
    public CommandController(ServerAppState serverAppState,
                             Messenger<String, String> messenger,
                             UserInputDeserializer userInputDeserializer) {
        this.serverAppState = serverAppState;
        this.messenger = messenger;
        this.userInputDeserializer = userInputDeserializer;
    }

    /**
     * Execute user input command.
     *
     * @param rawUserInput raw user input.
     */
    @Override
    public void accept(String rawUserInput) {
        Stream.of(rawUserInput)
                .map(userInputDeserializer)
                .filter(UserInputDto::isValid)
                .forEach(this::runCommand);
    }

    private void runCommand(UserInputDto userInputDto) {
        switch (userInputDto.getCommand()) {
            case "ADD_NEW_PLAYER":
                new AddNewPlayer(serverAppState, messenger).execute(userInputDto.getData());
                break;
            case "START":
                new Start(serverAppState, messenger).execute(userInputDto.getData());
                break;
            case "PLAY":
                new Play(serverAppState, messenger).execute(userInputDto.getData());
                break;
            case "STATE":
                new State(serverAppState, messenger).execute(userInputDto.getData());
                break;
            case "EXIT":
                new Exit(serverAppState, messenger).execute(userInputDto.getData());
                break;
            default:
                new Unknown(serverAppState, messenger).execute(userInputDto.getData());
        }
    }
}
