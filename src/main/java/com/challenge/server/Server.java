package com.challenge.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    /**
     * Start server on specified port.
     *
     * @param port server port
     * @return {@code [Messenger<String, String>]} messenger to handle in/out connections.
     */
    public Messenger start(int port) {
        LOGGER.debug("Starting server.");
        try {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            LOGGER.debug("Server started.");
        } catch (IOException e) {
            LOGGER.error("IO exception while server is running.", e);
            stop();
        }
        return new Messenger(in, out);
    }

    /**
     * Stop server.
     */
    public void stop() {
        LOGGER.info("Stopping server.");
        try {
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
            LOGGER.debug("Server stopped.");
        } catch (IOException ex) {
            throw new RuntimeException("IO exception while server is stopping.", ex);
        }
    }
}


