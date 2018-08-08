package com.challenge;

import com.challenge.game.domain.PlayerAggregate;
import com.challenge.server.controller.dto.ClientCommandDto;
import com.challenge.server.controller.dto.ServerResponseDto;
import com.challenge.game.Game;
import com.challenge.game.service.GameRoundService;
import com.challenge.game.service.algorithm.DivideByThree;
import com.challenge.game.service.algorithm.GameAlgorithm;
import com.challenge.game.service.algorithm.WinLogic;
import com.challenge.game.service.algorithm.WinWhenOne;
import com.challenge.game.model.Player;
import com.challenge.utils.JsonStringConverter;
import com.challenge.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);


    //TODO: add glogal exception handling
    //TODO: add global aplication state configuration singleton.


    public static void main( String[] args ) throws IOException {
        Server server = new Server();
        Game game = buildGame();
        JsonStringConverter jsonStringConverter = new JsonStringConverter();

        server.start(8888, new Function<String, String>() {
            @Override
            public String apply(String input) {
                ClientCommandDto clientCommandDto = jsonStringConverter.convertJsonStringToData(input, ClientCommandDto.class);

                // TODO: treat as event stream
                switch (clientCommandDto.getCommand()) {
                    case "connect":
//                        game.addPlayer(new Player(clientCommandDto.getPlayerName()));
                        break;
                    case "start":
//                        game.start();
                        LOGGER.info("Game started.");
                        break;
                    case "play":
                        game.play(clientCommandDto.getInputNumber());
                        LOGGER.info("Played input {}", clientCommandDto.getInputNumber());
                        break;
                    default:
                        LOGGER.info("Unknown command.");
                }

                LOGGER.info("Last round result: {}", game.getGameRoundResult());
                LOGGER.info("Current game state: {}", game.getGameRoundResult());
                ServerResponseDto serverResponseDto = new ServerResponseDto(
                        game.getGameRoundResult(),
                        game.getPlayers().getRootPlayer()
                );
                return jsonStringConverter.convertDataToJsonString(Objects.requireNonNull(serverResponseDto));
            }
        });

    }

    private static Game buildGame() {
        GameAlgorithm gameAlgorithm = new DivideByThree();
        WinLogic winLogic = new WinWhenOne();
        GameRoundService gameRoundService = new GameRoundService(gameAlgorithm, winLogic);

        PlayerAggregate playerAggregate = new PlayerAggregate(Arrays.asList(new Player("player1"), new Player("player2")), 0);

        return new Game(gameRoundService, playerAggregate);
    }
}

