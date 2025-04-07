package it.polimi.ingsw.galaxytrucker.rmi.server;

import it.polimi.ingsw.galaxytrucker.controller.GameController;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerRMI extends UnicastRemoteObject {
    private final GameController controller;

    public ServerRMI(boolean firstFlight, int numPlayers) throws RemoteException {
        super();
        this.controller = new GameController(firstFlight, numPlayers);
    }

    public static void main(String[] args) throws RemoteException {
        final String serverName = "GalaxyTruckerServer";
        ServerRMI server = new ServerRMI(false, 4);
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind(serverName, server);
        System.out.println("Server bound");
    }
}
