package com.challenge.application.controller;

import com.challenge.application.controller.commands.*;
import com.challenge.application.controller.dto.UserInputDto;
import com.challenge.application.controller.mapper.UserInputDeserializer;
import com.challenge.application.gameofthree.ai.DivideByThreeAi;
import com.challenge.application.gameofthree.game.GameService;
import com.challenge.server.SocketChannel;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class CommandController implements Consumer<String> {

    private GameService gameService;
    private SocketChannel socketChannel;
    private UserInputDeserializer userInputDeserializer;

    /**
     * Controller to handle user input commands.
     *
     * @param gameService service to interact with running game.
     * @param socketChannel socket adapter.
     */
    public CommandController(GameService gameService,
                             SocketChannel socketChannel,
                             UserInputDeserializer userInputDeserializer) {
        this.gameService = gameService;
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
                new AddHuman(gameService, socketChannel).execute(userInputDto.getData());
                break;
            case ADD_MACHINE:
                new AddMachine(gameService, socketChannel).execute(userInputDto.getData());
                break;
            case START:
                Start start = new Start(gameService, socketChannel);
                PlayMachine playMachine = new PlayMachine(gameService, socketChannel, new DivideByThreeAi());
                start.setSuccessor(playMachine);
                start.execute(userInputDto.getData());
                break;
            case PLAY:
                Play playCommand = new Play(gameService, socketChannel);
                playCommand.setSuccessor(new PlayMachine(gameService, socketChannel, new DivideByThreeAi()));
                playCommand.execute(userInputDto.getData());
                break;
            case STATE:
                new State(gameService, socketChannel).execute(userInputDto.getData());
                break;
            case EXIT:
                new Exit(gameService, socketChannel).execute(userInputDto.getData());
                break;
            default:
                new Unknown(gameService, socketChannel).execute(userInputDto.getData());
        }
    }
}
