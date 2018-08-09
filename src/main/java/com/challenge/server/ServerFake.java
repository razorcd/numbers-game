package com.challenge.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerFake {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerFake.class);

    private ServerSocket serverSocket;

    public ServerSocket start(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            LOGGER.error("IOException while starting server.");
        } finally {
            stop();
        }

        return serverSocket;
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("IO exception while closing server connection.", e);
        }
    }
}


