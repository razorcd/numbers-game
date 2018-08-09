package com.challenge.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class Messenger<I,O> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Messenger.class);

    private List<Consumer<I>> onMessageConsumers = new CopyOnWriteArrayList<>();
    private BufferedReader in;
    private PrintWriter out;

    /**
     * Messenger to handle in/out communication.
     *
     * @param in the input stream
     * @param out the output writer
     */
    public Messenger(BufferedReader in, PrintWriter out) {
        this.in = in;
        this.out = out;
    }

    /**
     * Subscribe consumers for incoming messages.
     *
     * @param consumer consumer to process incoming message.
     */
    public void userInputSubscribe(Consumer<I> consumer) {
        onMessageConsumers.add(consumer);
    }

    /**
     * Send message to output.
     *
     * @param message the message to sent.
     */
    synchronized public void send(O message) {
        LOGGER.debug("Sending message: {}", message);
        out.println(message);
    }

    /**
     * Start listening on incoming messages.
     */
    public void startListening() {
        try {
            do {
                String incomingMessage = in.readLine();
                if (!Objects.isNull(incomingMessage) && !incomingMessage.isEmpty()) {
                    LOGGER.info("Received message: {}", incomingMessage);
                    onMessageConsumers.forEach(consumer -> consumer.accept((I) incomingMessage));
                    if(incomingMessage.equals("EXIT")) break;
                }
            } while (true);
        } catch (IOException e) {
            LOGGER.error("Error while reading input. ", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Start listening only on first line.
     *
     * @return the first line.
     */
    public I readNextLineSync() {
        try {
            do {
                String incomingMessage = in.readLine();
                if (!Objects.isNull(incomingMessage) && !incomingMessage.isEmpty()) {
                    LOGGER.info("Reading one line: {}", incomingMessage);
                    return (I) incomingMessage;
                }
            } while (true);
        } catch (IOException e) {
            LOGGER.error("Error while reading one line input. ", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
