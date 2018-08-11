package com.challenge.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry to hold all active socket channels.
 */
public class SocketChannelRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketChannelRegistry.class);

    private final Map<String, SocketChannel> activeSocketChannels;

    public SocketChannelRegistry() {
        this.activeSocketChannels = new ConcurrentHashMap<>();
    }

    public void register(String id, SocketChannel listener) {
        LOGGER.debug("Registering socket channel id: {}, listener: {}.", id, listener.hashCode());
        activeSocketChannels.put(id, listener);
    }

    public Collection<SocketChannel> getActiveSocketChannels() {
        return activeSocketChannels.values();
    }
}
