package com.challenge.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Function;

public class Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(int port, Function<String,String> controller  ) {
        try {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            do {
                String incoming = in.readLine();
                LOGGER.debug("Received data: {}", incoming);
                if (incoming.equals("exit")) {
                    break;
                }

                String response = controller.apply(incoming);

                LOGGER.debug("Sending response: {}", response);
                out.println(response);
            } while (true);


        } catch (IOException e) {
            LOGGER.error("IOException while server is running.");
        } finally {
            stop();
        }
    }

    public void stop() {
        out.println("exit");
        try {
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException ex) {
            throw new RuntimeException("IO exception while closing connections.", ex);
        }
    }
}


