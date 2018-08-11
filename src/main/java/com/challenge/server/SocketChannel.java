package com.challenge.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.stream.Stream;

public class SocketChannel {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketChannel.class);

    private BufferedReader in;
    private PrintWriter out;

    /**
     * SocketChannel to handle in/out communication.
     *
     * @param in the input stream
     * @param out the output writer
     */
    public SocketChannel(BufferedReader in, PrintWriter out) {
        this.in = in;
        this.out = out;
    }

    /**
     * Send message to output.
     *
     * @param message the message to sent.
     */
    synchronized public void send(String message) {
        LOGGER.debug("Sending message: {}", message);
        out.println(message);
    }

    /**
     * Start listening on incoming messages.
     */
    public Stream<String> getInputStream() {
        return getValidInputStream()
            .peek(msg -> LOGGER.info("Received message: {}", msg));
    }

    /**
     * Start listening only on first message.
     * Will block thread until a message is received.
     *
     * @return the new incoming message.
     */
    public String readNextLineSync() {
        return getValidInputStream()
                .peek(msg -> LOGGER.info("Received message: {}", msg))
                .findFirst()
                .orElse("stream closed");
    }

    private Stream<String> getValidInputStream() {
        return in.lines()
                .filter(Objects::nonNull)
                .filter(s -> !s.isEmpty());
    }
}
