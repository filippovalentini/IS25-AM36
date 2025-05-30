package it.polimi.ingsw.galaxytrucker.network.rmi.server;

import it.polimi.ingsw.galaxytrucker.network.rmi.client.ClientRMI;

import java.rmi.RemoteException;

public class PingThreadRMI extends Thread {
    int gameID;
    String nickname;
    private ServerRMI server;
    private VirtualViewRMI remoteClient;

    public PingThreadRMI(ServerRMI server, VirtualViewRMI remoteClient, int gameID, String nickname) {
        this.server = server;
        this.remoteClient = remoteClient;
        this.gameID = gameID;
        this.nickname = nickname;
    }

    public void run() {
        while (true) {
            try {
                System.out.println("PING");
                remoteClient.ping();
                Thread.sleep(5000);
            } catch (Exception e) {
                try{
                    server.manageClientDisconnection(gameID, nickname);
                    break;
                } catch (RemoteException ignored) {
                    break;
                }
            }
        }
    }
}
