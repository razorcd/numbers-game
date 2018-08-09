package com.challenge.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    /**
     * Start current client on specified port.
     * @param ip servers ip
     * @param port servers port
     * @return {@code [Messenger<String, String>]} messenger to handle in/out communication
     */
    public Messenger start(String ip, int port) {
        LOGGER.info("Connecting to ip: {}, port: {}.", ip, port);
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            LOGGER.error("IOException while starting client.");
            stop();
        }

        return new Messenger(in, out);
    }

    /**
     * Stop current server.
     */
    public void stop() {
        LOGGER.info("Stopping client.");
        try {
            in.close();
            out.close();
            clientSocket.close();
            LOGGER.debug("Client stopped.");
        } catch (IOException ex) {
            throw new RuntimeException("IO exception while closing connections.", ex);
        }
    }
}


