package it.polimi.ingsw.galaxytrucker.network.rmi.server;

import it.polimi.ingsw.galaxytrucker.controller.GameController;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class ServerRMI extends UnicastRemoteObject {
    private final GameController controller;
    final Map<String, VirtualViewRMI> clients = new HashMap<>();

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

    public void addPlayer(VirtualViewRMI client, String nickname, Color color) {
        try{
            controller.addPlayer(client, nickname, color);
            clients.put(nickname, client);
        }
        catch(Exception e){
            client.notifyError(e.getMessage());
        }
    }
}
