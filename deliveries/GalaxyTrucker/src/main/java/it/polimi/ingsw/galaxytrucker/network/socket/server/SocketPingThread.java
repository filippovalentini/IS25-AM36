package it.polimi.ingsw.galaxytrucker.network.socket.server;

import it.polimi.ingsw.galaxytrucker.network.socket.message.GameUpdateMessage;
import it.polimi.ingsw.galaxytrucker.network.socket.message.GameUpdateType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class SocketPingThread extends Thread {
    Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;
    SocketClientHandler clientHandler;

    public SocketPingThread(Socket socket, SocketClientHandler clientHandler, ObjectInputStream in, ObjectOutputStream out) {
        this.clientHandler = clientHandler;
        this.in = in;
        this.out = out;
        this.socket = socket;
    }


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
