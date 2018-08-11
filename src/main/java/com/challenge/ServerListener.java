package com.challenge;

import com.challenge.controller.CommandController;
import com.challenge.controller.mapper.UserInputDeserializer;
import com.challenge.server.ServerStream;
import com.challenge.server.SocketChannel;

import java.net.ServerSocket;

public class ServerListener implements Runnable {

    private ServerSocket mainServerSocket;

    /**
     * Start listener on server socket.
     *
     * @param mainServerSocket the main server socket to connect to
     */
    public ServerListener(ServerSocket mainServerSocket) {
        this.mainServerSocket = mainServerSocket;
    }

    /**
     * Run application logic.
     * This is intended to be ran in new thread.
     */
    @Override
    public void run() {
        try (ServerStream serverStream = new ServerStream(mainServerSocket)) {
            SocketChannel socketChannel = serverStream.start();

            //setup application
            GameContext gameContext = new GameContext();
            CommandController commandController = new CommandController(gameContext, socketChannel, new UserInputDeserializer());

            //listen on input stream
            socketChannel.send("connected");
            socketChannel.getInputStream()
                    .peek(commandController)
                    .filter(msg -> msg.equals("EXIT"))
                    .findAny();
        }
    }
}
