package it.polimi.ingsw.galaxytrucker.network.rmi.server;

import it.polimi.ingsw.galaxytrucker.network.rmi.client.ClientRMI;

import java.rmi.RemoteException;

/**
 * This thread is responsible for periodically pinging the remote client to check if it is still alive.
 */
public class PingThreadRMI extends Thread {
    int gameID;
    String nickname;
    private ServerRMI server;
    private VirtualViewRMI remoteClient;

    /**
     * Constructor for PingThreadRMI.
     * @param server
     * @param remoteClient
     * @param gameID
     * @param nickname
     */
    public PingThreadRMI(ServerRMI server, VirtualViewRMI remoteClient, int gameID, String nickname) {
        this.server = server;
        this.remoteClient = remoteClient;
        this.gameID = gameID;
        this.nickname = nickname;
    }

    /**
     * This method runs the thread, which continuously pings the remote client every 5 seconds.
     */
    public void run() {
        while (true) {
            try {
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
