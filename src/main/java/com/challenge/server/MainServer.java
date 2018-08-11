package com.challenge.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;

public class MainServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainServer.class);

    private int port;
    private ServerSocket serverSocket;

    /**
     * Main server socket on specified port.
     *
     * @param port the port of the socket.
     */
    public MainServer(int port) {
        this.port = port;
    }

    /**
     * Open main server socket.
     *
     * @return [ServerSocket] the socket of the started server
     */
    public ServerSocket start() {
        try {
            serverSocket = new ServerSocket(port);
            return serverSocket;
        } catch (IOException e) {
            LOGGER.error("IO exception while opening main server socket.");
            throw new RuntimeException(e);
        }
    }

    /**
     * Close main server socket.
     */
    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            LOGGER.error("IO exception while closing main server socket.");
            throw new RuntimeException(e);
        }

    }
}
