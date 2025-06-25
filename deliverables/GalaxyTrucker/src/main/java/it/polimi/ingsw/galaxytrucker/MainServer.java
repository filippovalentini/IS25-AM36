package it.polimi.ingsw.galaxytrucker;

import it.polimi.ingsw.galaxytrucker.controller.GameController;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.ServerRMI;
import it.polimi.ingsw.galaxytrucker.network.socket.server.SocketServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.GuiInterface;
import it.polimi.ingsw.galaxytrucker.ui.tui.TuiInterface;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

public class MainServer {
    //launches the Socket and RMI Servers based on the parameters (IP + port) provided by the user (otherwise
    //it uses default parameters
    public static void main(String[] args) {
        Map<Integer, GameController> controllers = new HashMap<>();
        int rmiPort;
        int socketPort;
        if(args.length > 1 && args[1].equals("--port")){
            if(args.length==4) {
                rmiPort = Integer.parseInt(args[2]);
                socketPort = Integer.parseInt(args[3]);
            }else{
                System.out.println("wrong arguments! using default ports...");
                rmiPort = 1234;
                socketPort = 1235;
            }
        } else { // default ports
            rmiPort = 1234;
            socketPort = 1235;
        }
        new Thread(() -> {
            try {
                startServerRMI(controllers, args[0], rmiPort);
            } catch (RemoteException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }).start();

        new Thread(() -> {
            try {
                startSocketServer(controllers, socketPort);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }).start();
    }

    //launches the socket server
    private static void startSocketServer(Map<Integer, GameController> controllers, int port) throws IOException {
        ServerSocket listenSocket = new ServerSocket(port);
        System.out.println("Socket server running...");
        new SocketServer(listenSocket, controllers).runServer();
    }

    //launches the RMI server
    private static void startServerRMI(Map<Integer, GameController> controllers, String ipV4, int port) throws RemoteException {
        System.setProperty("java.rmi.server.hostname", ipV4);
        ServerRMI server = new ServerRMI(controllers);
        final String serverName = "GalaxyTruckerServer";
        Registry registry = LocateRegistry.createRegistry(port);
        registry.rebind(serverName, server);
        System.out.println("RMI server running...");
    }
}
