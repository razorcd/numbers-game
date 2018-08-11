package com.challenge;

import com.challenge.server.MainServer;
import com.challenge.application.utils.PropertiesConfigLoader;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ServerApp {
    static {initializeGlobalConfiguration();}

    private static final String PORT = PropertiesConfigLoader.getProperties().getProperty("com.challenge.server.port");
    private static final String SERVER_LISTENERS_COUNT = PropertiesConfigLoader.getProperties().getProperty("com.challenge.server.server_listeners_count");

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerApp.class);

    public static void main(String[] args) {
        LOGGER.info("Starting application.");

        MainServer server = new MainServer(Integer.parseInt(PORT));
        ServerSocket mainServerSocket = server.start();

        runServerListenerThreads(mainServerSocket);

        server.stop();

        LOGGER.info("Application closed.");
    }

    /**
     * Runs main application is separate threads.
     *
     * @param mainServerSocket port of the main server.
     */
    private static void runServerListenerThreads(ServerSocket mainServerSocket) {
        int serverListenersCount = Integer.parseInt(SERVER_LISTENERS_COUNT);
        ExecutorService executorService = new ThreadPoolExecutor(
                serverListenersCount, serverListenersCount,0, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

        for (int i = 0; i < serverListenersCount; i++) {
            executorService.execute(new ServerListener(mainServerSocket));
        }
        executorService.shutdown();

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.error("InterruptedException while running main thread. {}", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Initialize global configurations.
     */
    private static void initializeGlobalConfiguration() {
        PropertyConfigurator.configure(PropertyConfigurator.class.getClassLoader().getResourceAsStream("log4j.properties"));
        PropertiesConfigLoader.initialize("application.properties");
    }
}

