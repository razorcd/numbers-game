package com.challenge.controller;

import com.challenge.GameContext;
import com.challenge.controller.commands.*;
import com.challenge.controller.dto.UserInputDto;
import com.challenge.controller.mapper.UserInputDeserializer;
import com.challenge.server.SocketChannel;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class CommandController implements Consumer<String> {

    private GameContext gameContext;
    private SocketChannel socketChannel;
    private UserInputDeserializer userInputDeserializer;

    /**
     * Controller to handle user input commands.
     *
     * @param gameContext holds the state of the application.
     * @param socketChannel socket adapter.
     */
    public CommandController(GameContext gameContext,
                             SocketChannel socketChannel,
                             UserInputDeserializer userInputDeserializer) {
        this.gameContext = gameContext;
        this.socketChannel = socketChannel;
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
        CommandType commandType = CommandType.valueOfString(userInputDto.getCommand());

        switch (commandType) {
            case ADD_PLAYER:
                new AddPlayer(gameContext, socketChannel).execute(userInputDto.getData());
                break;
            case START:
                new Start(gameContext, socketChannel).execute(userInputDto.getData());
                break;
            case PLAY:
                new Play(gameContext, socketChannel).execute(userInputDto.getData());
                break;
            case STATE:
                new State(gameContext, socketChannel).execute(userInputDto.getData());
                break;
            case EXIT:
                new Exit(gameContext, socketChannel).execute(userInputDto.getData());
                break;
            default:
                new Unknown(gameContext, socketChannel).execute(userInputDto.getData());
        }
    }
}
