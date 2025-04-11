package it.polimi.ingsw.galaxytrucker.network.socket.client;

import it.polimi.ingsw.galaxytrucker.network.rmi.client.VirtualServerRMI;

import java.io.PrintWriter;

public abstract class SocketServerHandler implements VirtualServerSocket {
    final PrintWriter output;

    public SocketServerHandler(PrintWriter output) {
        this.output = output;
    }

    // messages sent to server

    /*
        ex. pickComponent() etc.
     */
    public void test(){
        output.println("test"); // send "test" to server
        output.flush();
    }
}
