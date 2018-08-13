package com.challenge.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerStream implements Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerStream.class);

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    /**
     * Create a new server stream trough specified socket server.
     *
     * @param serverSocket the socket server to connect to.
     */
    public ServerStream(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    /**
     * Open a new channel for communicating on main server socket stream.
     *
     * @return {@code [SocketChannel<String, String>]} socket channel to handle in/out connections.
     */
    public SocketChannel start() {
        LOGGER.debug("Starting ServerStream.");
        try {
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            LOGGER.debug("ServerStream started.");
        } catch (IOException e) {
            LOGGER.error("IO exception while server is running.", e);
            close();
        }
        return new SocketChannel(in, out);
    }

    /**
     * Close server stream.
     */
    public void close() {
        LOGGER.info("Stopping server.");
        try {
            in.close();
            out.close();
            clientSocket.close();
            LOGGER.debug("ServerStream stopped.");
        } catch (IOException ex) {
            throw new RuntimeException("IO exception while server is stopping.", ex);
        }
    }
}


