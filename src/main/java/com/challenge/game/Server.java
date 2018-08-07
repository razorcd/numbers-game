package com.challenge.game;

import com.challenge.game.service.GameRoundService;
import com.challenge.game.service.algorithm.DivideByThree;
import com.challenge.game.service.algorithm.GameAlgorithm;
import com.challenge.game.service.algorithm.WinLogic;
import com.challenge.game.service.algorithm.WinWhenOne;
import com.challenge.game.service.domain.GameRoundResult;
import com.challenge.game.service.domain.InputNumber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);

        clientSocket = serverSocket.accept();
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));



        GameAlgorithm gameAlgorithm = new DivideByThree();
        WinLogic winLogic = new WinWhenOne();
        GameRoundService gameRoundService = new GameRoundService(gameAlgorithm, winLogic);

//        int startVal = new Random().ints(2, 100).findFirst().getAsInt();
        int startVal = 100;
        out.println(startVal);

        do {
            String incoming = in.readLine();
            if (incoming.equals("exit")) {break;}
            System.out.println("Received data: " + incoming);

            GameRoundResult gameRoundResult = gameRoundService.play(new InputNumber(Integer.parseInt(incoming)));
            System.out.println("Generated result: " + gameRoundResult);

            out.println(gameRoundResult);
        } while (true);



    }

    public void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }
    public static void main(String[] args) throws IOException {
        Server server=new Server();
        server.start(6666);
    }
}
