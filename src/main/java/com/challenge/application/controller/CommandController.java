package com.challenge.application.controller;

import com.challenge.application.controller.commands.*;
import com.challenge.application.controller.dto.UserInputDto;
import com.challenge.application.controller.mapper.UserInputDeserializer;
import com.challenge.application.game.GameManager;
import com.challenge.application.game.gameround.ai.DivideByThreeAi;
import com.challenge.server.SocketChannel;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class CommandController implements Consumer<String> {

    private GameManager gameManager;
    private SocketChannel socketChannel;
    private UserInputDeserializer userInputDeserializer;

    /**
     * Controller to handle user input commands.
     *
     * @param gameManager holds the state of the application.
     * @param socketChannel socket adapter.
     */
    public CommandController(GameManager gameManager,
                             SocketChannel socketChannel,
                             UserInputDeserializer userInputDeserializer) {
        this.gameManager = gameManager;
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
                new AddHuman(gameManager, socketChannel).execute(userInputDto.getData());
                break;
            case ADD_MACHINE:
                new AddMachine(gameManager, socketChannel).execute(userInputDto.getData());
                break;
            case START:
                Start start = new Start(gameManager, socketChannel);
                PlayMachine playMachine = new PlayMachine(gameManager, socketChannel, new DivideByThreeAi());
                start.setSuccessor(playMachine);
                start.execute(userInputDto.getData());
                break;
            case PLAY:
                Play playCommand = new Play(gameManager, socketChannel);
                playCommand.setSuccessor(new PlayMachine(gameManager, socketChannel, new DivideByThreeAi()));
                playCommand.execute(userInputDto.getData());
                break;
            case STATE:
                new State(gameManager, socketChannel).execute(userInputDto.getData());
                break;
            case EXIT:
                new Exit(gameManager, socketChannel).execute(userInputDto.getData());
                break;
            default:
                new Unknown(gameManager, socketChannel).execute(userInputDto.getData());
        }
    }
}
