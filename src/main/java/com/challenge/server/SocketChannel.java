package com.challenge.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

public class SocketChannel {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketChannel.class);
    private static final int SEND_WITH_DELAY_VALUE = 75;

    private BufferedReader in;
    private PrintWriter out;
    private Collection<SocketChannel> allActiveSocketChannels = new CopyOnWriteArrayList<>();
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
    public void send(String message) {
        LOGGER.debug("Sending message: {}", message);
        out.println(message);
    }

    /**
     * Send message to output.
     *
     * @param message the message to sent.
     */
    public void sendWithDelay(String message) {
        send(message);
        try {
            Thread.sleep(SEND_WITH_DELAY_VALUE);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Broadcast message to all active socket listeners.
     *
     * @param message the message to broadcast.
     */
    public void broadcast(String message) {
        allActiveSocketChannels
                .forEach((socketChannel -> socketChannel.send(message)));
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

    public Iterator<String> getInputiterator() {
        return getValidInputStream().iterator();
    }

    public boolean inputIsEmpty() {
        try {
            return !in.ready();
        } catch (IOException e) {
            LOGGER.error("IOException while checking if input buffer is ready.");
            throw new RuntimeException(e);
        }
    }

    public void clearInput() {
        try {
            Thread.sleep(100);
            while (!inputIsEmpty()) {
                in.read();
            }
        } catch (IOException | InterruptedException e) {
            LOGGER.error("Exception while reading all characters from input buffer.");
            throw new RuntimeException(e);
        }
    }

    private Stream<String> getValidInputStream() {
        return in.lines()
                .filter(Objects::nonNull)
                .filter(s -> !s.isEmpty());
    }

    public void setActiveSocketChannels(Collection<SocketChannel> allActiveSocketChannels) {
        this.allActiveSocketChannels = allActiveSocketChannels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SocketChannel that = (SocketChannel) o;
        return Objects.equals(in, that.in) &&
                Objects.equals(out, that.out) &&
                Objects.equals(allActiveSocketChannels, that.allActiveSocketChannels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(in, out, allActiveSocketChannels);
    }
}
