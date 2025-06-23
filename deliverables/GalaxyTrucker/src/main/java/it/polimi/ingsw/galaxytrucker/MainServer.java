package it.polimi.ingsw.galaxytrucker;

import it.polimi.ingsw.galaxytrucker.controller.GameController;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.ServerRMI;
import it.polimi.ingsw.galaxytrucker.network.socket.server.SocketServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

public class MainServer {
    //asks the user the game settings (number of players and first flight/std game) and launches
    // the Socket and RMI servers
    public static void main(String[] args) {
        Map<Integer, GameController> controllers = new HashMap<>();

        new Thread(() -> {
            try {
                startServerRMI(controllers, args[0]);
            } catch (RemoteException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }).start();

        new Thread(() -> {
            try {
                startSocketServer(controllers);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }).start();
    }

    //launches the socket server
    private static void startSocketServer(Map<Integer, GameController> controllers) throws IOException {
        ServerSocket listenSocket = new ServerSocket(1235);
        System.out.println("Socket server running...");
        new SocketServer(listenSocket, controllers).runServer();
    }

    //launches the RMI server
    private static void startServerRMI(Map<Integer, GameController> controllers, String ipV4) throws RemoteException {
        if(!ipV4.equals("127.0.0.1") && !ipV4.equals("localhost")) {
            System.setProperty("java.rmi.server.hostname", ipV4);
        }
        ServerRMI server = new ServerRMI(controllers);
        final String serverName = "GalaxyTruckerServer";
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind(serverName, server);
        System.out.println("RMI server running...");
    }
}
