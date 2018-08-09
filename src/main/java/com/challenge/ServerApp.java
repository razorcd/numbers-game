package com.challenge;

import com.challenge.game.Game;
import com.challenge.game.domain.GameRoundResult;
import com.challenge.game.domain.PlayerAggregate;
import com.challenge.game.model.Player;
import com.challenge.server.Messenger;
import com.challenge.server.Server;
import com.challenge.utils.PropertiesConfigLoader;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Random;

public class ServerApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerApp.class);

    //TODO: add glogal exception handling
    //TODO: add global aplication state configuration singleton.
    //TODO: implement command pattern

    public static void main(String[] args) {
        String log4jConfPath = "./src/main/resources/log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);

        PropertiesConfigLoader.initialize("application.properties");

        LOGGER.info("Starting application.");
        ServerAppState serverAppState = ServerAppState.getInstance();

        Server server = new Server();
        Messenger<String, String> messenger = server.start(9999);

        messenger.onMessageSubscribe(incomingMessage -> {
            String[] parsedIncommingMessage = null;
            String command = null;
            String data = null;

            try {
                parsedIncommingMessage = incomingMessage.split(":");
                command = parsedIncommingMessage[0];
                try {
                    data = parsedIncommingMessage[1];
                } catch(Exception e) {}
            } catch (Exception e) {
                messenger.send("Error parsing command " + incomingMessage + ". " + e.getMessage());
            }

            switch (command) {
                case "ADD_NEW_PLAYER":
                    serverAppState.addPlayer(data);
                    messenger.send("Added player " + data + " to game.");
                    break;
                case "START":
                    try {
                        serverAppState.startGame();
                    } catch (IllegalArgumentException e) {

                        String startErrMessage = new StringBuilder()
                                .append("ERROR: ")
                                .append(e.getMessage())
                                .toString();
                        messenger.send(startErrMessage);
                        break;
                    }

                    Game beforePlayStartGame = serverAppState.getGame();
                    Player currentPlayer = beforePlayStartGame.getPlayers().getRootPlayer();

                    String randomInput = PropertiesConfigLoader.getProperties().getProperty("com.challenge.random_start_input");
                    int randomNumber = Optional.ofNullable((randomInput))
                            .map(Integer::parseInt)
                            .orElse(new Random().nextInt(150) + 10);

                    serverAppState.play(randomNumber);

                    Game afterStartGame = serverAppState.getGame();
                    GameRoundResult roundResult = afterStartGame.getGameRoundResult();

                    String startMessage = new StringBuilder()
                            .append(currentPlayer)
                            .append(" started game and played a random number ")
                            .append(randomNumber)
                            .append(". The result is ")
                            .append(roundResult)
                            .toString();
                    messenger.send(startMessage);
                    break;
                case "PLAY":
                    Game beforePlayingGame = serverAppState.getGame();
                    PlayerAggregate currentPlayingPlayers = beforePlayingGame .getPlayers();

                    try {
                        serverAppState.play(Integer.parseInt(data));
                    } catch (NumberFormatException | IllegalStateException e) {

                        String errMessage = new StringBuilder()
                                .append("ERROR: ")
                                .append(currentPlayingPlayers.getRootPlayer())
                                .append(": ")
                                .append(e.getMessage())
                                .toString();
                        messenger.send(errMessage);
                        break;
                    }

                    Game afterPlayingGame = serverAppState.getGame();
                    GameRoundResult playingRoundResult = afterPlayingGame.getGameRoundResult();

                    String message = new StringBuilder()
                        .append(currentPlayingPlayers.getRootPlayer())
                        .append(" played number ")
                        .append(data)
                        .append(". The result is ")
                        .append(playingRoundResult)
                        .toString();
                    messenger.send(message);
                    break;
                case "STATE":
                    Game currentGame = serverAppState.getGame();

                    PlayerAggregate players = currentGame.getPlayers();
                    GameRoundResult currentRoundResult = currentGame.getGameRoundResult();
                    String currentStateMessage = new StringBuilder()
                            .append(players.getRootPlayer())
                            .append(" is next.")
                            .append(" Last ")
                            .append(currentRoundResult)
                            .toString();
                    messenger.send(currentStateMessage);
                    break;
                case "EXIT":
                    try {
                        messenger.send("server shutdown");
                        server.stop();
                    } catch(Exception e) {}
                    break;
                default:
                    messenger.send("unknown command. Available commands are: ADD_NEW_PLAYER:player_name, START, PLAY:number, STATE, EXIT.");
            }
        });

        messenger.send("connected");
        messenger.startListening(); //blocks current thread

        server.stop();

        LOGGER.info("Application closed.");
    }

    @Override
    protected void finalize() throws Throwable {
        LOGGER.info("GC !!!!!!!!!!!!!!!!!!!!!!!!");
    }
}

