package it.polimi.ingsw.galaxytrucker.network.socket.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public abstract class SocketClientHandler implements VirtualViewSocket {
    Socket socket;

    public SocketClientHandler(Socket socket) {
        this.socket = socket;

       /* public void runVirtualView() {
            try {
                Scanner in = new Scanner(socket.getInputStream());
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                while (true) {
                    String line = in.nextLine();
                    if (line.equals("exit")) { // object received to stop the virtual view
                        break;
                    } else if (line.equals("test")) {
                        System.out.println("test request received from client!");
                        new PrintWriter(socket.getOutputStream()).println("[SERVER]: test request received!");
                    } else {
                        // serialization through JSON not implemented, yet
                        // it must de-serialize JSON obj and then call the respective update
                    }
                }
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
        */

    }
}
