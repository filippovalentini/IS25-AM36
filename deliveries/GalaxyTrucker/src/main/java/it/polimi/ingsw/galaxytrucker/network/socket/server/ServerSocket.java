package it.polimi.ingsw.galaxytrucker.network.socket.server;

import it.polimi.ingsw.galaxytrucker.controller.GameController;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public abstract class ServerSocket {
    private final java.net.ServerSocket listenSocket;
    private final GameController controller;
    final List<SocketClientHandler> clients = new ArrayList<>();
    private final int numPlayers;

    //bind listen socket and initialize controller with given params
    public ServerSocket(java.net.ServerSocket listenSocket, boolean firstFlight, int numPlayers) {
        this.listenSocket = listenSocket;
        this.numPlayers = numPlayers;
        this.controller = new GameController(firstFlight, numPlayers);
    }

    // receive connection from client and then create a thread for every client (connection limited due to number of players)
    private void runServer() throws IOException {
        Socket clientSocket = null;
        while ((clientSocket = this.listenSocket.accept()) != null && clients.size() < numPlayers) {
            //SocketClientHandler handler = new SocketClientHandler(clientSocket);
            synchronized (this.clients){
                //clients.add(handler); // save the client in clients list
            }
            // start the actual server virtual view in the dedicated thread
            //(new Thread(handler::runVirtualView)).start();
        }
    }

    //server launch with given params (example: "$IP $PORT leveltwo 4")
    public static void main(String[] args) throws IOException {
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String gameType  = args[2];
        boolean firstFlight = false;
        int players = Integer.parseInt(args[3]);
        if(gameType.equalsIgnoreCase("firstflight")){
            firstFlight = true;
        }else if(!gameType.equalsIgnoreCase("leveltwo")){
            throw new RuntimeException("Unknown gameType: " + gameType);
        }
        java.net.ServerSocket listenSocket = new java.net.ServerSocket(port);
        //new ServerSocket(listenSocket, firstFlight, players).runServer();
    }
}
