package it.polimi.ingsw.galaxytrucker.network.rmi.client;

import it.polimi.ingsw.galaxytrucker.client.TUI;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.VirtualViewRMI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Scanner;

public class ClientRMI extends UnicastRemoteObject implements VirtualViewRMI {
    private VirtualServerRMI server;
    private TUI tui;
    private String nickname;
    private Color color;

    public ClientRMI(VirtualServerRMI server, String nickname, Color color) throws RemoteException {
        super();
        this.server = server;
        this.nickname = nickname;
        this.color = color;
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        final String serverName = "GalaxyTruckerServer";
        Registry registry = LocateRegistry.getRegistry(args[0], 1234);
        VirtualServerRMI server = (VirtualServerRMI) registry.lookup(serverName);
        System.out.println("Obtained remote object...");
        System.out.println("Insert nickname: ");
        Scanner inputScanner = new Scanner(System.in);
        String nickname = inputScanner.nextLine();
        System.out.println("Insert color: ");
        String color = inputScanner.nextLine();
        Color colorEnum = switch (color) {
            case "RED" -> Color.RED;
            case "GREEN" -> Color.GREEN;
            case "BLUE" -> Color.BLUE;
            case "YELLOW" -> Color.YELLOW;
            default -> null;
        };
        VirtualViewRMI client = new ClientRMI(server, nickname, colorEnum);
        server.addPlayer(client, nickname, colorEnum);
        client.runCli();
    }

    public void printRules(){
        System.out.println("Comandi disponibili:");
        System.out.println("1 - pickHidden");
        System.out.println("2 - pickShown <index>");
        System.out.println("3 - putShown");
        System.out.println("4 - reserveComponent");
        System.out.println("5 - pickReservedComponent <position>");
        System.out.println("6 - rotatePickedComponent");
        System.out.println("7 - assembleComponent <x> <y>");
        System.out.println("8 - pickDeck <deckNumber>");
        System.out.println("9 - releaseDeck");
        System.out.println("10 - setPosition <initCell>");
        System.out.println("0 - exit");
    }


    //runs a command line interface to send requests to the server
    @Override
    public void runCli() throws RemoteException {
        Scanner scan = new Scanner(System.in);

        printRules();

        while (true) {
            System.out.print("> ");
            String input = scan.nextLine().trim();
            String[] tokens = input.split("\\s+");
            if (tokens.length == 0) continue;

            try {
                int command = Integer.parseInt(tokens[0]);
                switch (command) {
                    case 1:
                        server.pickHidden(nickname);
                        break;
                    case 2:
                        if (tokens.length < 2) {
                            System.out.println("Errore: inserire un indice.");
                            break;
                        }
                        int index = Integer.parseInt(tokens[1]);
                        server.pickShown(nickname, index);
                        break;
                    case 3:
                        server.putShown(nickname);
                        break;
                    case 4:
                        server.reserveComponent(nickname);
                        break;
                    case 5:
                        if (tokens.length < 2) {
                            System.out.println("Errore: inserire una posizione.");
                            break;
                        }
                        int pos = Integer.parseInt(tokens[1]);
                        server.pickReservedComponent(nickname, pos);
                        break;
                    case 6:
                        server.rotatePickedComponent(nickname);
                        break;
                    case 7:
                        if (tokens.length < 3) {
                            System.out.println("Errore: inserire x e y.");
                            break;
                        }
                        int x = Integer.parseInt(tokens[1]);
                        int y = Integer.parseInt(tokens[2]);
                        server.assembledComponent(nickname, x, y);
                        break;
                    case 8:
                        if (tokens.length < 2) {
                            System.out.println("Errore: inserire un numero di mazzo.");
                            break;
                        }
                        int deck = Integer.parseInt(tokens[1]);
                        server.pickDeck(nickname, deck);
                        break;
                    case 9:
                        server.releaseDeck(nickname);
                        break;
                    case 10:
                        if (tokens.length < 2) {
                            System.out.println("Errore: inserire una posizione iniziale.");
                            break;
                        }
                        int initCell = Integer.parseInt(tokens[1]);
                        server.setPosition(nickname, initCell);
                        break;
                    case 11:
                        printRules();
                        break;
                    case 0:
                        System.out.println("Chiusura client...");
                        return;
                    default:
                        System.out.println("Comando non valido.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Formato numero non valido.");
            } catch (RemoteException e) {
                System.out.println("Errore remoto: " + e.getMessage());
            }
            this.tui.visualize();
        }
    }


    //notifies a view about an error committed while executing a method on the remote server; the parameter
    //errorMessage describes the type of error
    @Override
    public void notifyError(String errorMessage) throws RemoteException {
        System.out.println(errorMessage);
    }

    //notifies a view about the fact that the corresponding player has been correctly added to the game, but
    //the server is waiting for other players in order to start the assembling phase; the parameter firstFlight
    //in needed for the view to determine which type of ship board/flight board to show to the user
    @Override
    public void updateWaitingForPlayers(boolean firstFlight) throws RemoteException {
        this.tui = new TUI(this.nickname, this.color, firstFlight);
    }


    //notifies a view about the beginning of the assembling phase
    @Override
    public void updateStartAssembling() throws RemoteException {
        this.tui.updateStartAssembling();
    }

    //notifies the view about the fact that a component has been successfully picked/released (depending on
    //the value of the boolean parameter) by the corresponding player; the parameter imageID is needed for the
    //view in order to show the right component to the user
    @Override
    public void updatePickedComponent(int imageID, boolean released) throws RemoteException {
        this.tui.updatePickedComponent(imageID, released);
    }

    //notifies the view about the fact that a shown component has been picked/released (depending on the value
    //of the boolean parameter); the parameter imageID is needed for the view in order to show the right
    //component to the user
    @Override
    public void updateShownComponent(int imageID, boolean released) throws RemoteException {
        this.tui.updateShownComponent(imageID, released);
    }

    //notifies the view about the fact that a player (identified by the nickname parameter) has picked a reserved
    //component/ reserved a component (depending on the value of the boolean parameter); the parameter imageID
    //is needed for the view in order to show the right component to the user
    @Override
    public void updateReservedComponent(String nickname, int imageID, boolean released) throws RemoteException {
        this.tui.updateReservedComponent(nickname, imageID, released);
    }

    //notifies the view about the fact that the picked component of the corresponding player has been rotated
    @Override
    public void updateRotatePickedComponent() throws RemoteException {
        this.tui.updateRotatePickedComponent();
    }

    //notifies the view about the fact that a player (identified by the nickname parameter) has assembled a
    //component in position (x,y) of its ship board; the parameter imageID is needed for the view in order
    //to show the right component to the user
    @Override
    public void updateAssembledComponent(String nickname, int imageID, Orientation orientation, int x, int y) throws RemoteException {
        this.tui.updateAssembledComponent(nickname, imageID, orientation, x, y);
    }

    //notifies the view about the fact that the corresponding player has successfully picked a deck; the parameter
    //contains the list of image IDs of the cards contained in the deck, so that the view can show the
    //correct adventure cards to the user
    @Override
    public void updatePickedDeck(List<Integer> deckIDs) throws RemoteException {
        this.tui.updatePickedDeck(deckIDs);
    }

    //notifies the view about the fact that the corresponding player has successfully released a deck
    @Override
    public void updateReleasedDeck() throws RemoteException {
        this.tui.updateReleasedDeck();
    }

    //notifies the view about the fact that the corresponding player has finished the assembling phase and is
    //correctly positioned on the flight board; still, other players have to finish building their ships
    @Override
    public void updateFinishAssembling() throws RemoteException {
        this.tui.updateFinishAssembling();
    }

    //notifies the view that all the players have concluded the assembling phase, which means that the players
    //enter the ship control phase
    @Override
    public void updateShipControl() throws RemoteException {
        this.tui.updateShipControl();
    }

}
