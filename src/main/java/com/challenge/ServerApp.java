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

    //TODO: add global exception handling
    //TODO: add global application state configuration singleton.

    public static void main(String[] args) {
        PropertyConfigurator.configure(PropertyConfigurator.class.getClassLoader().getResourceAsStream("log4j.properties"));
        PropertiesConfigLoader.initialize("application.properties");

        LOGGER.info("Starting application.");
        ServerAppState serverAppState = new ServerAppState();

        Server server = new Server();
        Messenger<String, String> messenger = server.start(9999);

        CommandController commandController = new CommandController(serverAppState, messenger, new UserInputDeserializer());

        messenger.userInputSubscribe(commandController);

        messenger.send("connected");
        messenger.startListening(); //blocks current thread

        server.stop();

        LOGGER.info("Application closed.");
    }
}

