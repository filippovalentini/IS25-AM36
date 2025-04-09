package it.polimi.ingsw.galaxytrucker.network.socket.client;

import it.polimi.ingsw.galaxytrucker.network.socket.server.VirtualViewSocket;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientSocket implements VirtualViewSocket {
    private Socket clientSocket;
    private Scanner input;
    private SocketServerHandler output;
    protected ClientSocket(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.input = new Scanner(clientSocket.getInputStream());
        this.output = new SocketServerHandler(new PrintWriter(clientSocket.getOutputStream()));
    }

    private void run() {
        new Thread(() -> {
            try {
                runVirtualServer(); // run actual virtual server
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
        runCli();
    }

    // server to client messages
    private void runVirtualServer() throws IOException {
        String line;
        while (input.hasNext()) {
            line = input.nextLine();
            System.out.println("message reveiveded: " + line);
            // json de-serialization
        }
    }

    private void runCli()  {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.print("[INSERT_COMMAND]: ");
            String command = scan.nextLine();
            switch (command) {
                case "test":{ // message test
                    this.output.test();
                }
                default:{
                    break;
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        Socket serverSocket = new Socket(host, port);
        new ClientSocket(serverSocket).run();
    }
}
