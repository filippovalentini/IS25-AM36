package it.polimi.ingsw.galaxytrucker.network.socket.server;

import it.polimi.ingsw.galaxytrucker.controller.GameController;

import java.io.*;
import java.net.*;
import java.util.Map;



/**
 * This class contains all the logic needed to instantiate the concurrent socket server and to handle client connections
 */
public class SocketServer {
    private final ServerSocket listenSocket;            //accepts client connections
    private final Map<Integer, GameController> controllers;     //maps each controller with the ID of the respective game

    //constructor, initializes the controllers mapping with a provided empty map
    /**
     * Constructor for SocketServer.
     *
     * @param listenSocket the ServerSocket to listen for client connections
     * @param controllers  a map of game controllers indexed by their game ID
     */
    public SocketServer(ServerSocket listenSocket, Map<Integer, GameController> controllers) {
        this.listenSocket = listenSocket;
        this.controllers = controllers;
    }

    //accepts client connections and for each client creates a thread to handle message communication
    /**
     * Runs the server, accepting client connections and managing them in separate threads.
     *
     * @throws IOException if an I/O error occurs when accepting connections
     */
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
