package com.challenge;

import com.challenge.controller.CommandController;
import com.challenge.controller.mapper.UserInputDeserializer;
import com.challenge.server.Messenger;
import com.challenge.server.Server;
import com.challenge.utils.PropertiesConfigLoader;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerApp.class);

    public static void main(String[] args) {
        LOGGER.info("Starting application.");

        initializeGlobalConfiguration();

        //setup server
        String port = PropertiesConfigLoader.getProperties().getProperty("com.challenge.server.port", "9999");
        Server server = new Server();
        Messenger messenger = server.start(Integer.parseInt(port));

        //setup application
        GameContext gameContext = new GameContext();
        CommandController commandController = new CommandController(gameContext, messenger, new UserInputDeserializer());

        //listen on input stream
        messenger.send("connected");
        messenger.getInputStream()
                .peek(commandController)
                .filter(msg -> msg.equals("EXIT"))
                .findAny();

        server.stop();
        LOGGER.info("Application closed.");
    }

    /**
     * Initialize global configurations.
     */
    private static void initializeGlobalConfiguration() {
        PropertyConfigurator.configure(PropertyConfigurator.class.getClassLoader().getResourceAsStream("log4j.properties"));
        PropertiesConfigLoader.initialize("application.properties");
    }
}

