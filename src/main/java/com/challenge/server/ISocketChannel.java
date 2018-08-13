package com.challenge.server;

import java.util.Collection;
import java.util.stream.Stream;

public interface ISocketChannel {

    /**
     * Send message to output.
     *
     * @param message the message to sent.
     */
    void send(String message);

    /**
     * Send message to output.
     *
     * @param message the message to sent.
     */
    void sendWithDelay(String message);

    /**
     * Broadcast message to all active socket listeners.
     *
     * @param message the message to broadcast.
     */
    void broadcast(String message);

    /**
     * Start listening on incoming messages.
     */
    Stream<String> getInputStream();

    /**
     * Start listening only on first message.
     * Will block thread until a message is received.
     *
     * @return the new incoming message.
     */
    String readNextLineSync();

    /**
     * Check if there is any data to be consumed on the incoming stream.
     *
     * @return [boolean] if there is any data on the incoming stream.
     */
    boolean inputIsEmpty();

    /**
     * Consume all th data available on the stream.
     */
    void clearInput();

    /**
     * Add link to all active socket channels for broadcasting.
     *
     * @param allActiveSocketChannels all available socket channels
     */
    void setActiveSocketChannels(Collection<SocketChannel> allActiveSocketChannels);
}
