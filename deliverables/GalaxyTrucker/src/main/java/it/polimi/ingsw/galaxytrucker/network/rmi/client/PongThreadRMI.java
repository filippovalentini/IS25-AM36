package it.polimi.ingsw.galaxytrucker.network.rmi.client;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.VirtualViewRMI;

/**
 * This thread is responsible for periodically ponging the remote server to  send its heartbeat.
 */
public class PongThreadRMI extends Thread{
    private final VirtualServerRMI remoteServer;
    private final VirtualViewRMI client;

    /**
     * Constructor for PongThreadRMI.
     * @param remoteServer
     * @param client
     */
    public PongThreadRMI(VirtualServerRMI remoteServer, VirtualViewRMI client) {
        this.remoteServer = remoteServer;
        this.client = client;
    }

    /**
     * This method runs the thread, which continuously pongs the remote server every 2 seconds.
     */
    public void run(){
        try{
            while(true){
                Thread.sleep(2000);
                remoteServer.setClientStatus(client, 1);
            }
        }catch(Exception e){
            try{
                client.notifyError(e.getMessage());
            } catch (Exception ignored) {}
        }
    }
}
