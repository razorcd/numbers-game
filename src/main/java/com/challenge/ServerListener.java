package com.challenge;

import com.challenge.application.controller.CommandController;
import com.challenge.application.controller.mapper.UserInputDeserializer;
import com.challenge.application.game.GameService;
import com.challenge.server.ServerStream;
import com.challenge.server.SocketChannel;
import com.challenge.server.SocketChannelRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;

public class ServerListener implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerListener.class);

    private ServerSocket mainServerSocket;
    private ThreadLocal<SocketChannelRegistry> socketChannelRegistry;
    private ThreadLocal<GameService> gameManager;

    /**
     * Start listener on server socket.
     *
     * @param mainServerSocket the main server socket to connect to
     */
    public ServerListener(ServerSocket mainServerSocket,
                          ThreadLocal<SocketChannelRegistry> socketChannelRegistry,
                          ThreadLocal<GameService> gameManager) {
        this.mainServerSocket = mainServerSocket;
        this.socketChannelRegistry = socketChannelRegistry;
        this.gameManager = gameManager;
    }

    /**
     * Run application logic.
     * This is intended to be ran in new thread.
     */
    @Override
    public void run() {
        try (ServerStream serverStream = new ServerStream(mainServerSocket)) {
            SocketChannel socketChannel = serverStream.start();
            socketChannelRegistry.get().register(Thread.currentThread().getName(), socketChannel);
            socketChannel.setActiveSocketChannels(socketChannelRegistry.get().getActiveSocketChannels());

            //setup application controller
            CommandController commandController = new CommandController(gameManager.get(), socketChannel, new UserInputDeserializer());

            //listen on input stream
            socketChannel.send("connected");
            socketChannel.getInputStream()
                    .peek(commandController)
                    .filter(msg -> msg.equals("EXIT"))
                    .findAny();

            LOGGER.debug("ServerListener shutting down.");
        } catch (Exception e) {
            LOGGER.error("ServerListener {} exited with exception.", e);
        }
    }
}
