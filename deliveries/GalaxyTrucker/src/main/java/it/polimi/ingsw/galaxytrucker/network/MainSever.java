package it.polimi.ingsw.galaxytrucker.network;

import it.polimi.ingsw.galaxytrucker.controller.GameController;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.ServerRMI;
import it.polimi.ingsw.galaxytrucker.network.socket.server.SocketServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class MainSever {
    //asks the user the game settings (number of players and first flight/std game) and launches
    // the Socket and RMI servers
    public static void main(String[] args) {
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

        GameController controller = new GameController(firstFlight, numPlayers);

        new Thread(() -> {
            try {
                startServerRMI(controller);
            } catch (RemoteException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }).start();

        new Thread(() -> {
            try {
                startSocketServer(controller);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }).start();
    }

    //launches the socket server
    private static void startSocketServer(GameController controller) throws IOException {
        ServerSocket listenSocket = new ServerSocket(1235);
        System.out.println("Socket server running...");
        new SocketServer(listenSocket, controller).runServer();
    }

    //launches the RMI server
    private static void startServerRMI(GameController controller) throws RemoteException {
        //System.setProperty("java.rmi.server.hostname", "172.20.10.3");
        ServerRMI server = new ServerRMI(controller);
        final String serverName = "GalaxyTruckerServer";
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind(serverName, server);
        System.out.println("RMI server running...");
    }
}
