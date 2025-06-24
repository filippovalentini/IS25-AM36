package it.polimi.ingsw.galaxytrucker.network.socket.server;

import it.polimi.ingsw.galaxytrucker.controller.GameController;

import java.io.*;
import java.net.*;
import java.util.Map;

//this class contains all the logic needed to instantiate the concurrent socket server and to handle client connections
public class SocketServer {
    private final ServerSocket listenSocket;            //accepts client connections
    private final Map<Integer, GameController> controllers;     //maps each controller with the ID of the respective game

    //constructor, initializes the controllers mapping with a provided empty map
    public SocketServer(ServerSocket listenSocket, Map<Integer, GameController> controllers) {
        this.listenSocket = listenSocket;
        this.controllers = controllers;
    }

    //accepts client connections and for each client creates a thread to handle message communication
    public void runServer() throws IOException {
        Socket clientSocket;
        while ((clientSocket = this.listenSocket.accept()) != null) {
            System.out.println("Accepted connection from " + clientSocket.getInetAddress().getHostAddress() + " on port " + clientSocket.getLocalPort());
            SocketClientHandler clientHandler = new SocketClientHandler(clientSocket);

            new Thread(() -> {
                try {
                    clientHandler.manageClientSetUp(controllers);
                    clientHandler.manageClientMessages();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }

    }

}
