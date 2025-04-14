package it.polimi.ingsw.galaxytrucker.network.socket.server;

import it.polimi.ingsw.galaxytrucker.controller.GameController;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class SocketServer {
    private final ServerSocket listenSocket;
    private final GameController controller;

    //bind listen socket and initialize controller with given params
    public SocketServer(ServerSocket listenSocket, boolean firstFlight, int numPlayers) {
        this.listenSocket = listenSocket;
        this.controller = new GameController(firstFlight, numPlayers);
    }

    // receive connection from client and then create a thread for every client (connection limited due to number of players)
    private void runServer() throws IOException {
        Socket clientSocket;
        while ((clientSocket = this.listenSocket.accept()) != null) {
            System.out.println("Accepted connection from " + clientSocket.getInetAddress().getHostAddress());
            SocketClientHandler handler = new SocketClientHandler(clientSocket, controller);

            new Thread(() -> {
                try {
                    handler.runVirtualView();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }

    }

    //server launch with given params
    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(args[0]);
        int numPlayers;
        String ff;
        boolean firstFlight;
        Scanner inputScanner = new Scanner(System.in);
        do{
            System.out.println("Number of players (from 1 to 4): ");
            numPlayers = Integer.parseInt(inputScanner.nextLine());
        }while(numPlayers>4 || numPlayers<1);
        do{
            System.out.println("Standard game (S) or first flight (F): ");
            ff = inputScanner.nextLine();
        }while(!ff.equals("F") && !ff.equals("S"));
        firstFlight = (ff.equals("F"));
        ServerSocket listenSocket = new ServerSocket(port);
        System.out.println("Server bound...");
        new SocketServer(listenSocket, firstFlight, numPlayers).runServer();
    }
}
