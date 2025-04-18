package it.polimi.ingsw.galaxytrucker.network.socket.client;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.network.socket.server.VirtualViewSocket;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class SocketClient implements VirtualViewSocket {
    private Socket clientSocket;
    private Scanner input;
    private SocketServerHandler output;
    protected SocketClient(Socket clientSocket) throws IOException {
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

    public void runCli()  {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.print("[INSERT_COMMAND]: ");
            String command = scan.nextLine();
            switch (command) {
                case "test":{ // message test
                    //this.output.test();
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
        new SocketClient(serverSocket).run();
    }

    //notifies a view about an error committed while executing a method on the remote server; the parameter
    //errorMessage describes the type of error
    @Override
    public void notifyError(String errorMessage) throws IOException{}

    //notifies a view about the fact that the corresponding player has been correctly added to the game, but
    //the server is waiting for other players in order to start the assembling phase; the parameter firstFlight
    //in needed for the view to determine which type of ship board/flight board to show to the user
    @Override
    public void updateWaitingForPlayers(boolean firstFlight) throws IOException{}

    //notifies a view about the presence of another player in the game; this method is invoked before the
    //beginning of the assembling phase, therefore just the nickname and color of the new player is required
    @Override
    public void updateNewPlayer(String nickname, Color color) throws IOException{}

    //notifies a view about the beginning of the assembling phase
    @Override
    public void updateStartAssembling() throws IOException{}

    //notifies the view about the fact that a component has been successfully picked/released (depending on
    //the value of the boolean parameter) by the corresponding player; the parameter imageID is needed for the
    //view in order to show the right component to the user
    @Override
    public void updatePickedComponent(int imageID, boolean released) throws IOException{}

    //notifies the view about the fact that a shown component has been picked/released (depending on the value
    //of the boolean parameter); the parameter imageID is needed for the view in order to show the right
    //component to the user
    @Override
    public void updateShownComponent(int imageID, boolean released) throws IOException{}

    //notifies the view about the fact that a player (identified by the nickname parameter) has picked a reserved
    //component/ reserved a component (depending on the value of the boolean parameter); the parameter imageID
    //is needed for the view in order to show the right component to the user
    @Override
    public void updateReservedComponent(String nickname, int imageID, boolean released) throws IOException{}

    //notifies the view about the fact that the picked component of the corresponding player has been rotated
    @Override
    public void updateRotatePickedComponent() throws IOException{}

    //notifies the view about the fact that a player (identified by the nickname parameter) has assembled a
    //component in position (x,y) of its ship board; the parameter imageID is needed for the view in order
    //to show the right component to the user
    @Override
    public void updateAssembledComponent(String nickname, int imageID, Orientation orientation, int x, int y) throws IOException{}

    //notifies the view about the fact that the corresponding player has successfully picked a deck; the parameter
    //contains the list of image IDs of the cards contained in the deck, so that the view can show the
    //correct adventure cards to the user
    @Override
    public void updatePickedDeck(List<Integer> deckIDs) throws IOException{}

    //notifies the view about the fact that the corresponding player has successfully released a deck
    @Override
    public void updateReleasedDeck() throws IOException{}

    //notifies the view about the fact that the corresponding player has finished the assembling phase and is
    //correctly positioned on the flight board; still, other players have to finish building their ships
    @Override
    public void updateFinishAssembling(String nickname, int position) throws IOException{}

    //notifies the view that all the players have concluded the assembling phase, which means that the players
    //enter the ship control phase
    @Override
    public void updateShipControl() throws IOException{}

    //runs a command line interface to send requests to the server
    @Override
    public void runCli(VirtualServer server) throws IOException{}
}
