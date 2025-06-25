package it.polimi.ingsw.galaxytrucker.network.socket.server;

import it.polimi.ingsw.galaxytrucker.network.socket.message.GameUpdateMessage;
import it.polimi.ingsw.galaxytrucker.network.socket.message.GameUpdateType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
/** * This thread is responsible for sending periodic ping messages to the client to check if it is still connected.
 * If the client does not respond, it will set the client status to disconnected.
 */
public class SocketPingThread extends Thread {
    Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;
    SocketClientHandler clientHandler;
    /**
     * Constructor for the SocketPingThread.
     *
     * @param socket         The socket connected to the client.
     * @param clientHandler  The handler for managing client connections.
     * @param in             The input stream to read messages from the client.
     * @param out            The output stream to send messages to the client.
     */
    public SocketPingThread(Socket socket, SocketClientHandler clientHandler, ObjectInputStream in, ObjectOutputStream out) {
        this.clientHandler = clientHandler;
        this.in = in;
        this.out = out;
        this.socket = socket;
    }

    /**
     * The run method of the thread that sends ping messages to the client every 5 seconds.
     * If the client does not respond, it sets the client status to disconnected and closes the streams and socket.
     */
    public void run() {
        try {
            while (true) {
                GameUpdateMessage pingMessage = new GameUpdateMessage(GameUpdateType.PING, new ArrayList<>());
                out.writeObject(pingMessage);
                out.flush();

                Thread.sleep(5000);

                if (clientHandler.isClientConnected()) {
                    clientHandler.setClientStatus(false);
                }
                else{
                    break;
                }
            }
        } catch (Exception ignored) {
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException ignored) {}
        }
    }
}
