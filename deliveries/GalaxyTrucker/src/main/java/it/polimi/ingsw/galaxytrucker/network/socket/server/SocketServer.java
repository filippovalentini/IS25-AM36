package it.polimi.ingsw.galaxytrucker.network.socket.server;

import it.polimi.ingsw.galaxytrucker.controller.GameController;

import java.io.*;
import java.net.*;

//this class contains all the logic needed to instantiate the concurrent socket server and to handle client connections
public class SocketServer {
    private final ServerSocket listenSocket;
    private final GameController controller;

    //bind listen socket and initialize controller with given params
    public SocketServer(ServerSocket listenSocket, GameController controller) {
        this.listenSocket = listenSocket;
        this.controller = controller;
    }

    // receive connection from client and then create a thread for every client (connection limited due to number of players)
    public void runServer() throws IOException {
        Socket clientSocket;
        while ((clientSocket = this.listenSocket.accept()) != null) {
            System.out.println("Accepted connection from " + clientSocket.getInetAddress().getHostAddress());
            SocketClientHandler clientHandler = new SocketClientHandler(clientSocket, controller);

            new Thread(() -> {
                try {
                    clientHandler.manageClientMessages();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }

    }

}
