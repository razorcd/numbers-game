package com.challenge.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public String sendMessage(String msg) throws IOException {
        out.println(msg);
        String resp = in.readLine();
        return resp;
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }


    public static void main(String[] args) throws IOException {
        Socket clientSocket = new Socket("127.0.0.1", 6666);
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        do {
            String incoming = in.readLine();
            if (incoming.equals("exit")) {break;}
            System.out.println("Received number: " + incoming);



//            if (gameRoundResult.isWinner()) {
//                out.println("exit");
//                break;
//            } else {
//                out.println(gameRoundResult.getOutputNumber().getValue());
//            }
        } while (true);


        in.close();
        out.close();
        clientSocket.close();
    }
}

